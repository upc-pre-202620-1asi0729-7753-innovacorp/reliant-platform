package com.innovacorp.reliant.platform.reporting.application.internal.queryservices;

import com.innovacorp.reliant.platform.reporting.application.queryservices.GeneratedReportQueryService;
import com.innovacorp.reliant.platform.reporting.domain.model.aggregates.GeneratedReport;
import com.innovacorp.reliant.platform.reporting.domain.model.queries.GetAllGeneratedReportsByOrganizationIdQuery;
import com.innovacorp.reliant.platform.reporting.domain.model.queries.GetGeneratedReportByIdQuery;
import com.innovacorp.reliant.platform.reporting.domain.repositories.GeneratedReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GeneratedReportQueryServiceImpl implements GeneratedReportQueryService {
    private final GeneratedReportRepository reportRepository;

    public GeneratedReportQueryServiceImpl(GeneratedReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public List<GeneratedReport> handle(GetAllGeneratedReportsByOrganizationIdQuery query) {
        return reportRepository.findAllByOrganizationId(query.organizationId(), query.subjectType(), query.subjectId());
    }

    @Override
    public Optional<GeneratedReport> handle(GetGeneratedReportByIdQuery query) {
        return reportRepository.findById(query.generatedReportId());
    }
}
