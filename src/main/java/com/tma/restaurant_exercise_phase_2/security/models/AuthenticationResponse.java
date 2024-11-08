package com.tma.restaurant_exercise_phase_2.security.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AuthenticationResponse {
    private int id;
    private String accessToken;
    private String username;
}
