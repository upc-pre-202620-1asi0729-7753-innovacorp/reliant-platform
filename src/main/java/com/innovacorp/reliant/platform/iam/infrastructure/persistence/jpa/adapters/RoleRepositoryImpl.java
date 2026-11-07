package com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.adapters;

import com.innovacorp.reliant.platform.iam.domain.model.entities.Role;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.Roles;
import com.innovacorp.reliant.platform.iam.domain.repositories.RoleRepository;
import com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.assemblers.RolePersistenceAssembler;
import com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.repositories.RolePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private final RolePersistenceRepository rolePersistenceRepository;

    public RoleRepositoryImpl(RolePersistenceRepository rolePersistenceRepository) {
        this.rolePersistenceRepository = rolePersistenceRepository;
    }

    @Override
    public Optional<Role> findByName(Roles name) {
        return rolePersistenceRepository.findByName(name)
                .map(RolePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Role> findAll() {
        return rolePersistenceRepository.findAll().stream()
                .map(RolePersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public List<Role> findAllById(List<Long> ids) {
        return rolePersistenceRepository.findAllById(ids).stream()
                .map(RolePersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public Role save(Role role) {
        var saved = rolePersistenceRepository.save(
                RolePersistenceAssembler.toPersistenceFromDomain(role));
        return RolePersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public boolean existsByName(Roles name) {
        return rolePersistenceRepository.existsByName(name);
    }
}