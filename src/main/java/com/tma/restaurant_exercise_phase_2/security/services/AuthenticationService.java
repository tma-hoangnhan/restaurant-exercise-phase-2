package com.tma.restaurant_exercise_phase_2.security.services;

import com.tma.restaurant_exercise_phase_2.model.User;
import com.tma.restaurant_exercise_phase_2.security.models.AuthenticationRequest;
import com.tma.restaurant_exercise_phase_2.security.models.AuthenticationResponse;
import com.tma.restaurant_exercise_phase_2.security.models.RegisterRequest;
import com.tma.restaurant_exercise_phase_2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;

@Service
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;


    @Autowired
    public AuthenticationService(UserService userService,
                                 JwtService jwtService,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(CharBuffer.wrap(request.getPassword()));

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(encodedPassword.toCharArray())
                .build();
        userService.addUser(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(request.getEmail())
                .password(encodedPassword)
                .build();

        String accessToken = jwtService.generateToken(userDetails);

        return AuthenticationResponse.builder()
                .id(user.getId())
                .accessToken(accessToken)
                .username(user.getUsername())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        new String(request.getPassword())
                )
        );

        User user = userService.getUserByEmail(request.getEmail());
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(new String(user.getPassword()))
                .build();

        String accessToken = jwtService.generateToken(userDetails);
        return AuthenticationResponse.builder()
                .id(user.getId())
                .accessToken(accessToken)
                .username(user.getUsername())
                .build();
    }
}
