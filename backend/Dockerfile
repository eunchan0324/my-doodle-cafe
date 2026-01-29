# 1단계: 빌드 스테이지 (Java 코드를 실행 가능한 파일로 변환)
FROM gradle:7.6-jdk17 AS build
WORKDIR /app
COPY . .
# 테스트는 제외하고 빌드하여 시간을 단축합니다.
RUN ./gradlew clean build -x test

# 2단계: 실행 스테이지 (가벼운 환경에서 앱 실행)
FROM openjdk:17-jdk-slim
WORKDIR /app
# 빌드 스테이지에서 생성된 jar 파일만 가져옵니다.
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]