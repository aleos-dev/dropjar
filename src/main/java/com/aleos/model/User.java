package com.aleos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    private Integer id;

    @NaturalId
    @Email
    private String email;

    @NonNull
    @Size(min = 3, max = 100, message = "Password length should be between {min} and {max}")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

}
