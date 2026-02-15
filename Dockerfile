FROM gradle:9.3.1-jdk25-alpine AS build

WORKDIR /app

COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts gradlew ./

RUN chmod +x gradlew
RUN ./gradlew shadowJar --no-daemon

COPY src src

RUN gradle shadowJar --no-daemon

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup \
    && mkdir -p /app/logs \
    && chown appuser:appgroup /app/logs

COPY --from=build /app/build/libs/*-all.jar app.jar

USER appuser

ENTRYPOINT ["java", "-DLOG_DIR=/app/logs", "-jar", "app.jar"]
