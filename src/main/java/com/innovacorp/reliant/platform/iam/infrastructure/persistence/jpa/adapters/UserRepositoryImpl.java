package com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.adapters;

import com.innovacorp.reliant.platform.iam.domain.model.aggregates.User;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.EmailAddress;
import com.innovacorp.reliant.platform.iam.domain.repositories.UserRepository;
import com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.assemblers.UserPersistenceAssembler;
import com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.repositories.UserPersistenceRepository;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.infrastructure.events.DomainEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserPersistenceRepository persistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public UserRepositoryImpl(UserPersistenceRepository persistenceRepository, DomainEventPublisher domainEventPublisher) {
        this.persistenceRepository = persistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public User save(User user) {
        boolean isNew = user.getId() == null;
        var savedEntity = persistenceRepository.save(UserPersistenceAssembler.toPersistenceFromDomain(user));
        var saved = UserPersistenceAssembler.toDomainFromPersistence(savedEntity);
        if (isNew) {
            saved.onCreated();
            domainEventPublisher.publishAndClearEvents(saved);
        }
        return saved;
    }

    @Override
    public boolean existsByEmailAddress(EmailAddress emailAddress) {
        return persistenceRepository.countByEmailAddress(emailAddress) > 0;
    }

    @Override
    public Optional<User> findByEmailAddress(EmailAddress emailAddress) {
        return persistenceRepository.findByEmailAddress(emailAddress).map(UserPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<User> findById(Long id) {
        return persistenceRepository.findById(id).map(UserPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<User> findAllByOrganizationId(OrganizationId organizationId) {
        return persistenceRepository.findAllByOrganizationId(organizationId).stream()
                .map(UserPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }
}
