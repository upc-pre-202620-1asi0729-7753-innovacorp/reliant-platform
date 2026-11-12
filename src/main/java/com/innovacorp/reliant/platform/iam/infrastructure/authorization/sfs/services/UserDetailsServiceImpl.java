package com.innovacorp.reliant.platform.iam.infrastructure.authorization.sfs.services;

import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.EmailAddress;
import com.innovacorp.reliant.platform.iam.domain.repositories.UserRepository;
import com.innovacorp.reliant.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "defaultUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmailAddress(new EmailAddress(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        return UserDetailsImpl.build(user);
    }
}
