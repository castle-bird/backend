package io.project.backend.domain.auth.service;

import io.project.backend.domain.auth.dto.common.AuthTokenDto;
import io.project.backend.domain.auth.dto.request.LoginRequest;
import io.project.backend.domain.auth.dto.request.SignupRequest;
import io.project.backend.domain.auth.dto.common.LoginDto;
import io.project.backend.domain.auth.dto.response.SignupResponse;

public interface AuthService {

  SignupResponse createEmployee(SignupRequest request);

  LoginDto login(LoginRequest request);

  AuthTokenDto refreshToken(String refreshToken);

  void logout(Long userId, String refreshToken);
}
