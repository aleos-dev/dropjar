package com.aleos.repository;

import com.aleos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserRepository is an interface for managing User entities in the database.
 * It extends JpaRepository, providing standard CRUD operations.
 *
 * Methods:
 * - findByEmail: Retrieves a user by their email address.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
}
