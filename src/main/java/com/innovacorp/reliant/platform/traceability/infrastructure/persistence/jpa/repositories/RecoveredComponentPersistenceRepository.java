package com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.repositories;

import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.CustomerId;
import com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.entities.RecoveredComponentPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecoveredComponentPersistenceRepository extends JpaRepository<RecoveredComponentPersistenceEntity, Long> {

    @Query("select c from RecoveredComponentPersistenceEntity c where c.customerId = :customerId order by c.serialNumber")
    List<RecoveredComponentPersistenceEntity> findAllByCustomerId(@Param("customerId") CustomerId customerId);

    @Query("select count(c) from RecoveredComponentPersistenceEntity c where c.customerId = :customerId and c.serialNumber = :serialNumber")
    long countByCustomerIdAndSerialNumber(
            @Param("customerId") CustomerId customerId,
            @Param("serialNumber") String serialNumber);
}