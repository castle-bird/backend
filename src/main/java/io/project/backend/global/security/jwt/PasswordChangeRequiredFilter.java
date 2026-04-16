package io.project.backend.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.project.backend.global.exception.ErrorCode;
import io.project.backend.global.response.ErrorResponse;
import io.project.backend.global.security.details.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class PasswordChangeRequiredFilter extends OncePerRequestFilter {

  private static final String PASSWORD_CHANGE_PATH = "/auth/password";

  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl userDetails) {

      if (!userDetails.isCredentialsNonExpired()
          && !request.getRequestURI().equals(PASSWORD_CHANGE_PATH)
      ) {

        ErrorResponse errorResponse = ErrorResponse.of(
            ErrorCode.PASSWORD_CHANGE_REQUIRED,
            request.getRequestURI()
        );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

        return;
      }
    }

    filterChain.doFilter(request, response);
  }

}
