package com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.repositories;

import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.CustomerId;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.WorkOrderNumber;
import com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.entities.RecuperationPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecuperationPersistenceRepository extends JpaRepository<RecuperationPersistenceEntity, Long> {

    @Query("select r from RecuperationPersistenceEntity r where r.supplierOrganizationId = :supplierOrganizationId order by r.receivedAt desc")
    List<RecuperationPersistenceEntity> findAllBySupplierOrganizationId(
            @Param("supplierOrganizationId") OrganizationId supplierOrganizationId);

    @Query("select r from RecuperationPersistenceEntity r where r.customerId = :customerId order by r.receivedAt desc")
    List<RecuperationPersistenceEntity> findAllByCustomerId(@Param("customerId") CustomerId customerId);

    @Query("select count(r) from RecuperationPersistenceEntity r where r.supplierOrganizationId = :supplierOrganizationId and r.workOrderNumber = :workOrderNumber")
    long countBySupplierOrganizationIdAndWorkOrderNumber(
            @Param("supplierOrganizationId") OrganizationId supplierOrganizationId,
            @Param("workOrderNumber") WorkOrderNumber workOrderNumber);
}