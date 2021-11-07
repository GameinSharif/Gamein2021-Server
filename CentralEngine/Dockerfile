FROM openjdk:11

WORKDIR /opt/app

ARG JAR_FILE=CentralEngine/target/central-engine-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

COPY CentralEngine/src/main/resources/application-production.properties application.properties

COPY Core/src/main/resources/JsonFiles JsonFiles/

COPY Core/src/main/resources/CSVFiles CSVFiles/

ENTRYPOINT ["java","-jar","app.jar"]

