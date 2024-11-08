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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

            AuthenticationResponse response = new AuthenticationResponse(1, "This is a token", "Username");
            when(authenticationService.authenticate(Mockito.any(AuthenticationRequest.class))).thenReturn(response);

            // when
            performAuthenticate(request)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
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

        @ParameterizedTest
        @ValueSource(strings = {"null", ""})
        void authenticate_passwordIsNullOrEmpty_return400(String password) throws Exception {
            // given
            AuthenticationRequest request = new AuthenticationRequest();
            request.setEmail("username@gmail.com");

            if (!password.equals("null")) request.setPassword(password.toCharArray());

            // when
            performAuthenticate(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isString())
                    .andExpect(jsonPath("$").value("password CANNOT BE BLANK"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"null", ""})
        void authenticate_emailIsNullOrEmpty_return400(String email) throws Exception {
            // given
            AuthenticationRequest request = new AuthenticationRequest();
            request.setPassword("123456".toCharArray());

            if (!email.equals("null")) request.setEmail(email);

            // when
            performAuthenticate(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isString())
                    .andExpect(jsonPath("$").value("email CANNOT BE BLANK"));
        }

        @Test
        void authenticate_emailIsInvalid_return400() throws Exception {
            // given
            AuthenticationRequest request = new AuthenticationRequest();
            request.setEmail("InvalidEmail");
            request.setPassword("123456".toCharArray());

            // when
            performAuthenticate(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isString())
                    .andExpect(jsonPath("$").value("email IS NOT IN APPROPRIATE FORM"));
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
            request.setPassword("123456".toCharArray());

            AuthenticationResponse response = new AuthenticationResponse(1, "This is a token", "Username");
            when(authenticationService.register(Mockito.any(RegisterRequest.class))).thenReturn(response);

            // when
            performRegister(request)
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.accessToken").value("This is a token"))
                    .andExpect(jsonPath("$.username").value("Username"));
        }

        @Test
        void register_existedEmail_return400() throws Exception {
            // given
            RegisterRequest request = new RegisterRequest();
            request.setUsername("Username");
            request.setEmail("username@gmail.com");
            request.setPassword("123456".toCharArray());

            when(authenticationService.register(Mockito.any(RegisterRequest.class)))
                    .thenThrow(new UserAlreadyExistedException("USER HAS ALREADY EXISTED WITH EMAIL: " + request.getEmail()));

            // when
            performRegister(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").value("USER HAS ALREADY EXISTED WITH EMAIL: username@gmail.com"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"null", ""})
        void register_usernameIsNullOrEmpty_return400(String username) throws Exception {
            // given
            RegisterRequest request = new RegisterRequest();
            request.setEmail("username@gmail.com");
            request.setPassword("123456".toCharArray());

            if (!username.equals("null")) request.setUsername(username);

            // when
            performRegister(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").value("username CANNOT BE BLANK"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"null", ""})
        void register_passwordIsNullOrEmpty_return400(String password) throws Exception {
            // given
            RegisterRequest request = new RegisterRequest();
            request.setEmail("username@gmail.com");
            request.setUsername("username");

            if (!password.equals("null")) request.setPassword(password.toCharArray());

            // when
            performRegister(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").value("password CANNOT BE BLANK"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"null", ""})
        void register_emailIsNullOrEmpty_return400(String email) throws Exception {
            // given
            RegisterRequest request = new RegisterRequest();
            request.setPassword("123456".toCharArray());
            request.setUsername("username");

            if (!email.equals("null")) request.setEmail(email);

            // when
            performRegister(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").value("email CANNOT BE BLANK"));
        }

        @Test
        void register_emailIsInvalid_return400() throws Exception {
            // given
            RegisterRequest request = new RegisterRequest();
            request.setPassword("123456".toCharArray());
            request.setUsername("username");
            request.setEmail("InvalidEmail");

            // when
            performRegister(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").value("email IS NOT IN APPROPRIATE FORM"));
        }
    }

}
