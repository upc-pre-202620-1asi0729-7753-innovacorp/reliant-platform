package com.innovacorp.reliant.platform.traceability.domain.repositories;

import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Recuperation;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.CustomerId;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.WorkOrderNumber;

import java.util.List;
import java.util.Optional;

public interface RecuperationRepository {
    Recuperation save(Recuperation recuperation);

    Optional<Recuperation> findById(Long id);

    List<Recuperation> findAllBySupplierOrganizationId(OrganizationId supplierOrganizationId);

    List<Recuperation> findAllByCustomerId(CustomerId customerId);

    boolean existsBySupplierOrganizationIdAndWorkOrderNumber(OrganizationId supplierOrganizationId, WorkOrderNumber workOrderNumber);
}
