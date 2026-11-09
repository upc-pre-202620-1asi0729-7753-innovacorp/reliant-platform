package com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.repositories;

import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.Ruc;
import com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.entities.CustomerPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerPersistenceRepository extends JpaRepository<CustomerPersistenceEntity, Long> {

    @Query("select c from CustomerPersistenceEntity c where c.supplierOrganizationId = :supplierOrganizationId order by c.legalName")
    List<CustomerPersistenceEntity> findAllBySupplierOrganizationId(
            @Param("supplierOrganizationId") OrganizationId supplierOrganizationId);

    List<CustomerPersistenceEntity> findAllByLinkedAssetOwnerOrganizationIdOrderByLegalName(Long linkedAssetOwnerOrganizationId);

    @Query("select count(c) from CustomerPersistenceEntity c where c.supplierOrganizationId = :supplierOrganizationId and c.ruc = :ruc")
    long countBySupplierOrganizationIdAndRuc(
            @Param("supplierOrganizationId") OrganizationId supplierOrganizationId, @Param("ruc") Ruc ruc);
}