package com.innovacorp.reliant.platform.reporting.interfaces.rest;

import com.innovacorp.reliant.platform.reporting.application.commandservices.GeneratedReportCommandService;
import com.innovacorp.reliant.platform.reporting.application.queryservices.GeneratedReportQueryService;
import com.innovacorp.reliant.platform.reporting.domain.model.queries.GetAllGeneratedReportsByOrganizationIdQuery;
import com.innovacorp.reliant.platform.reporting.domain.model.queries.GetGeneratedReportByIdQuery;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.ReportSubjectType;
import com.innovacorp.reliant.platform.reporting.interfaces.rest.resources.GeneratedReportResource;
import com.innovacorp.reliant.platform.reporting.interfaces.rest.resources.RegisterGeneratedReportResource;
import com.innovacorp.reliant.platform.reporting.interfaces.rest.transform.GeneratedReportResourceFromEntityAssembler;
import com.innovacorp.reliant.platform.reporting.interfaces.rest.transform.RegisterGeneratedReportCommandFromResourceAssembler;
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
 * Registry of the reports rendered from templates by the authenticated organization.
 */
@RestController
@RequestMapping(value = "/api/v1/generated-reports", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Generated Reports", description = "Registry of generated report endpoints")
public class GeneratedReportsController {
    private final GeneratedReportCommandService commandService;
    private final GeneratedReportQueryService queryService;
    private final CurrentUserProvider currentUserProvider;

    public GeneratedReportsController(GeneratedReportCommandService commandService, GeneratedReportQueryService queryService, CurrentUserProvider currentUserProvider) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping
    @Operation(summary = "Get the generated reports of the authenticated organization", description = "Optional filters by subjectType (spray_session | recuperation) and subjectId.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reports retrieved", content = @Content(schema = @Schema(implementation = GeneratedReportResource.class)))
    })
    public ResponseEntity<List<GeneratedReportResource>> getAllGeneratedReports(@RequestParam(required = false) String subjectType,
                                                                                @RequestParam(required = false) Long subjectId,
                                                                                @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var organizationId = currentUserProvider.current().organizationId();
        ReportSubjectType type = subjectType == null || subjectType.isBlank() ? null : ReportSubjectType.fromString(subjectType);
        var reports = queryService.handle(new GetAllGeneratedReportsByOrganizationIdQuery(new OrganizationId(organizationId), type, subjectId));
        return ResponseEntity.ok(reports.stream().map(GeneratedReportResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @GetMapping("/{generatedReportId}")
    @Operation(summary = "Get a generated report by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report found", content = @Content(schema = @Schema(implementation = GeneratedReportResource.class))),
            @ApiResponse(responseCode = "404", description = "Report not found")
    })
    public ResponseEntity<?> getGeneratedReportById(@PathVariable Long generatedReportId,
                                                    @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var organizationId = currentUserProvider.current().organizationId();
        var report = queryService.handle(new GetGeneratedReportByIdQuery(generatedReportId)).filter(r -> r.isOwnedBy(organizationId));
        if (report.isEmpty()) {
            return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(ApplicationError.notFound("generated-report", generatedReportId.toString()));
        }
        return ResponseEntity.ok(GeneratedReportResourceFromEntityAssembler.toResourceFromEntity(report.get()));
    }

    @PostMapping
    @Operation(summary = "Register a generated report", description = "The web application renders the PDF from the template and registers it here.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Report registered", content = @Content(schema = @Schema(implementation = GeneratedReportResource.class))),
            @ApiResponse(responseCode = "404", description = "Template not found"),
            @ApiResponse(responseCode = "422", description = "Template archived")
    })
    public ResponseEntity<?> registerGeneratedReport(@Valid @RequestBody RegisterGeneratedReportResource resource,
                                                     @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var result = commandService.handle(RegisterGeneratedReportCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, GeneratedReportResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }
}
