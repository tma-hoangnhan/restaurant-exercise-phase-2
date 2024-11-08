package com.tma.restaurant_exercise_phase_2.security.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "username CANNOT BE BLANK")
    private String username;

    @NotBlank(message = "email CANNOT BE BLANK")
    @Email(message = "email IS NOT IN APPROPRIATE FORM")
    private String email;

    @NotEmpty(message = "password CANNOT BE BLANK")
    private char[] password;
}
