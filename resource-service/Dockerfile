FROM alpine:3.18

ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk
ENV MAVEN_HOME=/usr/share/maven

RUN apk add --no-cache openjdk17 maven

ENV PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH

LABEL maintainer="Your Name <your.email@example.com>"
LABEL version="1.0"
LABEL description="Dockerfile for JDK 17 and Maven"

WORKDIR /app

COPY . /app

RUN mvn clean package -DskipTests

EXPOSE 8081

ENTRYPOINT ["java","-jar","target/resource-service-0.0.1-SNAPSHOT.jar"]
