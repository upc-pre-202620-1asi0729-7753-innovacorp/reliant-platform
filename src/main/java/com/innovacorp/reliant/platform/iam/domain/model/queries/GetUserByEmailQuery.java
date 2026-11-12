package com.innovacorp.reliant.platform.iam.domain.model.queries;

import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.EmailAddress;

public record GetUserByEmailQuery(EmailAddress emailAddress) {
}
