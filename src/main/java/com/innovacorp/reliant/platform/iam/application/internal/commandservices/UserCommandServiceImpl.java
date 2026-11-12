package com.innovacorp.reliant.platform.iam.application.internal.commandservices;

import com.innovacorp.reliant.platform.iam.application.commandservices.UserCommandService;
import com.innovacorp.reliant.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.innovacorp.reliant.platform.iam.domain.model.aggregates.Organization;
import com.innovacorp.reliant.platform.iam.domain.model.aggregates.User;
import com.innovacorp.reliant.platform.iam.domain.model.commands.SignUpCommand;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.EmailAddress;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.OrganizationType;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.Roles;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.Ruc;
import com.innovacorp.reliant.platform.iam.domain.repositories.OrganizationRepository;
import com.innovacorp.reliant.platform.iam.domain.repositories.RoleRepository;
import com.innovacorp.reliant.platform.iam.domain.repositories.UserRepository;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User Command Service Implementation. Sign-up creates the organization and its first administrator in one transaction.
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final RoleRepository roleRepository;
    private final HashingService hashingService;

    public UserCommandServiceImpl(UserRepository userRepository, OrganizationRepository organizationRepository,
                                  RoleRepository roleRepository, HashingService hashingService) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.roleRepository = roleRepository;
        this.hashingService = hashingService;
    }

    @Override
    @Transactional
    public Result<User, ApplicationError> handle(SignUpCommand command) {
        try {
            var emailAddress = new EmailAddress(command.email());
            var ruc = new Ruc(command.ruc());
            if (userRepository.existsByEmailAddress(emailAddress)) {
                return Result.failure(ApplicationError.conflict("user", command.email()));
            }
            if (organizationRepository.existsByRuc(ruc)) {
                return Result.failure(ApplicationError.conflict("organization", command.ruc()));
            }
            var adminRole = roleRepository.findByName(Roles.ROLE_ORG_ADMIN);
            if (adminRole.isEmpty()) {
                return Result.failure(ApplicationError.notFound("role", Roles.ROLE_ORG_ADMIN.name()));
            }

            var organization = organizationRepository.save(
                    new Organization(command.organizationName(), ruc, OrganizationType.fromString(command.organizationType())));
            var user = new User(emailAddress, command.fullName(), hashingService.encode(command.password()),
                    new OrganizationId(organization.getId()), List.of(adminRole.get()));
            return Result.success(userRepository.save(user));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("sign-up", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("sign-up", e.getMessage()));
        }
    }
}
