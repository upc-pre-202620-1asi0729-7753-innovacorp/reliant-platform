package com.innovacorp.reliant.platform.traceability.domain.repositories;

import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.RecoveredComponent;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.CustomerId;

import java.util.List;
import java.util.Optional;

public interface RecoveredComponentRepository {
    RecoveredComponent save(RecoveredComponent component);

    Optional<RecoveredComponent> findById(Long id);

    List<RecoveredComponent> findAllByCustomerId(CustomerId customerId);

    boolean existsByCustomerIdAndSerialNumber(CustomerId customerId, String serialNumber);
}
