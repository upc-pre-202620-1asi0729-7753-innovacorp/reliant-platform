package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest;

import com.innovacorp.reliant.platform.processmonitoring.application.commandservices.SpraySessionCommandService;
import com.innovacorp.reliant.platform.processmonitoring.application.queryservices.SpraySessionQueryService;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.aggregates.SpraySession;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.commands.AbortSpraySessionCommand;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.commands.CompleteSpraySessionCommand;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.queries.*;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.Band;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.RecuperationId;
import com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources.*;
import com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.transform.*;
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
import java.util.Optional;

/**
 * REST controller for spray sessions, their readings and passes.
 */
@RestController
@RequestMapping(value = "/api/v1/spray-sessions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Spray Sessions", description = "Spray session monitoring endpoints")
public class SpraySessionsController {
    private final SpraySessionCommandService commandService;
    private final SpraySessionQueryService queryService;
    private final CurrentUserProvider currentUserProvider;

    public SpraySessionsController(SpraySessionCommandService commandService, SpraySessionQueryService queryService, CurrentUserProvider currentUserProvider) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping
    @Operation(summary = "Start a spray session", description = "Freezes the bands of the ACTIVE recipe and links the session to its recuperation.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Session started", content = @Content(schema = @Schema(implementation = SpraySessionResource.class))),
            @ApiResponse(responseCode = "403", description = "HVOF system of another organization"),
            @ApiResponse(responseCode = "404", description = "HVOF system or recuperation not found"),
            @ApiResponse(responseCode = "422", description = "Recipe not active")
    })
    public ResponseEntity<?> startSpraySession(@Valid @RequestBody StartSpraySessionResource resource,
                                               @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var result = commandService.handle(StartSpraySessionCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, SpraySessionResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get the spray sessions of the authenticated organization", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessions retrieved", content = @Content(schema = @Schema(implementation = SpraySessionResource.class)))
    })
    public ResponseEntity<List<SpraySessionResource>> getAllSpraySessions(@RequestParam(required = false) Long recuperationId,
                                                                          @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var organizationId = currentUserProvider.current().organizationId();
        var sessions = recuperationId == null
                ? queryService.handle(new GetAllSpraySessionsByOrganizationIdQuery(new OrganizationId(organizationId)))
                : queryService.handle(new GetAllSpraySessionsByRecuperationIdQuery(new RecuperationId(recuperationId))).stream()
                        .filter(s -> s.getOrganizationIdValue().equals(organizationId)).toList();
        return ResponseEntity.ok(sessions.stream().map(SpraySessionResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @GetMapping("/{spraySessionId}")
    @Operation(summary = "Get a spray session by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session found", content = @Content(schema = @Schema(implementation = SpraySessionResource.class))),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    public ResponseEntity<?> getSpraySessionById(@PathVariable Long spraySessionId,
                                                 @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var session = ownedSession(spraySessionId);
        if (session.isEmpty()) return notFound(spraySessionId);
        return ResponseEntity.ok(SpraySessionResourceFromEntityAssembler.toResourceFromEntity(session.get()));
    }

    @PatchMapping("/{spraySessionId}/completion")
    @Operation(summary = "Complete a spray session", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session completed", content = @Content(schema = @Schema(implementation = SpraySessionResource.class))),
            @ApiResponse(responseCode = "404", description = "Session not found"),
            @ApiResponse(responseCode = "422", description = "Session not active")
    })
    public ResponseEntity<?> completeSpraySession(@PathVariable Long spraySessionId,
                                                  @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var result = commandService.handle(new CompleteSpraySessionCommand(spraySessionId));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, SpraySessionResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PatchMapping("/{spraySessionId}/abortion")
    @Operation(summary = "Abort a spray session", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session aborted", content = @Content(schema = @Schema(implementation = SpraySessionResource.class))),
            @ApiResponse(responseCode = "404", description = "Session not found"),
            @ApiResponse(responseCode = "422", description = "Session not active")
    })
    public ResponseEntity<?> abortSpraySession(@PathVariable Long spraySessionId, @Valid @RequestBody AbortSpraySessionResource resource,
                                               @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var result = commandService.handle(new AbortSpraySessionCommand(spraySessionId, resource.reason()));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, SpraySessionResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PostMapping("/{spraySessionId}/readings")
    @Operation(summary = "Ingest process readings", description = "Accepts a JSON array; each reading is classified against the frozen recipe bands.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Readings ingested", content = @Content(schema = @Schema(implementation = ProcessReadingResource.class))),
            @ApiResponse(responseCode = "404", description = "Session not found"),
            @ApiResponse(responseCode = "422", description = "Session not active")
    })
    public ResponseEntity<?> ingestReadings(@PathVariable Long spraySessionId, @RequestBody List<@Valid IngestProcessReadingResource> resources,
                                            @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var result = commandService.handle(IngestProcessReadingsCommandFromResourceAssembler.toCommandFromResources(spraySessionId, resources));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                readings -> readings.stream().map(ProcessReadingResourceFromEntityAssembler::toResourceFromEntity).toList(), HttpStatus.CREATED);
    }

    @GetMapping("/{spraySessionId}/readings")
    @Operation(summary = "Get the readings of a session", description = "Optional ?band=nominal|out_of_nominal|warning|shutdown", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Readings retrieved", content = @Content(schema = @Schema(implementation = ProcessReadingResource.class))),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    public ResponseEntity<?> getReadings(@PathVariable Long spraySessionId, @RequestParam(required = false) String band,
                                         @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        if (ownedSession(spraySessionId).isEmpty()) return notFound(spraySessionId);
        var readings = queryService.handle(new GetReadingsBySpraySessionIdQuery(spraySessionId, band == null ? null : Band.fromString(band)));
        return ResponseEntity.ok(readings.stream().map(ProcessReadingResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @GetMapping("/{spraySessionId}/deviations")
    @Operation(summary = "Count warning and shutdown readings of a session", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Counts retrieved", content = @Content(schema = @Schema(implementation = DeviationCountsResource.class))),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    public ResponseEntity<?> getDeviations(@PathVariable Long spraySessionId,
                                           @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        if (ownedSession(spraySessionId).isEmpty()) return notFound(spraySessionId);
        var counts = queryService.handle(new GetDeviationCountsBySpraySessionIdQuery(spraySessionId));
        return ResponseEntity.ok(new DeviationCountsResource(counts.get("warning"), counts.get("shutdown")));
    }

    @PostMapping("/{spraySessionId}/passes")
    @Operation(summary = "Register a spray pass", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pass registered", content = @Content(schema = @Schema(implementation = SprayPassResource.class))),
            @ApiResponse(responseCode = "404", description = "Session not found"),
            @ApiResponse(responseCode = "409", description = "Pass number already registered"),
            @ApiResponse(responseCode = "422", description = "Session not active")
    })
    public ResponseEntity<?> registerPass(@PathVariable Long spraySessionId, @Valid @RequestBody RegisterSprayPassResource resource,
                                          @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var result = commandService.handle(RegisterSprayPassCommandFromResourceAssembler.toCommandFromResource(spraySessionId, resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, p -> SprayPassResourceFromEntityAssembler.toResourceFromEntity(p, spraySessionId), HttpStatus.CREATED);
    }

    @GetMapping("/{spraySessionId}/passes")
    @Operation(summary = "Get the passes of a session", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passes retrieved", content = @Content(schema = @Schema(implementation = SprayPassResource.class))),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    public ResponseEntity<?> getPasses(@PathVariable Long spraySessionId,
                                       @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var session = ownedSession(spraySessionId);
        if (session.isEmpty()) return notFound(spraySessionId);
        return ResponseEntity.ok(session.get().getPasses().stream().map(p -> SprayPassResourceFromEntityAssembler.toResourceFromEntity(p, spraySessionId)).toList());
    }

    private Optional<SpraySession> ownedSession(Long spraySessionId) {
        var organizationId = currentUserProvider.current().organizationId();
        return queryService.handle(new GetSpraySessionByIdQuery(spraySessionId)).filter(s -> s.getOrganizationIdValue().equals(organizationId));
    }

    private ResponseEntity<?> notFound(Long spraySessionId) {
        return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(ApplicationError.notFound("spray-session", spraySessionId.toString()));
    }
}
