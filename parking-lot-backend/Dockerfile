# Use a lightweight official Java 17 image
FROM openjdk:17-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled .jar file into the container
COPY target/parkinglotbackend-0.0.1-SNAPSHOT.jar app.jar

# The command to execute when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]