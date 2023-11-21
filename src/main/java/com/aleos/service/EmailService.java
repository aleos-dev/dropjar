package com.aleos.service;

import com.aleos.exception.AuthenticationServiceException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * Service responsible for sending emails asynchronously.
 * <p>
 * This service uses JavaMailSender to create and send MimeMessage emails.
 * It provides a method to send an email with the specified recipient, subject, sender, and content.
 * <p>
 * Annotations:
 * - @Service: Indicates that this class is a Spring service component.
 * - @RequiredArgsConstructor: Generates a constructor with required arguments.
 * - @Async: Indicates that the send method should be executed asynchronously.
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Sends an email asynchronously using the provided recipient, subject, sender, and content.
     *
     * @param to            the recipient's email address
     * @param subject       the subject of the email
     * @param from          the sender's email address
     * @param emailContent  the content of the email
     * @throws AuthenticationServiceException if there is an issue creating or sending the email
     */
    @Async
    public void send(
            String to,
            String subject,
            String from,
            String emailContent
    ) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setText(emailContent, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new AuthenticationServiceException("The user was created, but confirmation url was not sent", e);
        }
    }
}
