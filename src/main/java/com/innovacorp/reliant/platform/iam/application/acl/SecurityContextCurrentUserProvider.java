package com.innovacorp.reliant.platform.iam.application.acl;

import com.innovacorp.reliant.platform.iam.domain.model.queries.GetOrganizationByIdQuery;
import com.innovacorp.reliant.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.EmailAddress;
import com.innovacorp.reliant.platform.iam.application.queryservices.OrganizationQueryService;
import com.innovacorp.reliant.platform.iam.application.queryservices.UserQueryService;
import com.innovacorp.reliant.platform.shared.application.context.CurrentUser;
import com.innovacorp.reliant.platform.shared.application.context.CurrentUserProvider;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * Resolves the authenticated user from the Spring Security context. ACL of iam toward other contexts.
 */
@Service
public class SecurityContextCurrentUserProvider implements CurrentUserProvider {
    private final UserQueryService userQueryService;
    private final OrganizationQueryService organizationQueryService;

    public SecurityContextCurrentUserProvider(UserQueryService userQueryService, OrganizationQueryService organizationQueryService) {
        this.userQueryService = userQueryService;
        this.organizationQueryService = organizationQueryService;
    }

    @Override
    public CurrentUser current() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails details)) {
            throw new AuthenticationCredentialsNotFoundException("No authenticated user");
        }
        var user = userQueryService.handle(new GetUserByEmailQuery(new EmailAddress(details.getUsername())))
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User not found"));
        var organization = organizationQueryService.handle(new GetOrganizationByIdQuery(user.getOrganizationIdValue()))
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Organization not found"));
        var roles = user.getRoles().stream().map(role -> role.getStringName()).collect(Collectors.toSet());
        return new CurrentUser(user.getId(), user.getOrganizationIdValue(), organization.getOrganizationType().name(), user.getEmailValue(), roles);
    }
}
