#Start with a base image containing Java runtime
FROM openjdk:11-slim as build

#Information about who maintains the image
MAINTAINER junjie.ong2@cognizant

#Add the application's jar to the container
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

#Execute the application
ENTRYPOINT ["java", "-jar", "/accounts-0.0.1-SNAPSHOT.jar"]