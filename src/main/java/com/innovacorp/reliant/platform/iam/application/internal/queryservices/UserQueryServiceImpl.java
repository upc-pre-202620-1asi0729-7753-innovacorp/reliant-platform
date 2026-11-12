package com.innovacorp.reliant.platform.iam.application.internal.queryservices;

import com.innovacorp.reliant.platform.iam.application.queryservices.UserQueryService;
import com.innovacorp.reliant.platform.iam.domain.model.aggregates.User;
import com.innovacorp.reliant.platform.iam.domain.model.queries.GetAllUsersByOrganizationIdQuery;
import com.innovacorp.reliant.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.innovacorp.reliant.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.innovacorp.reliant.platform.iam.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> handle(GetAllUsersByOrganizationIdQuery query) {
        return userRepository.findAllByOrganizationId(query.organizationId());
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }

    @Override
    public Optional<User> handle(GetUserByEmailQuery query) {
        return userRepository.findByEmailAddress(query.emailAddress());
    }
}
