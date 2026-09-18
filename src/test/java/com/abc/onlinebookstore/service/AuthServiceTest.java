package com.abc.onlinebookstore.service;

import com.abc.onlinebookstore.dto.LoginRequestDTO;
import com.abc.onlinebookstore.dto.LoginResponseDTO;
import com.abc.onlinebookstore.dto.RegisterRequestDTO;
import com.abc.onlinebookstore.entity.UserEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.abc.onlinebookstore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldRegisterUserSuccessfully() {

        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("mohana");
        request.setEmail("mohana@gmail.com");
        request.setPassword("password123");

        when(userRepository.existsByEmail("mohana@gmail.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        authService.register(request);

        ArgumentCaptor<UserEntity> captor =
                ArgumentCaptor.forClass(UserEntity.class);

        verify(userRepository).save(captor.capture());

        UserEntity savedUser = captor.getValue();

        assertEquals("mohana", savedUser.getUsername());
        assertEquals("mohana@gmail.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
    }
    @Test
    void shouldLoginUserSuccessfully() {

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("mohana@gmail.com");
        request.setPassword("password123");

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("mohana");
        user.setEmail("mohana@gmail.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("mohana@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password123", "encodedPassword"))
                .thenReturn(true);

        LoginResponseDTO response = authService.login(request);

        assertEquals("Login successful", response.getMessage());
        assertEquals("mohana", response.getUsername());
    }
}