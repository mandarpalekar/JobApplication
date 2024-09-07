# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory in the build stage
WORKDIR /app

# Copy the pom.xml and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim

# Set the working directory in the runtime stage
WORKDIR /app

# Copy the jar file from the build stage to the runtime stage
COPY --from=build /app/target/jobapp-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
