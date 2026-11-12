package com.innovacorp.reliant.platform.iam.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RoleId", example = "{\"roleId\": 1}")
public record RoleIdResource(@Schema(description = "Role id", example = "1") Long roleId) {
}
