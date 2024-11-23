package com.aleos.web;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.ISpringTemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * WebMvcConfiguration class provides the configuration for the Spring MVC framework.
 * It customizes the configuration by implementing the WebMvcConfigurer interface.
 * <p>
 * Annotations:
 * @Configuration - Indicates that the class can be used by the Spring IoC container as a source of bean definitions.
 * @EnableWebMvc - Enables default Spring MVC configuration.
 * @ComponentScan - Configures component scanning directives for use with @Configuration classes.
 * <p>
 * Methods:
 * - addViewControllers(ViewControllerRegistry registry): Customize the view controllers.
 * - addResourceHandlers(ResourceHandlerRegistry registry): Add resource handlers for serving static resources.
 * - templateEngine(ITemplateResolver templateResolver): Configure the SpringTemplateEngine bean with the given template resolver.
 * - templateResolver(): Configures the SpringResourceTemplateResolver bean to resolve templates from specified locations.
 * - viewResolver(ISpringTemplateEngine templateEngine): Configure the ThymeleafViewResolver bean with the given template engine.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.aleos.web")
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(31556926);
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Bean
    public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setEnableSpringELCompiler(true);
        engine.setTemplateResolver(templateResolver);
        engine.addDialect(new LayoutDialect());
        return engine;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setCharacterEncoding("UTF-8");
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }

    @Bean
    public ViewResolver viewResolver(ISpringTemplateEngine templateEngine) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine);
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }
}
