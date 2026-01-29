# 1단계: 빌드 스테이지
FROM gradle:7.6-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

# 2단계: 실행 스테이지 (에러가 났던 부분을 수정했습니다)
# 기존 openjdk 대신 현재 표준으로 쓰이는 eclipse-temurin을 사용합니다.
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
# 빌드 스테이지에서 생성된 jar 파일만 가져옵니다.
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]