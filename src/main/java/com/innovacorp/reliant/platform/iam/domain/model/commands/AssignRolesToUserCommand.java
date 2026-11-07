package com.innovacorp.reliant.platform.iam.domain.model.commands;

import java.util.List;

public record AssignRolesToUserCommand(Long userId, List<Long> roleIds) {
}