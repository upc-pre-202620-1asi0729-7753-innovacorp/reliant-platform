package com.innovacorp.reliant.platform.iam.interfaces.rest;

import com.innovacorp.reliant.platform.iam.application.commandservices.UserCommandService;
import com.innovacorp.reliant.platform.iam.interfaces.rest.resources.SignUpResource;
import com.innovacorp.reliant.platform.iam.interfaces.rest.resources.UserResource;
import com.innovacorp.reliant.platform.iam.interfaces.rest.transform.SignUpCommandFromResourceAssembler;
import com.innovacorp.reliant.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
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
 * REST controller that exposes user resources. POST is the sign-up.
 */
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "User management endpoints")
public class UsersController {

    private final UserCommandService userCommandService;

    public UsersController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @PostMapping
    @Operation(summary = "Sign up", description = "Registers an organization and its first administrator user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created", content = @Content(schema = @Schema(implementation = UserResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Email or RUC already registered")
    })
    public ResponseEntity<?> createUser(@Valid @RequestBody SignUpResource resource,
                                        @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var command = SignUpCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = userCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, UserResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }
}