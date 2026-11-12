package com.innovacorp.reliant.platform.traceability.interfaces.rest;

import com.innovacorp.reliant.platform.shared.application.context.CurrentUserProvider;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.interfaces.rest.transform.ProblemDetailFromApplicationErrorAssembler;
import com.innovacorp.reliant.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.innovacorp.reliant.platform.traceability.application.commandservices.RecoveredComponentCommandService;
import com.innovacorp.reliant.platform.traceability.application.queryservices.CustomerQueryService;
import com.innovacorp.reliant.platform.traceability.application.queryservices.RecoveredComponentQueryService;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetAllComponentsByCustomerIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetComponentByIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetCustomerByIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.CustomerId;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.ComponentResource;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.RegisterComponentResource;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.transform.ComponentResourceFromEntityAssembler;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.transform.RegisterComponentCommandFromResourceAssembler;
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
@RequestMapping(value = "/api/v1/components", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Components", description = "Recovered component management endpoints")
public class ComponentsController {
    private final RecoveredComponentCommandService componentCommandService;
    private final RecoveredComponentQueryService componentQueryService;
    private final CustomerQueryService customerQueryService;
    private final CurrentUserProvider currentUserProvider;

    public ComponentsController(RecoveredComponentCommandService componentCommandService, RecoveredComponentQueryService componentQueryService,
                                CustomerQueryService customerQueryService, CurrentUserProvider currentUserProvider) {
        this.componentCommandService = componentCommandService;
        this.componentQueryService = componentQueryService;
        this.customerQueryService = customerQueryService;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping
    @Operation(summary = "Register a component", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Component registered", content = @Content(schema = @Schema(implementation = ComponentResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Not the supplier of the customer"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "409", description = "Serial number already registered for the customer")
    })
    public ResponseEntity<?> registerComponent(@Valid @RequestBody RegisterComponentResource resource,
                                               @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var result = componentCommandService.handle(RegisterComponentCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, ComponentResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get the components of a customer", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Components retrieved", content = @Content(schema = @Schema(implementation = ComponentResource.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found or not visible")
    })
    public ResponseEntity<?> getAllComponentsByCustomerId(@RequestParam Long customerId,
                                                          @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var currentUser = currentUserProvider.current();
        var customer = customerQueryService.handle(new GetCustomerByIdQuery(customerId))
                .filter(c -> c.isOwnedBy(currentUser.organizationId()) || c.isLinkedTo(currentUser.organizationId()));
        if (customer.isEmpty()) {
            return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(ApplicationError.notFound("customer", customerId.toString()));
        }
        var components = componentQueryService.handle(new GetAllComponentsByCustomerIdQuery(new CustomerId(customerId)));
        return ResponseEntity.ok(components.stream().map(ComponentResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @GetMapping("/{componentId}")
    @Operation(summary = "Get a component by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Component found", content = @Content(schema = @Schema(implementation = ComponentResource.class))),
            @ApiResponse(responseCode = "404", description = "Component not found")
    })
    public ResponseEntity<?> getComponentById(@PathVariable Long componentId,
                                              @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var currentUser = currentUserProvider.current();
        var component = componentQueryService.handle(new GetComponentByIdQuery(componentId))
                .filter(c -> customerQueryService.handle(new GetCustomerByIdQuery(c.getCustomerIdValue()))
                        .map(customer -> customer.isOwnedBy(currentUser.organizationId()) || customer.isLinkedTo(currentUser.organizationId()))
                        .orElse(false));
        if (component.isEmpty()) {
            return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(ApplicationError.notFound("component", componentId.toString()));
        }
        return ResponseEntity.ok(ComponentResourceFromEntityAssembler.toResourceFromEntity(component.get()));
    }
}
