#
# Build stage
#
FROM maven:3.8-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM openjdk:17
COPY --from=build /home/app/target/email-service-0.0.1-SNAPSHOT.jar /usr/local/lib/email-service.jar
EXPOSE 5003
ENTRYPOINT ["java","-jar","/usr/local/lib/email-service.jar"]