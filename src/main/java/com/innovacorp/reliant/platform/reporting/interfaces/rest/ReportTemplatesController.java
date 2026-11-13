package com.innovacorp.reliant.platform.reporting.interfaces.rest;

import com.innovacorp.reliant.platform.reporting.application.commandservices.ReportTemplateCommandService;
import com.innovacorp.reliant.platform.reporting.application.queryservices.ReportTemplateQueryService;
import com.innovacorp.reliant.platform.reporting.domain.model.commands.ChangeReportTemplateStatusCommand;
import com.innovacorp.reliant.platform.reporting.domain.model.commands.SeedDefaultReportTemplatesCommand;
import com.innovacorp.reliant.platform.reporting.domain.model.queries.GetAllReportTemplatesByOrganizationIdQuery;
import com.innovacorp.reliant.platform.reporting.domain.model.queries.GetReportTemplateByIdQuery;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.ReportKind;
import com.innovacorp.reliant.platform.reporting.interfaces.rest.resources.ChangeReportTemplateStatusResource;
import com.innovacorp.reliant.platform.reporting.interfaces.rest.resources.CreateReportTemplateResource;
import com.innovacorp.reliant.platform.reporting.interfaces.rest.resources.ReportTemplateResource;
import com.innovacorp.reliant.platform.reporting.interfaces.rest.resources.UpdateReportTemplateResource;
import com.innovacorp.reliant.platform.reporting.interfaces.rest.transform.ReportTemplateCommandsFromResourceAssembler;
import com.innovacorp.reliant.platform.reporting.interfaces.rest.transform.ReportTemplateResourceFromEntityAssembler;
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

/**
 * Customizable report templates of the authenticated organization.
 */
@RestController
@RequestMapping(value = "/api/v1/report-templates", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Report Templates", description = "Customizable report template endpoints")
public class ReportTemplatesController {
    private final ReportTemplateCommandService commandService;
    private final ReportTemplateQueryService queryService;
    private final CurrentUserProvider currentUserProvider;

    public ReportTemplatesController(ReportTemplateCommandService commandService, ReportTemplateQueryService queryService, CurrentUserProvider currentUserProvider) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping
    @Operation(summary = "Get the report templates of the authenticated organization", description = "Seeds the standard templates the first time. Optional filter by reportKind.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Templates retrieved", content = @Content(schema = @Schema(implementation = ReportTemplateResource.class)))
    })
    public ResponseEntity<List<ReportTemplateResource>> getAllReportTemplates(@RequestParam(required = false) String reportKind,
                                                                              @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var currentUser = currentUserProvider.current();
        commandService.handle(new SeedDefaultReportTemplatesCommand(currentUser.organizationId(), currentUser.userId()));
        ReportKind kind = reportKind == null || reportKind.isBlank() ? null : ReportKind.fromString(reportKind);
        var templates = queryService.handle(new GetAllReportTemplatesByOrganizationIdQuery(new OrganizationId(currentUser.organizationId()), kind));
        return ResponseEntity.ok(templates.stream().map(ReportTemplateResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @GetMapping("/{reportTemplateId}")
    @Operation(summary = "Get a report template by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Template found", content = @Content(schema = @Schema(implementation = ReportTemplateResource.class))),
            @ApiResponse(responseCode = "404", description = "Template not found")
    })
    public ResponseEntity<?> getReportTemplateById(@PathVariable Long reportTemplateId,
                                                   @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var organizationId = currentUserProvider.current().organizationId();
        var template = queryService.handle(new GetReportTemplateByIdQuery(reportTemplateId)).filter(t -> t.isOwnedBy(organizationId));
        if (template.isEmpty()) {
            return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(ApplicationError.notFound("report-template", reportTemplateId.toString()));
        }
        return ResponseEntity.ok(ReportTemplateResourceFromEntityAssembler.toResourceFromEntity(template.get()));
    }

    @PostMapping
    @Operation(summary = "Create a report template", description = "Administrators and quality engineers.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Template created", content = @Content(schema = @Schema(implementation = ReportTemplateResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid layout"),
            @ApiResponse(responseCode = "403", description = "Role required"),
            @ApiResponse(responseCode = "409", description = "A template with that name already exists")
    })
    public ResponseEntity<?> createReportTemplate(@Valid @RequestBody CreateReportTemplateResource resource,
                                                  @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var result = commandService.handle(ReportTemplateCommandsFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, ReportTemplateResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{reportTemplateId}")
    @Operation(summary = "Update a report template", description = "Replaces the whole layout. Archived templates cannot be edited.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Template updated", content = @Content(schema = @Schema(implementation = ReportTemplateResource.class))),
            @ApiResponse(responseCode = "403", description = "Role required"),
            @ApiResponse(responseCode = "404", description = "Template not found"),
            @ApiResponse(responseCode = "422", description = "Template archived")
    })
    public ResponseEntity<?> updateReportTemplate(@PathVariable Long reportTemplateId, @Valid @RequestBody UpdateReportTemplateResource resource,
                                                  @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var result = commandService.handle(ReportTemplateCommandsFromResourceAssembler.toCommandFromResource(reportTemplateId, resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, ReportTemplateResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PatchMapping("/{reportTemplateId}/status")
    @Operation(summary = "Archive or reactivate a report template", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status changed", content = @Content(schema = @Schema(implementation = ReportTemplateResource.class))),
            @ApiResponse(responseCode = "403", description = "Role required"),
            @ApiResponse(responseCode = "404", description = "Template not found"),
            @ApiResponse(responseCode = "422", description = "Already in that status")
    })
    public ResponseEntity<?> changeReportTemplateStatus(@PathVariable Long reportTemplateId, @Valid @RequestBody ChangeReportTemplateStatusResource resource,
                                                        @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var result = commandService.handle(new ChangeReportTemplateStatusCommand(reportTemplateId, resource.status()));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, ReportTemplateResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
