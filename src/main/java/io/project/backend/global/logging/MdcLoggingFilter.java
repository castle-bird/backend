package io.project.backend.global.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class MdcLoggingFilter extends OncePerRequestFilter {

  private static final String REQUEST_ID_HEADER = "X-Request-Id";

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    String requestId = resolveRequestId(request);

    MDC.put("requestId", requestId);
    MDC.put("method", request.getMethod());
    MDC.put("uri", request.getRequestURI());
    MDC.put("clientIp", resolveClientIp(request));
    MDC.put("userId", "anonymous");

    response.setHeader(REQUEST_ID_HEADER, requestId);

    try {
      filterChain.doFilter(request, response);

    } finally {
      logByStatus(request, response);

      MDC.clear();
    }
  }

  private String resolveRequestId(HttpServletRequest request) {
    String requestId = request.getHeader(REQUEST_ID_HEADER);

    if (StringUtils.hasText(requestId)) {
      return requestId;
    }

    return UUID.randomUUID().toString();
  }

  private String resolveClientIp(HttpServletRequest request) {
    String forwardedFor = request.getHeader("X-Forwarded-For");

    if (StringUtils.hasText(forwardedFor)) {
      return forwardedFor.split(",")[0].trim();
    }

    String realIp = request.getHeader("X-Real-IP");

    if (StringUtils.hasText(realIp)) {
      return realIp;
    }

    return request.getRemoteAddr();
  }

  private void logByStatus(HttpServletRequest request, HttpServletResponse response) {
    int status = response.getStatus();

    if (status == HttpServletResponse.SC_UNAUTHORIZED) { // 401
      log.info("HTTP {} {} -> {}", request.getMethod(), request.getRequestURI(), status);
      return;
    }

    if (status == HttpServletResponse.SC_FORBIDDEN || status >= 500) { // 403, 5xx
      log.warn("HTTP {} {} -> {}", request.getMethod(), request.getRequestURI(), status);
      return;
    }

    log.info("HTTP {} {} -> {}", request.getMethod(), request.getRequestURI(), status);
  }
}
