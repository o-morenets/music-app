#FROM alpine:3.18
#ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk
#ENV MAVEN_HOME=/usr/share/maven
#RUN apk add --no-cache openjdk17 maven
#ENV PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH
#WORKDIR /app
#COPY . /app
#RUN mvn clean package -DskipTests
#EXPOSE 8081
#ENTRYPOINT ["java","-jar","target/resource-service-0.0.1-SNAPSHOT.jar"]

FROM openjdk:17
EXPOSE 8081
COPY target/resource-service-0.0.1-SNAPSHOT.jar resource-service.jar
ENTRYPOINT ["java","-jar","/resource-service.jar"]
