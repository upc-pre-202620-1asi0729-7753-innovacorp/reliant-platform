package com.innovacorp.reliant.platform.traceability.application.internal.queryservices;

import com.innovacorp.reliant.platform.traceability.application.queryservices.CustomerQueryService;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Customer;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetAllCustomersByLinkedAssetOwnerOrganizationIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetAllCustomersBySupplierOrganizationIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetCustomerByIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerQueryServiceImpl implements CustomerQueryService {
    private final CustomerRepository customerRepository;

    public CustomerQueryServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Customer> handle(GetCustomerByIdQuery query) {
        return customerRepository.findById(query.customerId());
    }

    @Override
    public List<Customer> handle(GetAllCustomersBySupplierOrganizationIdQuery query) {
        return customerRepository.findAllBySupplierOrganizationId(query.supplierOrganizationId());
    }

    @Override
    public List<Customer> handle(GetAllCustomersByLinkedAssetOwnerOrganizationIdQuery query) {
        return customerRepository.findAllByLinkedAssetOwnerOrganizationId(query.assetOwnerOrganizationId());
    }
}
