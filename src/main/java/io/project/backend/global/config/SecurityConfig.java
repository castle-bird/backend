package io.project.backend.global.config;

import io.project.backend.global.security.jwt.JwtAuthenticationFilter;
import io.project.backend.global.security.jwt.JwtProperties;
import io.project.backend.global.security.jwt.PasswordChangeRequiredFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final PasswordChangeRequiredFilter passwordChangeRequiredFilter;
  private final Environment environment;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(authorizeCustomizer())
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(sessionManagementCustomizer())
        .cors(corsCustomizer())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(passwordChangeRequiredFilter,JwtAuthenticationFilter.class) // → 인증 완료 후, 최초 비번 변경했는지 체크
        .build();
  }

  // 권한 경로 설정
  private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeCustomizer() {
    return auth -> {

      if (environment.acceptsProfiles(Profiles.of("dev"))) {
        auth.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll();
      }

      auth
          .requestMatchers(HttpMethod.POST, "/auth/signup").hasRole("ADMIN")
          .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
          .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll()
          .requestMatchers(HttpMethod.POST, "/auth/logout").authenticated()
          .anyRequest().authenticated();
    };
  }

  // CORS
  private Customizer<CorsConfigurer<HttpSecurity>> corsCustomizer() {
    CorsConfiguration config = new CorsConfiguration();

    if (environment.acceptsProfiles(Profiles.of("dev"))) {
      config.setAllowedOrigins(List.of(
          "http://localhost:8080",   // 서버 확인용
          "http://localhost:5173"    // React (Vite)
      ));
    }

    config.setAllowedMethods(List.of("*"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return cors -> cors.configurationSource(source);
  }

  // Session
  private Customizer<SessionManagementConfigurer<HttpSecurity>> sessionManagementCustomizer() {
    return session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
