package io.project.backend.domain.auth.service;

import io.project.backend.domain.auth.dto.request.SignupRequest;
import io.project.backend.domain.auth.dto.common.AuthTokenDto;

public interface AuthService {

  AuthTokenDto signup(SignupRequest request);
}
