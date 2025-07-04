# build stage
FROM openjdk:24-jdk-slim AS builder
WORKDIR /app

RUN apt-get update \
 && apt-get install -y findutils \
 && rm -rf /var/lib/apt/lists/*

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

COPY src src

RUN chmod +x gradlew \
 && ./gradlew clean bootJar --no-daemon

# runtime stage
FROM openjdk:24-jdk
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
