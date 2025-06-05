FROM gradle:8.14.0-jdk21 AS build
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts ./
COPY src ./src
RUN gradle shadowJar --no-daemon

FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*-all.jar /app/app.jar
COPY data.json /app/data.json

EXPOSE 8080

