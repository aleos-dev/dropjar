package com.aleos.repository;

import com.aleos.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * VerificationTokenRepository is an interface for managing VerificationToken entities in the database.
 * It extends JpaRepository, providing standard CRUD operations for VerificationToken entities identified by a UUID.
 *
 * Methods:
 * - Extends all standard JpaRepository methods including save, findById, findAll, and delete.
 */
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {
}
