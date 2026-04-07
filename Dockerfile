FROM amazoncorretto:17 AS builder
WORKDIR /app

# 의존성 레이어 캐싱 최적화
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x ./gradlew && \
    ./gradlew build -x test --stacktrace || true

# 소스 코드 복사 및 빌드
COPY . .
RUN ./gradlew clean build -x test

# (Alpine으로 경량화)
FROM amazoncorretto:17-alpine
WORKDIR /app

# Non-root 사용자 생성 (보안)
RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser

# JAR 복사 및 권한 설정
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar
RUN chown -R appuser:appuser /app

USER appuser

# 포트 설정
EXPOSE 8080

# 환경변수 설정
# JAVA_OPTS: G1GC 및 메모리 할당 비율 설정
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=25.0"
ENV TZ=Asia/Seoul
ENV SPRING_PROFILES_ACTIVE=prod

# 헬스체크 (ECS/ALB 자동 재시작)
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD wget -q -O- http://localhost:8080/actuator/health || exit 1

# 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTS} -Duser.timezone=Asia/Seoul -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar app.jar"]
