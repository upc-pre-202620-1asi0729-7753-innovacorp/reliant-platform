package com.innovacorp.reliant.platform.iam.domain.model.queries;

import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.Ruc;

/**
 * Query to get an organization by its RUC (unique per organization).
 */
public record GetOrganizationByRucQuery(Ruc ruc) {
}
