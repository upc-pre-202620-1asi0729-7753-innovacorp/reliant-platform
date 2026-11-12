package com.innovacorp.reliant.platform.iam.interfaces.rest;

import com.innovacorp.reliant.platform.iam.application.commandservices.UserCommandService;
import com.innovacorp.reliant.platform.iam.application.queryservices.UserQueryService;
import com.innovacorp.reliant.platform.iam.domain.model.queries.GetAllUsersByOrganizationIdQuery;
import com.innovacorp.reliant.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.innovacorp.reliant.platform.iam.interfaces.rest.resources.AssignRolesResource;
import com.innovacorp.reliant.platform.iam.interfaces.rest.resources.SignUpResource;
import com.innovacorp.reliant.platform.iam.interfaces.rest.resources.UserResource;
import com.innovacorp.reliant.platform.iam.interfaces.rest.transform.AssignRolesToUserCommandFromResourceAssembler;
import com.innovacorp.reliant.platform.iam.interfaces.rest.transform.SignUpCommandFromResourceAssembler;
import com.innovacorp.reliant.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
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
 * REST controller that exposes user resources. POST is the sign-up.
 */
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "User management endpoints")
public class UsersController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public UsersController(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
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

    @GetMapping
    @Operation(summary = "Get all users of an organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved", content = @Content(schema = @Schema(implementation = UserResource.class)))
    })
    public ResponseEntity<List<UserResource>> getAllUsers(@RequestParam Long organizationId,
                                                          @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var users = userQueryService.handle(new GetAllUsersByOrganizationIdQuery(new OrganizationId(organizationId)));
        return ResponseEntity.ok(users.stream().map(UserResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserResource.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> getUserById(@PathVariable Long userId,
                                         @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var user = userQueryService.handle(new GetUserByIdQuery(userId));
        if (user.isEmpty()) {
            return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(ApplicationError.notFound("user", userId.toString()));
        }
        return ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(user.get()));
    }

    @PutMapping("/{userId}/roles")
    @Operation(summary = "Assign roles to a user", description = "Replaces the whole role set of the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles assigned", content = @Content(schema = @Schema(implementation = UserResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User or role not found")
    })
    public ResponseEntity<?> assignRoles(@PathVariable Long userId, @Valid @RequestBody AssignRolesResource resource,
                                         @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        var command = AssignRolesToUserCommandFromResourceAssembler.toCommandFromResource(userId, resource);
        var result = userCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, UserResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
