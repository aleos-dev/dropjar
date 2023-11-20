package com.aleos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Entity representing a verification token used for user account verification.
 * This entity is mapped to the "verification_token" table in the database.
 *
 * Annotations like @Entity, @Table, @ManyToOne, @NotNull, and @Column indicate
 * the entity's nature and constraints on its fields.
 *
 * The Lombok annotations @NoArgsConstructor, @AllArgsConstructor, @Builder,
 * @Getter, and @Setter are used to reduce boilerplate code.
 *
 * Fields:
 * - id (UUID): The unique identifier for the verification token, generated using UUID strategy.
 * - user (User): The associated User entity for whom the token is generated. Cannot be null.
 * - createdAt (Instant): The timestamp when the token was created. Defaults to the current time.
 * - verifiedAt (Instant): The timestamp when the token was verified.
 */
@Entity
@Table(name = "verification_token")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "verified_at", nullable = false)
    private Instant verifiedAt;
}
