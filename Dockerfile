FROM openjdk:17
WORKDIR /app
COPY target/airline-app.jar /app/airline-app.jar
EXPOSE 9000
CMD ["java", "-jar", "/app/target/airline-app.jar"]

# # Use an OpenJDK runtime as the base image
# FROM openjdk:17

# # Set the working directory inside the container
# WORKDIR /app
# # connection with mysql
# ARG MYSQL_HOST_WITH_PORT
# ARG MYSQL_DB_NAME
# ARG MYSQL_USER
# ARG MYSQL_PASSWORD
# ENV MYSQL_HOST_WITH_PORT=$MYSQL_HOST_WITH_PORT
# ENV MYSQL_DB_NAME=$MYSQL_DB_NAME
# ENV MYSQL_USER=$MYSQL_USER
# ENV MYSQL_PASSWORD=$MYSQL_PASSWORD

# # Copy the JAR file of the Spring Boot application from the builder image into the container
# COPY target/airline-app.jar /app/airline-app.jar

# # Expose the port that the Spring Boot application will run on
# EXPOSE 9000

# # Define the command to run the Spring Boot application when the container starts
# CMD ["java", "-jar", "/app/airline-app.jar"]

# ###############################################################################################

# # # Use Maven image as the builder image
# # FROM maven:latest AS builder
# #
# # # Set the working directory inside the container
# # WORKDIR /build
# #
# # #Copy Application from Github into container
# # ADD https://github.com/ashiphsayyad32/Airline-App-Java.git .
# #
# # # Copy the Maven project file (pom.xml) into the container
# # # COPY pom.xml .
# #
# # # Download the Maven dependencies
# # RUN mvn -B dependency:go-offline
# #
# # # Copy the source code into the container
# # # COPY src src
# #
# # # Build the application using Maven
# # RUN mvn -B clean install
# #
# # # Use an OpenJDK runtime as the base image
# # FROM openjdk17:alpine-jre
# #
# # # Set the working directory inside the container
# # WORKDIR /app
# #
# # # Copy the JAR file of the Spring Boot application from the builder image into the container
# # COPY --from=builder /build/target/airline-app.jar /app/airline-app.jar
# #
# # # Expose the port that the Spring Boot application will run on
# # EXPOSE 9000
# #
# # # Define the command to run the Spring Boot application when the container starts
# #CMD ["java", "-jar", "airline-app.jar"]

# #####################################################################################################
