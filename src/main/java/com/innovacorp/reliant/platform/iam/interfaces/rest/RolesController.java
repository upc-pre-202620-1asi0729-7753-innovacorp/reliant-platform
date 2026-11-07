package com.innovacorp.reliant.platform.iam.interfaces.rest;

import com.innovacorp.reliant.platform.iam.application.queryservices.RoleQueryService;
import com.innovacorp.reliant.platform.iam.domain.model.queries.GetAllRolesQuery;
import com.innovacorp.reliant.platform.iam.interfaces.rest.resources.RoleResource;
import com.innovacorp.reliant.platform.iam.interfaces.rest.transform.RoleResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that exposes available IAM roles.
 */
@RestController
@RequestMapping(value = "/api/v1/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Roles", description = "Available roles")
public class RolesController {

    private final RoleQueryService roleQueryService;

    public RolesController(RoleQueryService roleQueryService) {
        this.roleQueryService = roleQueryService;
    }

    @GetMapping
    @Operation(summary = "Get available roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles retrieved",
                    content = @Content(schema = @Schema(implementation = RoleResource.class)))
    })
    public ResponseEntity<List<RoleResource>> getAllRoles(
            @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var roles = roleQueryService.handle(new GetAllRolesQuery());
        return ResponseEntity.ok(roles.stream()
                .map(RoleResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }
}