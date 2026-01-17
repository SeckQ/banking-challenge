FROM eclipse-temurin:17-jdk
VOLUME /tmp
EXPOSE 8081
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]