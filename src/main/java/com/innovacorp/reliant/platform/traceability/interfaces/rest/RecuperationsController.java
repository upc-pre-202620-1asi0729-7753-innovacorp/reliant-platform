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
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetAllCustomersByLinkedAssetOwnerOrganizationIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetAllRecuperationsByCustomerIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetAllRecuperationsBySupplierOrganizationIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetCustomerByIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetRecuperationByIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.CustomerId;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.OpenRecuperationResource;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.RecuperationResource;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.transform.OpenRecuperationCommandFromResourceAssembler;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.transform.RecuperationResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/recuperations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Recuperations", description = "Recuperation order management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class RecuperationsController {

    private final RecuperationCommandService recuperationCommandService;
    private final RecuperationQueryService recuperationQueryService;
    private final CustomerQueryService customerQueryService;
    private final CurrentUserProvider currentUserProvider;

    public RecuperationsController(RecuperationCommandService recuperationCommandService,
                                   RecuperationQueryService recuperationQueryService,
                                   CustomerQueryService customerQueryService,
                                   CurrentUserProvider currentUserProvider) {
        this.recuperationCommandService = recuperationCommandService;
        this.recuperationQueryService = recuperationQueryService;
        this.customerQueryService = customerQueryService;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping
    @Operation(summary = "Open a recuperation order")
    @ApiResponse(responseCode = "201", description = "Recuperation opened",
            content = @Content(schema = @Schema(implementation = RecuperationResource.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "403", description = "Only suppliers can open recuperations")
    @ApiResponse(responseCode = "404", description = "Customer or component not found")
    @ApiResponse(responseCode = "409", description = "Work order number already used")
    public ResponseEntity<?> openRecuperation(@Valid @RequestBody OpenRecuperationResource resource) {
        var command = OpenRecuperationCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = recuperationCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, RecuperationResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get the recuperations visible to the authenticated organization")
    @ApiResponse(responseCode = "200", description = "Recuperations retrieved")
    public ResponseEntity<List<RecuperationResource>> getAllRecuperations() {
        var currentUser = currentUserProvider.current();
        List<Recuperation> recuperations;

        if (currentUser.isAssetOwner()) {
            recuperations = customerQueryService.handle(
                            new GetAllCustomersByLinkedAssetOwnerOrganizationIdQuery(currentUser.organizationId()))
                    .stream()
                    .flatMap(customer -> recuperationQueryService.handle(
                            new GetAllRecuperationsByCustomerIdQuery(new CustomerId(customer.getId()))).stream())
                    .toList();
        } else {
            recuperations = recuperationQueryService.handle(
                    new GetAllRecuperationsBySupplierOrganizationIdQuery(
                            new OrganizationId(currentUser.organizationId())));
        }

        return ResponseEntity.ok(recuperations.stream()
                .map(RecuperationResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @GetMapping("/{recuperationId}")
    @Operation(summary = "Get a recuperation by id")
    @ApiResponse(responseCode = "200", description = "Recuperation found")
    @ApiResponse(responseCode = "404", description = "Recuperation not found")
    public ResponseEntity<?> getRecuperationById(@PathVariable Long recuperationId) {
        var currentUser = currentUserProvider.current();
        var recuperation = recuperationQueryService.handle(new GetRecuperationByIdQuery(recuperationId))
                .filter(existing ->
                        (currentUser.isSupplier() && existing.isOwnedBy(currentUser.organizationId()))
                                || (currentUser.isAssetOwner()
                                && customerQueryService.handle(new GetCustomerByIdQuery(existing.getCustomerIdValue()))
                                .map(customer -> customer.isLinkedTo(currentUser.organizationId()))
                                .orElse(false)));

        if (recuperation.isEmpty()) {
            return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(
                    ApplicationError.notFound("recuperation", recuperationId.toString()));
        }

        return ResponseEntity.ok(RecuperationResourceFromEntityAssembler.toResourceFromEntity(recuperation.get()));
    }

    @PatchMapping("/{recuperationId}/completion")
    @Operation(summary = "Complete a recuperation",
            description = "Completes an IN_PROGRESS or REWORK recuperation and returns its component to IN_SERVICE.")
    @ApiResponse(responseCode = "200", description = "Recuperation completed",
            content = @Content(schema = @Schema(implementation = RecuperationResource.class)))
    @ApiResponse(responseCode = "403", description = "Only suppliers can complete recuperations")
    @ApiResponse(responseCode = "404", description = "Recuperation or component not found")
    @ApiResponse(responseCode = "422", description = "Recuperation cannot be completed in its current state")
    public ResponseEntity<?> completeRecuperation(@PathVariable Long recuperationId) {
        var result = recuperationCommandService.handle(new CompleteRecuperationCommand(recuperationId));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, RecuperationResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}