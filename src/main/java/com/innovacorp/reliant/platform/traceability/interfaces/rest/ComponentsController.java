package com.innovacorp.reliant.platform.traceability.interfaces.rest;

import com.innovacorp.reliant.platform.shared.application.context.CurrentUser;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/components", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Components", description = "Recovered component management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ComponentsController {

    private final RecoveredComponentCommandService componentCommandService;
    private final RecoveredComponentQueryService componentQueryService;
    private final CustomerQueryService customerQueryService;
    private final CurrentUserProvider currentUserProvider;

    public ComponentsController(RecoveredComponentCommandService componentCommandService,
                                RecoveredComponentQueryService componentQueryService,
                                CustomerQueryService customerQueryService,
                                CurrentUserProvider currentUserProvider) {
        this.componentCommandService = componentCommandService;
        this.componentQueryService = componentQueryService;
        this.customerQueryService = customerQueryService;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping
    @Operation(summary = "Register a component")
    @ApiResponse(responseCode = "201", description = "Component registered",
            content = @Content(schema = @Schema(implementation = ComponentResource.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "403", description = "Only suppliers can register components")
    @ApiResponse(responseCode = "404", description = "Customer not found or not visible")
    @ApiResponse(responseCode = "409", description = "Serial number already registered for the customer")
    public ResponseEntity<?> registerComponent(@Valid @RequestBody RegisterComponentResource resource) {
        var command = RegisterComponentCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = componentCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ComponentResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get the components of a customer")
    @ApiResponse(responseCode = "200", description = "Components retrieved")
    @ApiResponse(responseCode = "404", description = "Customer not found or not visible")
    public ResponseEntity<?> getAllComponentsByCustomerId(@RequestParam Long customerId) {
        var currentUser = currentUserProvider.current();
        if (!canReadCustomer(customerId, currentUser)) {
            return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(
                    ApplicationError.notFound("customer", customerId.toString()));
        }

        var components = componentQueryService.handle(new GetAllComponentsByCustomerIdQuery(new CustomerId(customerId)));
        return ResponseEntity.ok(components.stream()
                .map(ComponentResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @GetMapping("/{componentId}")
    @Operation(summary = "Get a component by id")
    @ApiResponse(responseCode = "200", description = "Component found")
    @ApiResponse(responseCode = "404", description = "Component not found")
    public ResponseEntity<?> getComponentById(@PathVariable Long componentId) {
        var currentUser = currentUserProvider.current();
        var component = componentQueryService.handle(new GetComponentByIdQuery(componentId))
                .filter(existing -> canReadCustomer(existing.getCustomerIdValue(), currentUser));

        if (component.isEmpty()) {
            return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(
                    ApplicationError.notFound("component", componentId.toString()));
        }

        return ResponseEntity.ok(ComponentResourceFromEntityAssembler.toResourceFromEntity(component.get()));
    }

    private boolean canReadCustomer(Long customerId, CurrentUser currentUser) {
        return customerQueryService.handle(new GetCustomerByIdQuery(customerId))
                .filter(customer ->
                        (currentUser.isSupplier() && customer.isOwnedBy(currentUser.organizationId()))
                                || (currentUser.isAssetOwner() && customer.isLinkedTo(currentUser.organizationId())))
                .isPresent();
    }
}