package com.tma.restaurant_exercise_phase_2.controller;

import com.tma.restaurant_exercise_phase_2.exceptions.UserAlreadyExistedException;
import com.tma.restaurant_exercise_phase_2.security.models.AuthenticationRequest;
import com.tma.restaurant_exercise_phase_2.security.models.AuthenticationResponse;
import com.tma.restaurant_exercise_phase_2.security.models.RegisterRequest;
import com.tma.restaurant_exercise_phase_2.security.services.AuthenticationService;
import com.tma.restaurant_exercise_phase_2.security.services.JwtService;
import com.tma.restaurant_exercise_phase_2.utils.JsonUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean private AuthenticationService authenticationService;
    @MockBean private JwtService jwtService;

    @Nested
    class Authenticate {
        private ResultActions performAuthenticate(AuthenticationRequest request) throws Exception {
            return mockMvc.perform(
                    MockMvcRequestBuilders
                            .post("/auth/authenticate")
                            .content(JsonUtils.writeJsonString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            );
        }

        @Test
        void authenticate_success_return200() throws Exception {
            // given
            AuthenticationRequest request = new AuthenticationRequest();
            request.setEmail("username@gmail.com");
            request.setPassword(new char[] {1, 2, 3, 4, 5, 6});

            AuthenticationResponse response = new AuthenticationResponse("This is a token", "Username");
            when(authenticationService.authenticate(Mockito.any(AuthenticationRequest.class))).thenReturn(response);

            // when
            performAuthenticate(request)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").isString())
                    .andExpect(jsonPath("$.username").value("Username"));
        }

        @Test
        void authenticate_wrongPassword_return401() throws Exception {
            // given
            AuthenticationRequest request = new AuthenticationRequest();
            request.setEmail("username@gmail.com");
            request.setPassword(new char[] {1, 2, 3, 4, 5, 6});

            when(authenticationService.authenticate(Mockito.any(AuthenticationRequest.class))).thenThrow(new BadCredentialsException("Bad credentials"));

            // when
            performAuthenticate(request)
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$").isString())
                    .andExpect(jsonPath("$").value("Bad credentials"));
        }

        @Test
        void authenticate_notFoundEmail_return401() throws Exception {
            // given
            AuthenticationRequest request = new AuthenticationRequest();
            request.setEmail("username@gmail.com");
            request.setPassword(new char[] {1, 2, 3, 4, 5, 6});

            when(authenticationService.authenticate(Mockito.any(AuthenticationRequest.class)))
                    .thenThrow(new InternalAuthenticationServiceException("NO USER FOUND WITH EMAIL: username@gmail.com"));

            // when
            performAuthenticate(request)
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$").isString())
                    .andExpect(jsonPath("$").value("NO USER FOUND WITH EMAIL: username@gmail.com"));
        }
    }

    @Nested
    class Register {
        private ResultActions performRegister(RegisterRequest request) throws Exception {
            return mockMvc.perform(
                    MockMvcRequestBuilders
                            .post("/auth/register")
                            .content(JsonUtils.writeJsonString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            );
        }

        @Test
        void register_success_return201() throws Exception {
            // given
            RegisterRequest request = new RegisterRequest();
            request.setUsername("Username");
            request.setEmail("username@gmail.com");

            AuthenticationResponse response = new AuthenticationResponse("This is a token", "Username");
            when(authenticationService.register(Mockito.any(RegisterRequest.class))).thenReturn(response);

            // when
            performRegister(request)
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.accessToken").value("This is a token"))
                    .andExpect(jsonPath("$.username").value("Username"));
        }

        @Test
        void register_existedEmail_return400() throws Exception {
            // given
            RegisterRequest request = new RegisterRequest();
            request.setUsername("Username");
            request.setEmail("username@gmail.com");

            when(authenticationService.register(Mockito.any(RegisterRequest.class)))
                    .thenThrow(new UserAlreadyExistedException("USER HAS ALREADY EXISTED WITH EMAIL: " + request.getEmail()));

            // when
            performRegister(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").value("USER HAS ALREADY EXISTED WITH EMAIL: username@gmail.com"));
        }
    }

}
