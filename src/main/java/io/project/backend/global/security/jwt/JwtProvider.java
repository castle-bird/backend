package io.project.backend.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.project.backend.domain.employee.entity.Employee;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
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
    return false;
  }

  public Long extractUserId(String token) {
    return null;
  }

  public String extractRole(String token) {
    return null;
  }

  public Date extractExpiration(String token) {
    return null;
  }

  Claims extractClaims(String token) {
    return null;
  }

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
