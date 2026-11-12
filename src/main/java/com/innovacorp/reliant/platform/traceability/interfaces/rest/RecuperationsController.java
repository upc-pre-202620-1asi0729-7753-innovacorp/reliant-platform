package com.innovacorp.reliant.platform.traceability.interfaces.rest;

import com.innovacorp.reliant.platform.shared.application.context.CurrentUserProvider;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.interfaces.rest.transform.ProblemDetailFromApplicationErrorAssembler;
import com.innovacorp.reliant.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.innovacorp.reliant.platform.traceability.application.commandservices.RecuperationCommandService;
import com.innovacorp.reliant.platform.traceability.application.queryservices.CustomerQueryService;
import com.innovacorp.reliant.platform.traceability.application.queryservices.RecuperationQueryService;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Recuperation;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.CompleteRecuperationCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.*;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.CustomerId;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.OpenRecuperationResource;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.RecuperationResource;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.transform.OpenRecuperationCommandFromResourceAssembler;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.transform.RecuperationResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/recuperations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Recuperations", description = "Recuperation order management endpoints")
public class RecuperationsController {
    private final RecuperationCommandService recuperationCommandService;
    private final RecuperationQueryService recuperationQueryService;
    private final CustomerQueryService customerQueryService;
    private final CurrentUserProvider currentUserProvider;

    public RecuperationsController(RecuperationCommandService recuperationCommandService, RecuperationQueryService recuperationQueryService,
                                   CustomerQueryService customerQueryService, CurrentUserProvider currentUserProvider) {
        this.recuperationCommandService = recuperationCommandService;
        this.recuperationQueryService = recuperationQueryService;
        this.customerQueryService = customerQueryService;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping
    @Operation(summary = "Open a recuperation order", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recuperation opened", content = @Content(schema = @Schema(implementation = RecuperationResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Not the supplier of the customer"),
            @ApiResponse(responseCode = "404", description = "Customer or component not found"),
            @ApiResponse(responseCode = "409", description = "Work order number already used")
    })
    public ResponseEntity<?> openRecuperation(@Valid @RequestBody OpenRecuperationResource resource,
                                              @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var result = recuperationCommandService.handle(OpenRecuperationCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, RecuperationResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get the recuperations visible to the authenticated organization", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recuperations retrieved", content = @Content(schema = @Schema(implementation = RecuperationResource.class)))
    })
    public ResponseEntity<List<RecuperationResource>> getAllRecuperations(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var currentUser = currentUserProvider.current();
        List<Recuperation> recuperations;
        if (currentUser.isAssetOwner()) {
            recuperations = customerQueryService.handle(new GetAllCustomersByLinkedAssetOwnerOrganizationIdQuery(currentUser.organizationId())).stream()
                    .flatMap(customer -> recuperationQueryService.handle(new GetAllRecuperationsByCustomerIdQuery(new CustomerId(customer.getId()))).stream())
                    .toList();
        } else {
            recuperations = recuperationQueryService.handle(new GetAllRecuperationsBySupplierOrganizationIdQuery(new OrganizationId(currentUser.organizationId())));
        }
        return ResponseEntity.ok(recuperations.stream().map(RecuperationResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @GetMapping("/{recuperationId}")
    @Operation(summary = "Get a recuperation by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recuperation found", content = @Content(schema = @Schema(implementation = RecuperationResource.class))),
            @ApiResponse(responseCode = "404", description = "Recuperation not found")
    })
    public ResponseEntity<?> getRecuperationById(@PathVariable Long recuperationId,
                                                 @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var currentUser = currentUserProvider.current();
        var recuperation = recuperationQueryService.handle(new GetRecuperationByIdQuery(recuperationId))
                .filter(r -> r.isOwnedBy(currentUser.organizationId())
                        || customerQueryService.handle(new GetCustomerByIdQuery(r.getCustomerIdValue())).map(c -> c.isLinkedTo(currentUser.organizationId())).orElse(false));
        if (recuperation.isEmpty()) {
            return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(ApplicationError.notFound("recuperation", recuperationId.toString()));
        }
        return ResponseEntity.ok(RecuperationResourceFromEntityAssembler.toResourceFromEntity(recuperation.get()));
    }

    @PatchMapping("/{recuperationId}/completion")
    @Operation(summary = "Complete a recuperation", description = "IN_PROGRESS | REWORK → COMPLETED; the component returns to IN_SERVICE.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recuperation completed", content = @Content(schema = @Schema(implementation = RecuperationResource.class))),
            @ApiResponse(responseCode = "403", description = "Not the owning supplier"),
            @ApiResponse(responseCode = "404", description = "Recuperation not found"),
            @ApiResponse(responseCode = "422", description = "Recuperation is not in progress")
    })
    public ResponseEntity<?> completeRecuperation(@PathVariable Long recuperationId,
                                                  @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var result = recuperationCommandService.handle(new CompleteRecuperationCommand(recuperationId));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, RecuperationResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
