package com.innovacorp.reliant.platform.iam.domain.repositories;

import com.innovacorp.reliant.platform.iam.domain.model.aggregates.User;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.EmailAddress;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;

import java.util.List;
import java.util.Optional;

/**
 * User repository port.
 */
public interface UserRepository {

    User save(User user);

    boolean existsByEmailAddress(EmailAddress emailAddress);

    Optional<User> findByEmailAddress(EmailAddress emailAddress);

    Optional<User> findById(Long id);

    List<User> findAllByOrganizationId(OrganizationId organizationId);
}