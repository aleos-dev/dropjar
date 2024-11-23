package com.aleos.web.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * Configuration class for custom authentication handlers.
 */
@Configuration
public class CustomHandlersConfig {

    private static final String LOGIN_KEY = "login";
    private static final String EMAIL_KEY = "email";

    @Value("${security.login-failure-url}")
    private String loginFailureUrl;

    /**
     * Creates an AuthenticationFailureHandler bean that handles authentication
     * failures by redirecting the user to a login failure URL and setting the
     * user's email address as a session attribute.
     *
     * @return the AuthenticationFailureHandler that handles authentication failures
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {

        return (request, response, exception) -> {
            request.getSession().setAttribute(LOGIN_KEY, request.getParameter(EMAIL_KEY));
            response.sendRedirect(request.getContextPath() + loginFailureUrl);
        };
    }
}
