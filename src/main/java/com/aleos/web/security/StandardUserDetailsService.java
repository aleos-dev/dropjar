package com.aleos.web.security;

import com.aleos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StandardUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads the user details by the given email.
     *
     * @param email the email of the user to be loaded
     * @return UserDetails containing the user's authorities, password, email, and status
     * @throws UsernameNotFoundException if a user with the given email is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> new UserDetails() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return List.of(user.getRole());
                    }

                    @Override
                    public String getPassword() {
                        return user.getPassword();
                    }

                    @Override
                    public String getUsername() {
                        return user.getEmail();
                    }

                    @Override
                    public boolean isEnabled() {
                        return user.getActive();
                    }

                }).orElseThrow(() -> new UsernameNotFoundException("User email: %s, not found".formatted(email)));
    }
}
