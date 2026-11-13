package com.innovacorp.reliant.platform.reporting.application.internal.queryservices;

import com.innovacorp.reliant.platform.reporting.application.queryservices.ReportTemplateQueryService;
import com.innovacorp.reliant.platform.reporting.domain.model.aggregates.ReportTemplate;
import com.innovacorp.reliant.platform.reporting.domain.model.queries.GetAllReportTemplatesByOrganizationIdQuery;
import com.innovacorp.reliant.platform.reporting.domain.model.queries.GetReportTemplateByIdQuery;
import com.innovacorp.reliant.platform.reporting.domain.repositories.ReportTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportTemplateQueryServiceImpl implements ReportTemplateQueryService {
    private final ReportTemplateRepository templateRepository;

    public ReportTemplateQueryServiceImpl(ReportTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public List<ReportTemplate> handle(GetAllReportTemplatesByOrganizationIdQuery query) {
        return templateRepository.findAllByOrganizationId(query.organizationId(), query.reportKind());
    }

    @Override
    public Optional<ReportTemplate> handle(GetReportTemplateByIdQuery query) {
        return templateRepository.findById(query.reportTemplateId());
    }
}
