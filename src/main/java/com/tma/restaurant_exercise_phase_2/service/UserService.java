package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.exceptions.UserAlreadyExistedException;
import com.tma.restaurant_exercise_phase_2.exceptions.UserNotFoundException;
import com.tma.restaurant_exercise_phase_2.model.User;
import com.tma.restaurant_exercise_phase_2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow(() -> new UserNotFoundException("NO USER FOUND WITH EMAIL: " + email));
    }

    public void addUser(User user) {
        Optional<User> optional = userRepository.getUserByEmail(user.getEmail());
        if (optional.isPresent()) throw new UserAlreadyExistedException("USER HAS ALREADY EXISTED WITH EMAIL: " + user.getEmail());

        userRepository.save(user);
    }
}
