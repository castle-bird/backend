package io.project.backend.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.project.backend.domain.auth.exception.ExpiredTokenException;
import io.project.backend.domain.auth.exception.InvalidTokenException;
import io.project.backend.domain.employee.entity.Employee;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  private final JwtProperties jwtProperties;
  private final SecretKey signKey;

  public JwtProvider(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;

    byte[] keyBytes = jwtProperties.secretKey().getBytes(StandardCharsets.UTF_8);
    this.signKey = Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateAccessToken(Employee employee) {
    return generateToken(employee, TokenType.ACCESS_TOKEN);
  }

  public String generateRefreshToken(Employee employee) {
    return generateToken(employee, TokenType.REFRESH_TOKEN);
  }

  public boolean validateToken(String token) {
    try {
      extractClaims(token);
      return true;
    } catch (ExpiredTokenException | InvalidTokenException e) {
      return false;
    }
  }

  public Long extractUserId(String token) {
    return Long.valueOf(extractClaims(token).getSubject());
  }

  public String extractRole(String token) {
    return extractClaims(token).get("role").toString();
  }

  public Date extractExpiration(String token) {
    return extractClaims(token).getExpiration();
  }

  public Claims extractClaims(String token) {
    // jjwt 0.13 기준
    // 파싱 자체만으로 서명 검증이 되기때문에 서명검증은 따로 해주지 않는다.
    try {
      return Jwts.parser()
          .verifyWith(signKey)
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (ExpiredJwtException e) {
      throw new ExpiredTokenException(
          Map.of("token", "만료된 토큰입니다.")
      );
    } catch (JwtException | IllegalArgumentException e) {
      throw new InvalidTokenException(
          Map.of("token", "유효하지 않은 토큰입니다.")
      );
    }
  }

  /**
   * 토큰 유형에 따라 해당 직원에 대한 JWT 토큰을 생성합니다.
   *
   * @param employee  토큰을 생성할 대상 직원
   * @param tokenType 생성할 토큰의 유형 (ACCESS_TOKEN 또는 REFRESH_TOKEN)
   * @return 인코딩된 클레임과 서명을 포함하는 JWT 토큰 문자열
   */
  private String generateToken(Employee employee, TokenType tokenType) {
    String subject = String.valueOf(employee.getId());
    String tokenId = UUID.randomUUID().toString();
    String issuer = jwtProperties.jwtIssuer();

    // 토큰 유형에 따라 만료 시간 다르게 설정
    long tokenExpiration = tokenType == TokenType.ACCESS_TOKEN
        ? jwtProperties.accessTokenExpiration()
        : jwtProperties.refreshTokenExpiration();

    Instant now = Instant.now();
    Date issuedAt = Date.from(now);
    Date expiration = Date.from(now.plusMillis(tokenExpiration));

    JwtBuilder builder = Jwts.builder()
        .header()
        .add("typ", "JWT")
        .and()
        .subject(subject)
        .id(tokenId)
        .claim("type", tokenType.getValue())
        .issuer(issuer)
        .issuedAt(issuedAt)
        .expiration(expiration)
        .signWith(signKey);

    // Access Token에만 권한 정보 포함 (Refresh Token은 최소한의 정보만 담음)
    if (tokenType == TokenType.ACCESS_TOKEN) {
      builder.claim("role", employee.getRole().name());
    }

    return builder.compact();
  }
}
