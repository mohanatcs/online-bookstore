package com.abc.onlinebookstore.service;

import com.abc.onlinebookstore.dto.LoginRequestDTO;
import com.abc.onlinebookstore.dto.LoginResponseDTO;
import com.abc.onlinebookstore.exception.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.abc.onlinebookstore.dto.RegisterRequestDTO;
import com.abc.onlinebookstore.entity.UserEntity;
import com.abc.onlinebookstore.repository.UserRepository;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(RegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        UserEntity user = new UserEntity();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }
    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {

        Optional<UserEntity> userOptional =
                userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        UserEntity user = userOptional.get();

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new InvalidCredentialsException("Invalid email or password");
        }

        return new LoginResponseDTO(
                "Login successful",
                user.getUsername());
    }
}

