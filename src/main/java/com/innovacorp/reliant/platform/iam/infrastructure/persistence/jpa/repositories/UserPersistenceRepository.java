package com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.repositories;

import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.EmailAddress;
import com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.entities.UserPersistenceEntity;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPersistenceRepository extends JpaRepository<UserPersistenceEntity, Long> {

    @Query("select count(u) from UserPersistenceEntity u where u.emailAddress = :emailAddress")
    long countByEmailAddress(@Param("emailAddress") EmailAddress emailAddress);

    @Query("select u from UserPersistenceEntity u where u.emailAddress = :emailAddress")
    Optional<UserPersistenceEntity> findByEmailAddress(@Param("emailAddress") EmailAddress emailAddress);

    @Query("select u from UserPersistenceEntity u where u.organizationId = :organizationId order by u.fullName")
    List<UserPersistenceEntity> findAllByOrganizationId(@Param("organizationId") OrganizationId organizationId);
}