FROM openjdk:17

COPY target/DockerElastic.jar DockerElastic.jar

ENTRYPOINT ["java", "-jar", "/DockerElastic.jar"]