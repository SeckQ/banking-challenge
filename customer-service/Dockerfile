# Stage 1: Build
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# Stage 2: Runtime
FROM eclipse-temurin:17-jre
WORKDIR /app
VOLUME /tmp
EXPOSE 8081
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]