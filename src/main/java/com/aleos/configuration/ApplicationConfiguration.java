package com.aleos.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(value = "com.aleos",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "com.aleos.web.*"
        ))
@Slf4j
public class ApplicationConfiguration {

    /**
     * Configures and provides a ResourceBundleMessageSource bean.
     * The message source is used for resolving messages, typically for internationalization.
     *
     * @return a configured MessageSource with basename set to "messages" and default encoding "UTF-8".
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * Configures and returns a JavaMailSender object for sending emails via the SMTP server.
     *
     * @param username the SMTP server username for email authentication
     * @param password the SMTP server password for email authentication
     * @return a configured JavaMailSender object
     */
    @Bean
    public JavaMailSender javaMailSender(
            @Value("${mail.smtp.username}") String username,
            @Value("${mail.smtp.password}") String password
    ) {
        log.debug("Initializing SMTP properties for email session");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Configure SMTP server
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        // Define JavaMail properties
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.

                put("mail.smtp.starttls.enable", "true"); // Use STARTTLS for secure connections
        props.

                put("mail.debug", "true");

        log.

                debug("SMTP properties set: host={}, port={}, auth={}, starttls={}",
                        props.get("mail.smtp.host"),
                        props.

                                get("mail.smtp.port"),
                        props.

                                get("mail.smtp.auth"),
                        props.

                                get("mail.smtp.starttls.enable"));

        return mailSender;
    }
}
