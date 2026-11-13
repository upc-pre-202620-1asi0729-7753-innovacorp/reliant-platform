package com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.repositories;

import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.Ruc;
import com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.entities.OrganizationPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationPersistenceRepository extends JpaRepository<OrganizationPersistenceEntity, Long> {

    @Query("select count(o) from OrganizationPersistenceEntity o where o.ruc = :ruc")
    long countByRuc(@Param("ruc") Ruc ruc);
}
