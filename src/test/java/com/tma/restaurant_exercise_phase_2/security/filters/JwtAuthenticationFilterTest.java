package com.tma.restaurant_exercise_phase_2.security.filters;

import com.tma.restaurant_exercise_phase_2.security.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain filterChain;

    @Mock private UserDetailsService userDetailsService;
    @Mock private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(userDetailsService, jwtService);
    }

    @Test
    void doFilterInternal_requestParamIsNull() {
        // when
        NullPointerException result = assertThrows(
                NullPointerException.class,
                () ->         jwtAuthenticationFilter.doFilterInternal(null, response, filterChain)
        );

        // then
        assertEquals("request is marked non-null but is null", result.getMessage());
    }

    @Test
    void doFilterInternal_responseParamIsNull() {
        // when
        NullPointerException result = assertThrows(
                NullPointerException.class,
                () ->         jwtAuthenticationFilter.doFilterInternal(request, null, filterChain)
        );

        // then
        assertEquals("response is marked non-null but is null", result.getMessage());
    }

    @Test
    void doFilterInternal_filterChainParamIsNull() {
        // when
        NullPointerException result = assertThrows(
                NullPointerException.class,
                () ->         jwtAuthenticationFilter.doFilterInternal(request, response, null)
        );

        // then
        assertEquals("filterChain is marked non-null but is null", result.getMessage());
    }

    @Test
    void doFilterInternal_nullToken() throws ServletException, IOException {
        // given
        when(request.getHeader("Authorization")).thenReturn(null);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verifyNoInteractions(jwtService);
        verifyNoInteractions(userDetailsService);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "Not a valid token"})
    void doFilterInternal_invalidTokenType(String token) throws ServletException, IOException {
        // given
        when(request.getHeader("Authorization")).thenReturn(token);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verifyNoInteractions(jwtService);
        verifyNoInteractions(userDetailsService);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_validTokenType_nullExtractedEmail() throws ServletException, IOException {
        // given
        String token = "Bearer token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtService.extractUserEmail(token)).thenReturn(null);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verifyNoInteractions(userDetailsService);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_validTokenType_NotNullEmail_invalidToken() throws ServletException, IOException {
        // given
        String token = "Bearer token";
        UserDetails userDetails = User.builder().username("Username").password("123456").build();

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtService.extractUserEmail(token)).thenReturn(null);
        when(userDetailsService.loadUserByUsername(Mockito.anyString())).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(false);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
