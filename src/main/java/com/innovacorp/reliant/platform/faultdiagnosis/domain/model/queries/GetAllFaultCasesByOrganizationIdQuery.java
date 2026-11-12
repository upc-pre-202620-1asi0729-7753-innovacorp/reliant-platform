package com.innovacorp.reliant.platform.faultdiagnosis.domain.model.queries;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.FaultCaseStatus;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.SpraySessionId;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;

/** spraySessionId and status are optional filters (null = no filter). */
public record GetAllFaultCasesByOrganizationIdQuery(OrganizationId organizationId, SpraySessionId spraySessionId, FaultCaseStatus status) {
}
