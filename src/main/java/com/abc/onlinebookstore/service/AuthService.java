package com.abc.onlinebookstore.service;

import com.abc.onlinebookstore.dto.LoginRequestDTO;
import com.abc.onlinebookstore.dto.LoginResponseDTO;
import com.abc.onlinebookstore.dto.RegisterRequestDTO;

public interface AuthService {

    void register(RegisterRequestDTO request);

    LoginResponseDTO login(LoginRequestDTO request);
}