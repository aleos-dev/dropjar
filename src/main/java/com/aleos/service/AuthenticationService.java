package com.aleos.service;

import com.aleos.dto.SignUpPayload;
import com.aleos.exception.AuthenticationServiceException;
import com.aleos.mapper.UserMapper;
import com.aleos.model.Role;
import com.aleos.model.User;
import com.aleos.model.VerificationToken;
import com.aleos.repository.UserRepository;
import com.aleos.repository.VerificationTokenRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static com.aleos.model.Role.ROLE_USER;

/**
 * Service responsible for user authentication and management operations.
 * This service handles user sign-up, email verification, and activation token generation.
 * <p>
 * Dependencies:
 * - EmailService: Handles email sending for user verification.
 * - UserRepository: Manages user entities in the database.
 * - VerificationTokenRepository: Manages verification token entities in the database.
 * - UserMapper: Maps between SignUpPayload DTO and User entity.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Role DEFAULT_ROLE = ROLE_USER;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserMapper mapper;

    @Value("${registration.token.live.time}")
    public Long tokenLiveTime;

    /**
     * Signs up a new user by creating the user in the database, generating an activation token,
     * and sending a confirmation email to the user's email address.
     *
     * @param signUpPayload   data transfer object containing user signup information
     * @param emailSubject    the subject of the confirmation email
     * @param emailSender     the sender's email address
     * @param confirmationUrl the URL to confirm the user's email, including a placeholder for the token
     * @param encoder         the password encoder used to hash the user's password
     */
    @Transactional
    public void signUp(SignUpPayload signUpPayload,
                       String emailSubject,
                       String emailSender,
                       String confirmationUrl,
                       PasswordEncoder encoder
    ) {
        User newUser = mapper.toEntity(signUpPayload);
        newUser.setPassword(encoder.encode(newUser.getPassword()));
        newUser.setRole(DEFAULT_ROLE);

        User user = userRepository.save(newUser);

        VerificationToken token = generateActivationToken(user);
        verificationTokenRepository.save(token);

        emailService.send(
                user.getEmail(),
                emailSubject,
                emailSender,
                confirmationUrl.formatted(token.getId())
        );
    }

    /**
     * Processes the verification of a token by its ID. This method validates
     * the token's existence and whether it is still valid based on its creation time
     * and a predefined token lifetime. If valid, it completes the verification
     * process; otherwise, it throws an exception.
     *
     * @param verificationTokenId the UUID of the verification token to be processed
     * @return the email address associated with the verified token
     * @throws AuthenticationServiceException if the token is invalid or does not exist
     */
    @Transactional
    public String processVerification(UUID verificationTokenId) {
        return verificationTokenRepository.findById(verificationTokenId)
                .filter(token -> Instant.now().isBefore(token.getCreatedAt().plusSeconds(tokenLiveTime)))
                .map(this::completeVerificationProcess)
                .orElseThrow(() -> new AuthenticationServiceException("Invalid verification token: " + verificationTokenId));
    }

    private String completeVerificationProcess(VerificationToken token) {
        token.getUser().setActive(true);
        token.setVerifiedAt(Instant.now());

        return token.getUser().getEmail();
    }

    private VerificationToken generateActivationToken(User user) {
        return VerificationToken.builder()
                .user(user)
                .createdAt(Instant.now())
                .build();
    }
}
