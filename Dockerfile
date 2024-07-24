FROM gradle:8.8-jdk17-jammy as builder

WORKDIR /app
COPY ./ ./
RUN gradle clean bootjar

FROM openjdk:17.0-slim

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
USER nobody
ENTRYPOINT ["java", "-jar", "Dspring.profiles.active=local", "app.jar"]