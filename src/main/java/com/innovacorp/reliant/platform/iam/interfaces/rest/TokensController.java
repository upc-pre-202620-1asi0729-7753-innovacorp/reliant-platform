package com.innovacorp.reliant.platform.iam.interfaces.rest;

import com.innovacorp.reliant.platform.iam.application.commandservices.UserCommandService;
import com.innovacorp.reliant.platform.iam.application.queryservices.OrganizationQueryService;
import com.innovacorp.reliant.platform.iam.domain.model.queries.GetOrganizationByIdQuery;
import com.innovacorp.reliant.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.innovacorp.reliant.platform.iam.interfaces.rest.resources.SignInResource;
import com.innovacorp.reliant.platform.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.innovacorp.reliant.platform.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
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

/**
 * REST controller for authentication tokens. POST is the sign-in.
 */
@RestController
@RequestMapping(value = "/api/v1/tokens", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Tokens", description = "Authentication and security token management endpoints")
public class TokensController {
    private final UserCommandService userCommandService;
    private final OrganizationQueryService organizationQueryService;

    public TokensController(UserCommandService userCommandService, OrganizationQueryService organizationQueryService) {
        this.userCommandService = userCommandService;
        this.organizationQueryService = organizationQueryService;
    }

    @PostMapping
    @Operation(summary = "Sign in", description = "Authenticates a user and returns a JWT bearer token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated", content = @Content(schema = @Schema(implementation = AuthenticatedUserResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @ApiResponse(responseCode = "422", description = "User disabled")
    })
    public ResponseEntity<?> generateToken(@Valid @RequestBody SignInResource resource,
                                           @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var command = SignInCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = userCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                pair -> {
                    var organization = organizationQueryService
                            .handle(new GetOrganizationByIdQuery(pair.getLeft().getOrganizationIdValue()))
                            .orElseThrow();
                    return AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(pair.getLeft(), organization, pair.getRight());
                },
                HttpStatus.OK);
    }
}
