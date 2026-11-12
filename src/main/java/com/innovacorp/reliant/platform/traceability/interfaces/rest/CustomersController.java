package com.innovacorp.reliant.platform.traceability.interfaces.rest;

import com.innovacorp.reliant.platform.shared.application.context.CurrentUserProvider;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.interfaces.rest.transform.ProblemDetailFromApplicationErrorAssembler;
import com.innovacorp.reliant.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.innovacorp.reliant.platform.traceability.application.commandservices.CustomerCommandService;
import com.innovacorp.reliant.platform.traceability.application.queryservices.CustomerQueryService;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetAllCustomersByLinkedAssetOwnerOrganizationIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetAllCustomersBySupplierOrganizationIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetCustomerByIdQuery;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.CustomerResource;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.RegisterCustomerResource;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.UpdateCustomerResource;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.transform.CustomerResourceFromEntityAssembler;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.transform.RegisterCustomerCommandFromResourceAssembler;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.transform.UpdateCustomerCommandFromResourceAssembler;
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

/**
 * REST controller for customers. The tenant is the authenticated organization.
 */
@RestController
@RequestMapping(value = "/api/v1/customers", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Customers", description = "Customer management endpoints")
public class CustomersController {
    private final CustomerCommandService customerCommandService;
    private final CustomerQueryService customerQueryService;
    private final CurrentUserProvider currentUserProvider;

    public CustomersController(CustomerCommandService customerCommandService, CustomerQueryService customerQueryService, CurrentUserProvider currentUserProvider) {
        this.customerCommandService = customerCommandService;
        this.customerQueryService = customerQueryService;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping
    @Operation(summary = "Register a customer", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer registered", content = @Content(schema = @Schema(implementation = CustomerResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Only suppliers can register customers"),
            @ApiResponse(responseCode = "409", description = "RUC already registered for the organization")
    })
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody RegisterCustomerResource resource,
                                              @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var result = customerCommandService.handle(RegisterCustomerCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, CustomerResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get the customers visible to the authenticated organization", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers retrieved", content = @Content(schema = @Schema(implementation = CustomerResource.class)))
    })
    public ResponseEntity<List<CustomerResource>> getAllCustomers(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var currentUser = currentUserProvider.current();
        var customers = currentUser.isAssetOwner()
                ? customerQueryService.handle(new GetAllCustomersByLinkedAssetOwnerOrganizationIdQuery(currentUser.organizationId()))
                : customerQueryService.handle(new GetAllCustomersBySupplierOrganizationIdQuery(new OrganizationId(currentUser.organizationId())));
        return ResponseEntity.ok(customers.stream().map(CustomerResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @GetMapping("/{customerId}")
    @Operation(summary = "Get a customer by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found", content = @Content(schema = @Schema(implementation = CustomerResource.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<?> getCustomerById(@PathVariable Long customerId,
                                             @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var currentUser = currentUserProvider.current();
        var customer = customerQueryService.handle(new GetCustomerByIdQuery(customerId))
                .filter(c -> c.isOwnedBy(currentUser.organizationId()) || c.isLinkedTo(currentUser.organizationId()));
        if (customer.isEmpty()) {
            return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(ApplicationError.notFound("customer", customerId.toString()));
        }
        return ResponseEntity.ok(CustomerResourceFromEntityAssembler.toResourceFromEntity(customer.get()));
    }

    @PutMapping("/{customerId}")
    @Operation(summary = "Update a customer", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated", content = @Content(schema = @Schema(implementation = CustomerResource.class))),
            @ApiResponse(responseCode = "403", description = "Not the owning supplier"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<?> updateCustomer(@PathVariable Long customerId, @Valid @RequestBody UpdateCustomerResource resource,
                                            @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var result = customerCommandService.handle(UpdateCustomerCommandFromResourceAssembler.toCommandFromResource(customerId, resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, CustomerResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
