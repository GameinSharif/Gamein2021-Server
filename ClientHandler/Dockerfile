FROM openjdk:11

WORKDIR /opt/app

ARG JAR_FILE=target/client-handler-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

COPY src/main/resources/application-production.properties application.properties

ENTRYPOINT ["java", "-jar", "app.jar"]
