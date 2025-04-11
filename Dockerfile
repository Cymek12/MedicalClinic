FROM openjdk:23-jdk-slim
WORKDIR /app
COPY target/Medical-clinic-0.0.1-SNAPSHOT.jar Medical-clinic-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "Medical-clinic-0.0.1-SNAPSHOT.jar"]