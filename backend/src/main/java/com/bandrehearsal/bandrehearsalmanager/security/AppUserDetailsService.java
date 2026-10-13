package com.bandrehearsal.bandrehearsalmanager.security;

import com.bandrehearsal.bandrehearsalmanager.models.AppUser;
import com.bandrehearsal.bandrehearsalmanager.repositories.IAppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** Loads users from the "users" table for Spring Security. */
@Service
public class AppUserDetailsService implements UserDetailsService {

    private final IAppUserRepository userRepository;

    public AppUserDetailsService(IAppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));

        // roles("ADMIN") becomes the authority "ROLE_ADMIN", which hasRole("ADMIN") checks for
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
