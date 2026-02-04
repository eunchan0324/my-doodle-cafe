# 1단계: 빌드 스테이지
FROM gradle:7.6-jdk17 AS build
WORKDIR /app
COPY . .

# [추가] gradlew 파일에 실행 권한을 부여합니다.
RUN chmod +x gradlew

# 이제 권한 에러 없이 빌드가 가능합니다.
RUN ./gradlew clean build -x test

# 2단계: 실행 스테이지
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
# -Dspring.profiles.active=prod 옵션을 추가하여 컨테이너 실행 시 항상 운영 모드로 동작하게 합니다.
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]