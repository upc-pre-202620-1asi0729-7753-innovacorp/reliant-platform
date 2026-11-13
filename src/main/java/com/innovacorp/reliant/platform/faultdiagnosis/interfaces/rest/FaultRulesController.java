package com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest;

import com.innovacorp.reliant.platform.faultdiagnosis.application.commandservices.FaultRuleCommandService;
import com.innovacorp.reliant.platform.faultdiagnosis.application.queryservices.FaultRuleQueryService;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.SeedDefaultFaultRulesCommand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.queries.GetAllFaultRulesByOrganizationIdQuery;
import com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.resources.CreateFaultRuleResource;
import com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.resources.FaultRuleResource;
import com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.resources.UpdateFaultRuleResource;
import com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.transform.CreateFaultRuleCommandFromResourceAssembler;
import com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.transform.FaultRuleResourceFromEntityAssembler;
import com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.transform.UpdateFaultRuleCommandFromResourceAssembler;
import com.innovacorp.reliant.platform.shared.application.context.CurrentUserProvider;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
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
@RequestMapping(value = "/api/v1/fault-rules", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Fault Rules", description = "Cause-effect rule management endpoints")
public class FaultRulesController {
    private final FaultRuleCommandService commandService;
    private final FaultRuleQueryService queryService;
    private final CurrentUserProvider currentUserProvider;

    public FaultRulesController(FaultRuleCommandService commandService, FaultRuleQueryService queryService, CurrentUserProvider currentUserProvider) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping
    @Operation(summary = "Get the fault rules of the authenticated organization", description = "Seeds the default rules the first time.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rules retrieved", content = @Content(schema = @Schema(implementation = FaultRuleResource.class)))
    })
    public ResponseEntity<List<FaultRuleResource>> getAllFaultRules(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var organizationId = currentUserProvider.current().organizationId();
        commandService.handle(new SeedDefaultFaultRulesCommand(organizationId));
        var rules = queryService.handle(new GetAllFaultRulesByOrganizationIdQuery(new OrganizationId(organizationId)));
        return ResponseEntity.ok(rules.stream().map(FaultRuleResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @PostMapping
    @Operation(summary = "Create a fault rule", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rule created", content = @Content(schema = @Schema(implementation = FaultRuleResource.class))),
            @ApiResponse(responseCode = "403", description = "Administrator role required"),
            @ApiResponse(responseCode = "409", description = "Rule already exists for parameter and band")
    })
    public ResponseEntity<?> createFaultRule(@Valid @RequestBody CreateFaultRuleResource resource,
                                             @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var result = commandService.handle(CreateFaultRuleCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, FaultRuleResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{faultRuleId}")
    @Operation(summary = "Update a fault rule", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rule updated", content = @Content(schema = @Schema(implementation = FaultRuleResource.class))),
            @ApiResponse(responseCode = "403", description = "Administrator role required"),
            @ApiResponse(responseCode = "404", description = "Rule not found")
    })
    public ResponseEntity<?> updateFaultRule(@PathVariable Long faultRuleId, @Valid @RequestBody UpdateFaultRuleResource resource,
                                             @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var result = commandService.handle(UpdateFaultRuleCommandFromResourceAssembler.toCommandFromResource(faultRuleId, resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, FaultRuleResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
