FROM maven AS build
COPY src /home/app/src
COPY pom.xml /home/app/
RUN mvn -f /home/app clean package

FROM openjdk
COPY --from=build target/DockerElastic.jar DockerElastic.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/DockerElastic.jar"]