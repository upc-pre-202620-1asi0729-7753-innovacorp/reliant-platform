package com.innovacorp.reliant.platform.traceability.domain.model.queries;

import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.CustomerId;

public record GetAllComponentsByCustomerIdQuery(CustomerId customerId) {
}