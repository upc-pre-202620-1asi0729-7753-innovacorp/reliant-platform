package com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest;

import com.innovacorp.reliant.platform.faultdiagnosis.application.commandservices.FaultCaseCommandService;
import com.innovacorp.reliant.platform.faultdiagnosis.application.queryservices.FaultCaseQueryService;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.AcknowledgeFaultCaseCommand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.ResolveFaultCaseCommand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.queries.GetAllFaultCasesByOrganizationIdQuery;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.queries.GetFaultCaseByIdQuery;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.FaultCaseStatus;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.SpraySessionId;
import com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.resources.FaultCaseResource;
import com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.resources.ResolveFaultCaseResource;
import com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.transform.FaultCaseResourceFromEntityAssembler;
import com.innovacorp.reliant.platform.shared.application.context.CurrentUserProvider;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.interfaces.rest.transform.ProblemDetailFromApplicationErrorAssembler;
import com.innovacorp.reliant.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
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
@RequestMapping(value = "/api/v1/fault-cases", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Fault Cases", description = "Fault case diagnosis and follow-up endpoints")
public class FaultCasesController {
    private final FaultCaseCommandService commandService;
    private final FaultCaseQueryService queryService;
    private final CurrentUserProvider currentUserProvider;

    public FaultCasesController(FaultCaseCommandService commandService, FaultCaseQueryService queryService, CurrentUserProvider currentUserProvider) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping
    @Operation(summary = "Get the fault cases of the authenticated organization", description = "Optional filters by spray session and status (open, acknowledged, resolved).", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cases retrieved", content = @Content(schema = @Schema(implementation = FaultCaseResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status")
    })
    public ResponseEntity<?> getAllFaultCases(@RequestParam(required = false) Long spraySessionId,
                                              @RequestParam(required = false) String status,
                                              @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        try {
            var organizationId = new OrganizationId(currentUserProvider.current().organizationId());
            var sessionFilter = spraySessionId == null ? null : new SpraySessionId(spraySessionId);
            var statusFilter = status == null || status.isBlank() ? null : FaultCaseStatus.fromString(status);
            var cases = queryService.handle(new GetAllFaultCasesByOrganizationIdQuery(organizationId, sessionFilter, statusFilter));
            return ResponseEntity.ok(cases.stream().map(FaultCaseResourceFromEntityAssembler::toResourceFromEntity).toList());
        } catch (IllegalArgumentException e) {
            return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(ApplicationError.validationError("status", e.getMessage()));
        }
    }

    @GetMapping("/{faultCaseId}")
    @Operation(summary = "Get a fault case by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Case found", content = @Content(schema = @Schema(implementation = FaultCaseResource.class))),
            @ApiResponse(responseCode = "404", description = "Case not found")
    })
    public ResponseEntity<?> getFaultCaseById(@PathVariable Long faultCaseId,
                                              @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var faultCase = queryService.handle(new GetFaultCaseByIdQuery(faultCaseId));
        if (faultCase.isEmpty()) {
            return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(ApplicationError.notFound("fault-case", String.valueOf(faultCaseId)));
        }
        return ResponseEntity.ok(FaultCaseResourceFromEntityAssembler.toResourceFromEntity(faultCase.get()));
    }

    @PatchMapping("/{faultCaseId}/acknowledgement")
    @Operation(summary = "Acknowledge an open fault case", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Case acknowledged", content = @Content(schema = @Schema(implementation = FaultCaseResource.class))),
            @ApiResponse(responseCode = "404", description = "Case not found"),
            @ApiResponse(responseCode = "422", description = "Case is not open")
    })
    public ResponseEntity<?> acknowledgeFaultCase(@PathVariable Long faultCaseId,
                                                  @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var result = commandService.handle(new AcknowledgeFaultCaseCommand(faultCaseId));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, FaultCaseResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PatchMapping("/{faultCaseId}/resolution")
    @Operation(summary = "Resolve a fault case", description = "An open case is acknowledged and resolved in one step.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Case resolved", content = @Content(schema = @Schema(implementation = FaultCaseResource.class))),
            @ApiResponse(responseCode = "404", description = "Case not found"),
            @ApiResponse(responseCode = "422", description = "Case already resolved")
    })
    public ResponseEntity<?> resolveFaultCase(@PathVariable Long faultCaseId, @Valid @RequestBody(required = false) ResolveFaultCaseResource resource,
                                              @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var notes = resource == null ? null : resource.resolutionNotes();
        var result = commandService.handle(new ResolveFaultCaseCommand(faultCaseId, notes));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, FaultCaseResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
