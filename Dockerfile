FROM maven AS build
COPY src /home/app/src
COPY pom.xml /home/app/
RUN mvn -f /home/app clean package

FROM openjdk
COPY --from=build /home/app/target/DockerElastic.jar /usr/local/lib/DockerElastic.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/DockerElastic.jar"]