# 1단계: 빌드
FROM gradle:8.4-jdk17 AS build
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle bootJar

# 2단계: 실행
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]