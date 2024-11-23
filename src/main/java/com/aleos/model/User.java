package com.aleos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

/**
 * Represents a user in the system.
 * This entity is mapped to the "users" table in the database.
 *
 * Annotations such as @NotNull, @Size, @Email, and @Enumerated specify
 * validation and mapping constraints for the respective fields.
 * The class uses Lombok annotations like @Getter, @Setter, @NoArgsConstructor,
 * and @AllArgsConstructor to reduce boilerplate code.
 *
 * The equals and hashCode methods are overridden to ensure equality
 * based on the email field, which is marked as the natural ID of the entity.
 *
 * Fields:
 * - id (Integer): The unique identifier for the user.
 * - firstname (String): The user's first name. Must be between 3 and 100 characters.
 * - lastname (String): The user's last name. Must be between 3 and 100 characters.
 * - email (String): The user's email address. Must be unique and valid.
 * - password (String): The user's password.
 * - role (Role): The user's role within the system. Must not be null.
 * - active (Boolean): Indicates whether the user is active. Defaults to false.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 3, max = 100, message = "Firstname length should be between {min} and {max}")
    @Column(nullable = false)
    private String firstname;

    @NotNull
    @Size(min = 3, max = 100, message = "Lastname length should be between {min} and {max}")
    @Column(nullable = false)
    private String lastname;

    @NotNull
    @NaturalId
    @Email(message = "Invalid email format {value}")
    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String email;

    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @NotNull
    @Column(nullable = false)
    private Boolean active = false;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = (o instanceof HibernateProxy it) ?
                it.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = (this instanceof HibernateProxy it) ?
                it.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getEmail() != null && Objects.equals(getEmail(), user.getEmail());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(email);
    }
}
