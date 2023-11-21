package com.aleos.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for setting up authentication-related beans in a Spring application.
 *
 * This class defines beans for DaoAuthenticationProvider and PasswordEncoder,
 * which are essential components in the authentication process using Spring Security.
 */
@Configuration
public class AuthenticationConfig {

    /**
     * Creates a DaoAuthenticationProvider bean which will handle the authentication process.
     * <p>
     * * Note: Spring Boot automatically creates a DaoAuthenticationProvider
     * * if a UserDetailsService and PasswordEncoder are defined as beans.
     *
     * @param userDetailsService a service that loads user-specific data
     * @param encoder            a password encoder to encode the user passwords
     * @return a configured DaoAuthenticationProvider instance
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(StandardUserDetailsService userDetailsService,
                                                            PasswordEncoder encoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);

        return authProvider;
    }

    /**
     * Provides a PasswordEncoder bean that uses the BCrypt hashing algorithm for securing passwords.
     *
     * @return a BCryptPasswordEncoder instance for encoding passwords
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
