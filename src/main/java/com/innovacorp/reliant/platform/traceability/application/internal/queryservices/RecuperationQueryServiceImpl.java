package com.innovacorp.reliant.platform.traceability.application.internal.queryservices;

import com.innovacorp.reliant.platform.traceability.application.queryservices.RecuperationQueryService;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Recuperation;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetAllRecuperationsByCustomerIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetAllRecuperationsBySupplierOrganizationIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetRecuperationByIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.repositories.RecuperationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecuperationQueryServiceImpl implements RecuperationQueryService {

    private final RecuperationRepository recuperationRepository;

    public RecuperationQueryServiceImpl(RecuperationRepository recuperationRepository) {
        this.recuperationRepository = recuperationRepository;
    }

    @Override
    public Optional<Recuperation> handle(GetRecuperationByIdQuery query) {
        return recuperationRepository.findById(query.recuperationId());
    }

    @Override
    public List<Recuperation> handle(GetAllRecuperationsBySupplierOrganizationIdQuery query) {
        return recuperationRepository.findAllBySupplierOrganizationId(query.supplierOrganizationId());
    }

    @Override
    public List<Recuperation> handle(GetAllRecuperationsByCustomerIdQuery query) {
        return recuperationRepository.findAllByCustomerId(query.customerId());
    }
}