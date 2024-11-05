package com.tma.restaurant_exercise_phase_2.security;

import com.tma.restaurant_exercise_phase_2.model.User;
import com.tma.restaurant_exercise_phase_2.security.models.AuthenticationRequest;
import com.tma.restaurant_exercise_phase_2.security.models.AuthenticationResponse;
import com.tma.restaurant_exercise_phase_2.security.models.RegisterRequest;
import com.tma.restaurant_exercise_phase_2.security.services.AuthenticationService;
import com.tma.restaurant_exercise_phase_2.security.services.JwtService;
import com.tma.restaurant_exercise_phase_2.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.CharBuffer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
class AuthenticationServiceTest {
    private AuthenticationService authenticationService;

    @Mock private UserService userService;
    @Mock private JwtService jwtService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authManager;

    @Captor
    ArgumentCaptor<User> userCaptor;

    User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(userService, jwtService, passwordEncoder, authManager);

        user = User.builder()
                .id(1)
                .username("Username")
                .email("username@gmail.com")
                .password(new char[] {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'})
                .build();
    }

    @Test
    void authenticate() {
        // given
        String expectedUsername = "Username";
        String expectedToken = "This is a new token";

        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("username@gmail.com");
        request.setPassword(new char[] {1, 2, 3, 4, 5, 6});

        when(userService.getUserByEmail("username@gmail.com")).thenReturn(user);
        when(jwtService.generateToken(Mockito.any(UserDetails.class))).thenReturn(expectedToken);
        // when
        AuthenticationResponse actual = authenticationService.authenticate(request);

        // then
        verify(authManager).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        verify(userService).getUserByEmail("username@gmail.com");
        verify(jwtService).generateToken(Mockito.any(UserDetails.class));

        assertEquals(expectedUsername, actual.getUsername());
        assertEquals(expectedToken, actual.getAccessToken());
    }

    @Test
    void register() {
        // given
        String expectedPassword = "Encoded Password";

        RegisterRequest request = new RegisterRequest();
        request.setUsername("Username");
        request.setEmail("username@gmail.com");
        request.setPassword(new char[] {1, 2, 3, 4, 5, 6});

        when(passwordEncoder.encode(CharBuffer.wrap(request.getPassword()))).thenReturn(expectedPassword);
        when(jwtService.generateToken(Mockito.any(UserDetails.class))).thenReturn("This is a token");

        // when
        AuthenticationResponse response = authenticationService.register(request);

        // then: verify calling passwordEncoder, addUser and generateToken
        verify(passwordEncoder).encode(CharBuffer.wrap(request.getPassword()));
        verify(userService).addUser(userCaptor.capture());
        verify(jwtService).generateToken(Mockito.any(UserDetails.class));

        // and: Assert the response
        assertEquals("This is a token", response.getAccessToken());
        assertEquals("Username", response.getUsername());

        // and: Assert the captured User
        User capturedUser = userCaptor.getValue();
        assertEquals(request.getUsername(), capturedUser.getUsername());
        assertEquals(request.getEmail(), capturedUser.getEmail());
        assertArrayEquals(expectedPassword.toCharArray(), capturedUser.getPassword());

    }
}
