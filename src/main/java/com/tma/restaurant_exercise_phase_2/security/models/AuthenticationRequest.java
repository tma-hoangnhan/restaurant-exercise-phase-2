package com.tma.restaurant_exercise_phase_2.security.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AuthenticationRequest {
    private String email;
    private char[] password;
}
