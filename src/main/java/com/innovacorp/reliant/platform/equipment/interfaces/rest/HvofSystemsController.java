package com.innovacorp.reliant.platform.equipment.interfaces.rest;

import com.innovacorp.reliant.platform.equipment.application.commandservices.HvofSystemCommandService;
import com.innovacorp.reliant.platform.equipment.application.queryservices.HvofSystemQueryService;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetAllHvofSystemsByOrganizationIdQuery;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetHvofSystemByIdQuery;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.HvofSystemResource;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.RegisterHvofSystemResource;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.transform.RegisterHvofSystemCommandFromResourceAssembler;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.transform.ResponseEntityFromHvofSystemCommandResultAssembler;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.transform.ResponseEntityFromHvofSystemQueryResultAssembler;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/hvof-systems", produces = APPLICATION_JSON_VALUE)
@Tag(name = "HVOF Systems", description = "Endpoints for HVOF systems")
public class HvofSystemsController {
    private final HvofSystemCommandService hvofSystemCommandService;
    private final MessageSource messageSource;
    private final HvofSystemQueryService hvofSystemQueryService;

    public HvofSystemsController(HvofSystemCommandService hvofSystemCommandService, MessageSource messageSource, HvofSystemQueryService hvofSystemQueryService) {
        this.hvofSystemCommandService = hvofSystemCommandService;
        this.messageSource = messageSource;
        this.hvofSystemQueryService = hvofSystemQueryService;
    }

    @Operation(summary = "Register an HVOF system", description = "Registers an HVOF system for an organization; the code must be unique within the organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "HVOF system registered", content = @Content(schema = @Schema(implementation = HvofSystemResource.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Conflict - code already registered for the organization", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<?> registerHvofSystem(@Valid @RequestBody RegisterHvofSystemResource resource) {
        log.debug("POST /api/v1/hvof-systems – organizationId={}, code={}", resource.organizationId(), resource.code());
        var result = hvofSystemCommandService.handle(RegisterHvofSystemCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityFromHvofSystemCommandResultAssembler.toResponseEntityFromResult(result, messageSource);
    }


    @Operation(summary = "Get an HVOF system by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "HVOF system found", content = @Content(schema = @Schema(implementation = HvofSystemResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid id", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "HVOF system not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("{id}")
    public ResponseEntity<?> getHvofSystemById(@Parameter(description = "HVOF system id", example = "1") @PathVariable Long id) {
        var result = hvofSystemQueryService.handle(new GetHvofSystemByIdQuery(id));
        return ResponseEntityFromHvofSystemQueryResultAssembler.toResponseEntityFromOptional(result, id, messageSource);
    }

    @Operation(summary = "List the HVOF systems of an organization")
    @ApiResponse(responseCode = "200", description = "HVOF systems of the organization (empty list if none)")
    @GetMapping
    public ResponseEntity<List<HvofSystemResource>> getHvofSystemsByOrganizationId(@Parameter(description = "Organization id", example = "1") @RequestParam Long organizationId) {
        var systems = hvofSystemQueryService.handle(new GetAllHvofSystemsByOrganizationIdQuery(new OrganizationId(organizationId)));
        return ResponseEntityFromHvofSystemQueryResultAssembler.toResponseEntityFromList(systems);
    }
}