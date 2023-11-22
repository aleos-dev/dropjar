package com.aleos.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * A record class representing the payload required for a user signup operation.
 * It includes user's firstname, lastname, email and password.
 */
public record SignUpPayload(

        @NotNull
        @Size(min = 3, max = 100, message = "Firstname length should be between {min} and {max}")
        String firstname,

        @NotNull
        @Size(min = 3, max = 100, message = "Lastname length should be between {min} and {max}")
        String lastname,

        @NotNull
        @Email(message = "Invalid email format")
        String email,

        @NotBlank
        @Size(min = 3, max = 100, message = "Password length should be between {min} and {max}")
        String password
) {

    public static SignUpPayload empty() {
        return new SignUpPayload("", "", "", "");
    }
}
