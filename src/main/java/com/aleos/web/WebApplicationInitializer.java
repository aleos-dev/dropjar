package com.aleos.web;

import com.aleos.configuration.ApplicationConfiguration;
import jakarta.servlet.Filter;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Initializes the Spring Web MVC application by configuring the DispatcherServlet,
 * application contexts, and security filters.
 *
 * <p>This class extends {@link AbstractAnnotationConfigDispatcherServletInitializer} to
 * set up both the root application context and the servlet-specific context. It also
 * registers the {@link DelegatingFilterProxy} to integrate Spring Security into the
 * web application's filter chain.</p>
 *
 * <p>Configuration Details:</p>
 * <ul>
 *   <li><strong>Root Application Context:</strong> Configured with {@link ApplicationConfiguration},
 *       which typically contains service and repository beans shared across the application.</li>
 *   <li><strong>Servlet Application Context:</strong> Configured with {@link WebMvcConfiguration},
 *       which includes web-specific beans such as controllers and view resolvers.</li>
 *   <li><strong>DispatcherServlet Mapping:</strong> Mapped to the root URL pattern ("/").</li>
 *   <li><strong>Security Filter:</strong> Registers a {@link DelegatingFilterProxy} named
 *       "springSecurityFilterChain" to delegate security filtering to the Spring Security context.</li>
 * </ul>
 *
 * <p><b>By separating the root and servlet contexts, the application ensures a clear division
 * between business logic and web layer configurations. The {@code DelegatingFilterProxy} is
 * configured to look for the security filter chain within the servlet context to avoid
 * classpath issues and ensure proper integration with Spring MVC.</b></p>
 *
 * @since 2024-04-27
 */
public class WebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * Specifies the configuration classes for the root application context.
     *
     * <p>The root context typically contains beans that are shared across multiple
     * servlets, such as service and repository beans.</p>
     *
     * @return an array of classes to be used for the root application context configuration
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{ApplicationConfiguration.class};
    }

    /**
     * Specifies the configuration classes for the servlet application context.
     *
     * <p>The servlet context is specific to the DispatcherServlet and typically contains
     * web-related beans such as controllers, view resolvers, and handler mappings.</p>
     *
     * @return an array of classes to be used for the servlet application context configuration
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebMvcConfiguration.class};
    }

    /**
     * Defines the servlet mappings for the DispatcherServlet.
     *
     * <p>Maps the DispatcherServlet to the root URL pattern ("/"), meaning it will handle
     * all incoming requests to the application.</p>
     *
     * @return an array of URL patterns the DispatcherServlet should be mapped to
     */
    @Override
    @NonNull
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /**
     * Registers and configures servlet filters for the application.
     *
     * <p>Specifically, this method registers a {@link DelegatingFilterProxy} named
     * "springSecurityFilterChain". By default, {@code DelegatingFilterProxy} searches for
     * the specified bean in the root application context. However, since the security
     * configuration is part of the servlet application context, we need to direct the
     * proxy to look within the servlet context instead.</p>
     *
     * <p>This is achieved by setting the {@code contextAttribute} property to the name
     * of the servlet context attribute, which follows the pattern
     * {@code org.springframework.web.servlet.FrameworkServlet.CONTEXT.[servletName]}.
     * This ensures that the {@code DelegatingFilterProxy} correctly locates the
     * {@code springSecurityFilterChain} bean within the servlet context.</p>
     *
     * @return an array of {@link Filter} instances to be applied to incoming requests
     */
    @Override
    protected Filter[] getServletFilters() {
        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy("springSecurityFilterChain");
        delegatingFilterProxy.setContextAttribute(
                "org.springframework.web.servlet.FrameworkServlet.CONTEXT." + getServletName()
        );

        return new Filter[]{delegatingFilterProxy};
    }
}
