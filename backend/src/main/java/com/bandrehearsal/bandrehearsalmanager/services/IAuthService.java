package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.dto.AuthResponse;
import com.bandrehearsal.bandrehearsalmanager.dto.LoginRequest;
import com.bandrehearsal.bandrehearsalmanager.dto.RegisterRequest;

public interface IAuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(String refreshToken);
    void logout(String refreshToken);
}
