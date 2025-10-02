# ============================================
# Stage 1: 빌드 스테이지
# ============================================
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Gradle Wrapper 파일들을 먼저 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Gradle Wrapper에 실행 권한 부여
RUN chmod +x ./gradlew

# 의존성 다운로드 (캐싱)
RUN ./gradlew dependencies --no-daemon || true

# 소스코드 전체 복사
COPY src src

# JAR 파일 빌드
RUN ./gradlew clean build -x test --no-daemon

# ============================================
# Stage 2: 실행 스테이지
# ============================================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Stage 1에서 빌드된 JAR 파일만 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 환경변수 기본값 설정
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080

# 포트 노출
EXPOSE 8080

# 컨테이너 시작 시 실행할 명령어
ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]
