# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
ENV APP_HOME=/usr/app
WORKDIR ${APP_HOME}
COPY pom.xml ${APP_HOME}/.
COPY src ${APP_HOME}/src
RUN mvn clean package -DskipTests

# Package stage
FROM eclipse-temurin:17-jre
ARG APP_NAME
ENV ARTIFACT_NAME=${APP_NAME}-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/usr/app
WORKDIR ${APP_HOME}
COPY --from=build ${APP_HOME}/target/${ARTIFACT_NAME} .
EXPOSE 8082
ENTRYPOINT exec java -jar -Dspring.profiles.active=${ENV} ${ARTIFACT_NAME}
