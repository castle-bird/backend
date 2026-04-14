package io.project.backend.global.security.jwt;

import io.project.backend.global.security.details.UserDetailsImpl;
import io.project.backend.global.security.details.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final String BEARER_PREFIX = "Bearer ";

  private final JwtProvider jwtProvider;
  private final UserDetailsServiceImpl userDetailsService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    try {
      // 헤더 추출
      String authorizationHeader = request.getHeader("Authorization");

      // 헤더 유효성 검사
      if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
        // 유효하지 않은 헤더인 경우, 다음 필터로 넘어감 → 마지막 필터인 Authorization에서 인증 실패 처리
        filterChain.doFilter(request, response);
        return;
      }

      // Access Token 추출
      String accessToken = authorizationHeader.substring(BEARER_PREFIX.length());

      // 토큰 유효성 검사
      if (!jwtProvider.validateToken(accessToken)) {
        filterChain.doFilter(request, response);
        return;
      }

      // 내용 추출
      String email = jwtProvider.extractClaims(accessToken).getSubject();
      log.info("JWT 인증 성공. 이메일: {}", email);
      UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);

      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          userDetails, // Principal
          null, // 인증이 완료되었으니 비밀번호는 null로 설정
          userDetails.getAuthorities() // 권한 정보
      );

      // SecurityContext에 Authentication 객체 저장
      authenticationToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request) // → 클라이언트 요청에 대한 부가 정보를 추가한다. IP 등등
      );
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    } catch (Exception ex) {
      log.error("JWT 인증 처리 중 예외가 발생했습니다.", ex);

    }

    filterChain.doFilter(request, response);
  }
}
