package com.innovacorp.reliant.platform.faultdiagnosis.application.internal.queryservices;

import com.innovacorp.reliant.platform.faultdiagnosis.application.queryservices.FaultCaseQueryService;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.aggregates.FaultCase;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.queries.GetAllFaultCasesByOrganizationIdQuery;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.queries.GetFaultCaseByIdQuery;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.repositories.FaultCaseRepository;
import com.innovacorp.reliant.platform.shared.application.context.CurrentUserProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FaultCaseQueryServiceImpl implements FaultCaseQueryService {
    private final FaultCaseRepository caseRepository;
    private final CurrentUserProvider currentUserProvider;

    public FaultCaseQueryServiceImpl(FaultCaseRepository caseRepository, CurrentUserProvider currentUserProvider) {
        this.caseRepository = caseRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public List<FaultCase> handle(GetAllFaultCasesByOrganizationIdQuery query) {
        return caseRepository.findAllByOrganizationId(query.organizationId(), query.spraySessionId(), query.status());
    }

    /** Cases of another tenant are reported as not found. */
    @Override
    public Optional<FaultCase> handle(GetFaultCaseByIdQuery query) {
        var organizationId = currentUserProvider.current().organizationId();
        return caseRepository.findById(query.faultCaseId()).filter(c -> c.isOwnedBy(organizationId));
    }
}
