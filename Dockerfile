FROM openjdk:23-jdk-slim
WORKDIR /app
COPY target/medical-clinic-0.0.1-SNAPSHOT.jar medical-clinic.jar
ENTRYPOINT ["java", "-jar", "medical-clinic.jar"]