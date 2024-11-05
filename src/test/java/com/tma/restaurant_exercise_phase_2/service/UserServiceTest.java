package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.exceptions.UserAlreadyExistedException;
import com.tma.restaurant_exercise_phase_2.exceptions.UserNotFoundException;
import com.tma.restaurant_exercise_phase_2.model.User;
import com.tma.restaurant_exercise_phase_2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {
    private UserService userService;

    @Mock private UserRepository userRepository;

    User expected;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.userService = new UserService(userRepository);
        expected = User.builder()
                .id(1)
                .username("Username")
                .email("username@gmail.com")
                .build();
    }

    @Test
    void getUserByEmail_success() {
        // given
        when(userRepository.getUserByEmail("Username")).thenReturn(Optional.of(expected));

        // when
        User actual = userService.getUserByEmail("Username");

        // then
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    void getUserByEmail_notFound() {
        // given
        String notFoundEmail = "Not Found Email";
        String expectedMessage = "NO USER FOUND WITH EMAIL: " + notFoundEmail;

        // when
        UserNotFoundException actual = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserByEmail(notFoundEmail)
        );

        // then
        assertEquals(expectedMessage, actual.getMessage());
    }

    @Test
    void addUser_success() {
        // given
        User newUser = User.builder()
                .id(2)
                .username("New Username")
                .email("newusername@gmail.com")
                .build();

        // when
        userService.addUser(newUser);

        //then
        verify(userRepository).getUserByEmail(newUser.getEmail());
        verify(userRepository).save(newUser);
    }

    @Test
    void addUser_existedEmail() {
        // given
        String existedEmail = "username@gmail.com";
        String expectedMessage = "USER HAS ALREADY EXISTED WITH EMAIL: username@gmail.com";
        User newUser = User.builder()
                .id(2)
                .username("New Username")
                .email(existedEmail)
                .build();
        when(userRepository.getUserByEmail(existedEmail)).thenReturn(Optional.of(expected));

        // when
        UserAlreadyExistedException actual = assertThrows(
                UserAlreadyExistedException.class,
                () -> userService.addUser(newUser)
        );

        // then
        assertEquals(expectedMessage, actual.getMessage());
    }
}
