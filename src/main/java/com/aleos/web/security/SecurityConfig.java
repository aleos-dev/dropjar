package com.aleos.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * The SecurityConfig class configures the security settings for the web application.
 * Uses Spring Security's configuration capabilities to set up authentication, authorization,
 * session management, and other settings.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Value("${security.login-page}")
    private String loginPage;

    @Value("${security.login-processing-url}")
    private String loginProcessingUrl;

    @Value("${security.default-success-url}")
    private String defaultSuccessUrl;

    @Value("${security.logout-url}")
    private String logoutUrl;

    @Value("${security.logout-success-url}")
    private String logoutSuccessUrl;

    @Value("${security.session-expired-url}")
    private String sessionExpiredUrl;

    @Value("${security.maximum-sessions}")
    private int maximumSessions;

    /**
     * Configures the security filter chain for the web application.
     *
     * @param httpSecurity the {@link HttpSecurity} to modify.
     * @return a {@link SecurityFilterChain} configured with the desired settings.
     * @throws Exception in case of any configuration errors.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/resources/**", "/auth/**").permitAll()
                        .anyRequest().authenticated()

                ).formLogin(customizer -> customizer
                        .loginPage(loginPage)
                        .loginProcessingUrl(loginProcessingUrl)
                        .defaultSuccessUrl(defaultSuccessUrl, true)
                        .failureHandler(authenticationFailureHandler)
                        .usernameParameter("email")
                        .permitAll()

                ).logout(customizer -> customizer
                        .logoutUrl(logoutUrl)
                        .logoutSuccessUrl(logoutSuccessUrl)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()

                ).sessionManagement(customizer ->
                        customizer
                                .maximumSessions(maximumSessions)
                                .expiredUrl(sessionExpiredUrl)

                ).authenticationProvider(authenticationProvider)

                .build();
    }
}
