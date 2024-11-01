package com.tma.restaurant_exercise_phase_2.security.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String email;
    private char[] password;
}
