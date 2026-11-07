package com.innovacorp.reliant.platform.equipment.interfaces.rest;

import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetAllHvofSubsystemsByHvofSystemIdQuery;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetAllControllersByHvofSystemIdQuery;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.HvofSubsystemResource;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.RegisterHvofSubsystemResource;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.ControllerResource;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.RegisterControllerResource;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.transform.HvofSubsystemResourceFromEntityAssembler;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.transform.RegisterHvofSubsystemCommandFromResourceAssembler;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.transform.ControllerResourceFromEntityAssembler;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.transform.RegisterControllerCommandFromResourceAssembler;

import com.innovacorp.reliant.platform.equipment.application.commandservices.HvofSystemCommandService;
import com.innovacorp.reliant.platform.equipment.application.queryservices.HvofSystemQueryService;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetAllHvofSystemsByOrganizationIdQuery;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetHvofSystemByIdQuery;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.HvofSystemResource;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.RegisterHvofSystemResource;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.transform.HvofSystemResourceFromEntityAssembler;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.transform.RegisterHvofSystemCommandFromResourceAssembler;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.interfaces.rest.transform.ProblemDetailFromApplicationErrorAssembler;
import com.innovacorp.reliant.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that exposes HVOF system resources.
 */
@RestController
@RequestMapping(value = "/api/v1/hvof-systems", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "HVOF Systems", description = "HVOF system management endpoints")
public class HvofSystemsController {
    private final HvofSystemCommandService hvofSystemCommandService;
    private final HvofSystemQueryService hvofSystemQueryService;

    public HvofSystemsController(HvofSystemCommandService hvofSystemCommandService, HvofSystemQueryService hvofSystemQueryService) {
        this.hvofSystemCommandService = hvofSystemCommandService;
        this.hvofSystemQueryService = hvofSystemQueryService;
    }

    @PostMapping
    @Operation(
            summary = "Register an HVOF system",
            description = "Registers a thermal spray system owned by a recuperation supplier organization."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "HVOF system registered", content = @Content(schema = @Schema(implementation = HvofSystemResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Conflict - code already used in the organization")
    })
    public ResponseEntity<?> registerHvofSystem(@Valid @RequestBody RegisterHvofSystemResource resource,
                                                @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var command = RegisterHvofSystemCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = hvofSystemCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                HvofSystemResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{hvofSystemId}")
    @Operation(summary = "Get an HVOF system by id", description = "Retrieves an HVOF system by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "HVOF system found", content = @Content(schema = @Schema(implementation = HvofSystemResource.class))),
            @ApiResponse(responseCode = "404", description = "HVOF system not found")
    })
    public ResponseEntity<?> getHvofSystemById(@PathVariable Long hvofSystemId,
                                               @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var hvofSystem = hvofSystemQueryService.handle(new GetHvofSystemByIdQuery(hvofSystemId));
        if (hvofSystem.isEmpty()) {
            var error = ApplicationError.notFound("hvof-system", hvofSystemId.toString());
            return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(error);
        }
        return ResponseEntity.ok(HvofSystemResourceFromEntityAssembler.toResourceFromEntity(hvofSystem.get()));
    }

    @GetMapping
    @Operation(summary = "Get all HVOF systems of an organization", description = "Returns an empty array when the organization has no systems.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "HVOF systems retrieved", content = @Content(schema = @Schema(implementation = HvofSystemResource.class)))
    })
    public ResponseEntity<List<HvofSystemResource>> getAllHvofSystemsByOrganizationId(@RequestParam Long organizationId,
                                                                                      @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var systems = hvofSystemQueryService.handle(new GetAllHvofSystemsByOrganizationIdQuery(new OrganizationId(organizationId)));
        return ResponseEntity.ok(systems.stream().map(HvofSystemResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @PostMapping("/{hvofSystemId}/subsystems")
    @Operation(summary = "Register a subsystem in an HVOF system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subsystem registered", content = @Content(schema = @Schema(implementation = HvofSubsystemResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "HVOF system not found"),
            @ApiResponse(responseCode = "409", description = "Subsystem name already used in the system")
    })
    public ResponseEntity<?> registerSubsystem(@PathVariable Long hvofSystemId,
                                               @Valid @RequestBody RegisterHvofSubsystemResource resource,
                                               @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var command = RegisterHvofSubsystemCommandFromResourceAssembler.toCommandFromResource(hvofSystemId, resource);
        var result = hvofSystemCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                s -> HvofSubsystemResourceFromEntityAssembler.toResourceFromEntity(s, hvofSystemId),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{hvofSystemId}/subsystems")
    @Operation(summary = "Get the subsystems of an HVOF system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subsystems retrieved", content = @Content(schema = @Schema(implementation = HvofSubsystemResource.class))),
            @ApiResponse(responseCode = "404", description = "HVOF system not found")
    })
    public ResponseEntity<?> getSubsystems(@PathVariable Long hvofSystemId,
                                           @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        if (hvofSystemQueryService.handle(new GetHvofSystemByIdQuery(hvofSystemId)).isEmpty()) {
            return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(
                    ApplicationError.notFound("hvof-system", hvofSystemId.toString())
            );
        }

        var subsystems = hvofSystemQueryService.handle(
                new GetAllHvofSubsystemsByHvofSystemIdQuery(hvofSystemId)
        );

        return ResponseEntity.ok(
                subsystems.stream()
                        .map(s -> HvofSubsystemResourceFromEntityAssembler.toResourceFromEntity(s, hvofSystemId))
                        .toList()
        );
    }

    @PostMapping("/{hvofSystemId}/controllers")
    @Operation(summary = "Register a controller in an HVOF system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Controller registered", content = @Content(schema = @Schema(implementation = ControllerResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "HVOF system not found"),
            @ApiResponse(responseCode = "409", description = "Controller number already used in the system")
    })
    public ResponseEntity<?> registerController(@PathVariable Long hvofSystemId,
                                                @Valid @RequestBody RegisterControllerResource resource,
                                                @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var command = RegisterControllerCommandFromResourceAssembler.toCommandFromResource(hvofSystemId, resource);
        var result = hvofSystemCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                c -> ControllerResourceFromEntityAssembler.toResourceFromEntity(c, hvofSystemId),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{hvofSystemId}/controllers")
    @Operation(summary = "Get the controllers of an HVOF system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Controllers retrieved", content = @Content(schema = @Schema(implementation = ControllerResource.class))),
            @ApiResponse(responseCode = "404", description = "HVOF system not found")
    })
    public ResponseEntity<?> getControllers(@PathVariable Long hvofSystemId,
                                            @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        if (hvofSystemQueryService.handle(new GetHvofSystemByIdQuery(hvofSystemId)).isEmpty()) {
            return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(
                    ApplicationError.notFound("hvof-system", hvofSystemId.toString())
            );
        }

        var controllers = hvofSystemQueryService.handle(
                new GetAllControllersByHvofSystemIdQuery(hvofSystemId)
        );

        return ResponseEntity.ok(
                controllers.stream()
                        .map(c -> ControllerResourceFromEntityAssembler.toResourceFromEntity(c, hvofSystemId))
                        .toList()
        );
    }}