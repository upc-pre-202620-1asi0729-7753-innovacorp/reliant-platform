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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/spray-sessions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Spray Sessions", description = "Spray session monitoring endpoints")
@SecurityRequirement(name = "bearerAuth")
public class SpraySessionsController {

    private final SpraySessionCommandService commandService;
    private final SpraySessionQueryService queryService;
    private final CurrentUserProvider currentUserProvider;
    private final Validator validator;

    public SpraySessionsController(SpraySessionCommandService commandService,
                                   SpraySessionQueryService queryService,
                                   CurrentUserProvider currentUserProvider,
                                   Validator validator) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.currentUserProvider = currentUserProvider;
        this.validator = validator;
    }

    @PostMapping
    @Operation(summary = "Start a spray session",
            description = "Copies the active recipe bands and links the session to its recuperation.")
    @ApiResponse(responseCode = "201", description = "Session started")
    @ApiResponse(responseCode = "403", description = "HVOF system belongs to another organization")
    @ApiResponse(responseCode = "404", description = "HVOF system or recuperation not found")
    @ApiResponse(responseCode = "422", description = "Recipe or recuperation prevents starting the session")
    public ResponseEntity<?> startSpraySession(@Valid @RequestBody StartSpraySessionResource resource) {
        var command = StartSpraySessionCommandFromResourceAssembler.toCommandFromResource(resource);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.handle(command),
                SpraySessionResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get the spray sessions of the authenticated organization")
    @ApiResponse(responseCode = "200", description = "Sessions retrieved")
    @ApiResponse(responseCode = "400", description = "Invalid recuperation ID")
    public ResponseEntity<?> getAllSpraySessions(
            @RequestParam(name = "recuperationId", required = false) Long recuperationId) {
        if (recuperationId != null && recuperationId <= 0) {
            return badRequest("recuperationId", "Recuperation id must be a positive number");
        }

        var organizationId = currentUserProvider.current().organizationId();
        var sessions = recuperationId == null
                ? queryService.handle(
                new GetAllSpraySessionsByOrganizationIdQuery(new OrganizationId(organizationId)))
                : queryService.handle(
                        new GetAllSpraySessionsByRecuperationIdQuery(new RecuperationId(recuperationId)))
                .stream()
                .filter(session -> session.getOrganizationIdValue().equals(organizationId))
                .toList();

        return ResponseEntity.ok(sessions.stream()
                .map(SpraySessionResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @GetMapping("/{spraySessionId}")
    @Operation(summary = "Get a spray session by id")
    @ApiResponse(responseCode = "200", description = "Session found")
    @ApiResponse(responseCode = "404", description = "Session not found")
    public ResponseEntity<?> getSpraySessionById(@PathVariable Long spraySessionId) {
        var session = ownedSession(spraySessionId);
        if (session.isEmpty()) {
            return notFound(spraySessionId);
        }

        return ResponseEntity.ok(SpraySessionResourceFromEntityAssembler.toResourceFromEntity(session.get()));
    }

    @PatchMapping("/{spraySessionId}/completion")
    @Operation(summary = "Complete a spray session")
    @ApiResponse(responseCode = "200", description = "Session completed")
    @ApiResponse(responseCode = "404", description = "Session not found")
    @ApiResponse(responseCode = "422", description = "Session not active")
    public ResponseEntity<?> completeSpraySession(@PathVariable Long spraySessionId) {
        var result = commandService.handle(new CompleteSpraySessionCommand(spraySessionId));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, SpraySessionResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PatchMapping("/{spraySessionId}/abortion")
    @Operation(summary = "Abort a spray session")
    @ApiResponse(responseCode = "200", description = "Session aborted")
    @ApiResponse(responseCode = "404", description = "Session not found")
    @ApiResponse(responseCode = "422", description = "Session not active")
    public ResponseEntity<?> abortSpraySession(@PathVariable Long spraySessionId,
                                               @Valid @RequestBody AbortSpraySessionResource resource) {
        var result = commandService.handle(new AbortSpraySessionCommand(spraySessionId, resource.reason()));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, SpraySessionResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PostMapping("/{spraySessionId}/readings")
    @Operation(summary = "Ingest process readings",
            description = "Accepts a JSON array and classifies each reading against the session's recipe bands.")
    @ApiResponse(responseCode = "201", description = "Readings ingested")
    @ApiResponse(responseCode = "400", description = "Invalid reading batch")
    @ApiResponse(responseCode = "404", description = "Session not found")
    @ApiResponse(responseCode = "422", description = "Session not active")
    public ResponseEntity<?> ingestReadings(@PathVariable Long spraySessionId,
                                            @RequestBody List<IngestProcessReadingResource> resources) {
        if (resources.isEmpty()) {
            return badRequest("readings", "At least one reading is required");
        }

        for (var resource : resources) {
            if (resource == null) {
                return badRequest("readings", "Each reading must be an object");
            }
            var violations = validator.validate(resource);
            if (!violations.isEmpty()) {
                var violation = violations.iterator().next();
                return badRequest(violation.getPropertyPath().toString(), violation.getMessage());
            }
        }

        var command = IngestProcessReadingsCommandFromResourceAssembler
                .toCommandFromResources(spraySessionId, resources);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.handle(command),
                readings -> readings.stream()
                        .map(ProcessReadingResourceFromEntityAssembler::toResourceFromEntity)
                        .toList(),
                HttpStatus.CREATED);
    }

    @GetMapping("/{spraySessionId}/readings")
    @Operation(summary = "Get the readings of a session",
            description = "Optionally filters by nominal, out_of_nominal, warning or shutdown.")
    @ApiResponse(responseCode = "200", description = "Readings retrieved")
    @ApiResponse(responseCode = "400", description = "Invalid band")
    @ApiResponse(responseCode = "404", description = "Session not found")
    public ResponseEntity<?> getReadings(@PathVariable Long spraySessionId,
                                         @RequestParam(name = "band", required = false) String band) {
        if (ownedSession(spraySessionId).isEmpty()) {
            return notFound(spraySessionId);
        }

        Band filter;
        try {
            filter = band == null ? null : Band.fromString(band);
        } catch (IllegalArgumentException e) {
            return badRequest("band", e.getMessage());
        }

        var readings = queryService.handle(new GetReadingsBySpraySessionIdQuery(spraySessionId, filter));
        return ResponseEntity.ok(readings.stream()
                .map(ProcessReadingResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @GetMapping("/{spraySessionId}/deviations")
    @Operation(summary = "Count warning and shutdown readings of a session")
    @ApiResponse(responseCode = "200", description = "Counts retrieved")
    @ApiResponse(responseCode = "404", description = "Session not found")
    public ResponseEntity<?> getDeviations(@PathVariable Long spraySessionId) {
        if (ownedSession(spraySessionId).isEmpty()) {
            return notFound(spraySessionId);
        }

        var counts = queryService.handle(new GetDeviationCountsBySpraySessionIdQuery(spraySessionId));
        return ResponseEntity.ok(new DeviationCountsResource(counts.get("warning"), counts.get("shutdown")));
    }

    @PostMapping("/{spraySessionId}/passes")
    @Operation(summary = "Register a spray pass")
    @ApiResponse(responseCode = "201", description = "Pass registered")
    @ApiResponse(responseCode = "404", description = "Session not found")
    @ApiResponse(responseCode = "409", description = "Pass number already registered")
    @ApiResponse(responseCode = "422", description = "Session not active")
    public ResponseEntity<?> registerPass(@PathVariable Long spraySessionId,
                                          @Valid @RequestBody RegisterSprayPassResource resource) {
        var command = RegisterSprayPassCommandFromResourceAssembler
                .toCommandFromResource(spraySessionId, resource);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commandService.handle(command),
                pass -> SprayPassResourceFromEntityAssembler.toResourceFromEntity(pass, spraySessionId),
                HttpStatus.CREATED);
    }

    @GetMapping("/{spraySessionId}/passes")
    @Operation(summary = "Get the passes of a session")
    @ApiResponse(responseCode = "200", description = "Passes retrieved")
    @ApiResponse(responseCode = "404", description = "Session not found")
    public ResponseEntity<?> getPasses(@PathVariable Long spraySessionId) {
        var session = ownedSession(spraySessionId);
        if (session.isEmpty()) {
            return notFound(spraySessionId);
        }

        return ResponseEntity.ok(session.get().getPasses().stream()
                .map(pass -> SprayPassResourceFromEntityAssembler.toResourceFromEntity(pass, spraySessionId))
                .toList());
    }

    private Optional<SpraySession> ownedSession(Long spraySessionId) {
        var organizationId = currentUserProvider.current().organizationId();
        return queryService.handle(new GetSpraySessionByIdQuery(spraySessionId))
                .filter(session -> session.getOrganizationIdValue().equals(organizationId));
    }

    private ResponseEntity<?> notFound(Long spraySessionId) {
        return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(
                ApplicationError.notFound("spray-session", spraySessionId.toString()));
    }

    private ResponseEntity<?> badRequest(String field, String reason) {
        return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(
                ApplicationError.validationError(field, reason));
    }
}