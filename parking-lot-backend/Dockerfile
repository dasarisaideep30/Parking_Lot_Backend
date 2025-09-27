# --- Stage 1: Build the application ---
FROM maven:3.8.5-openjdk-17 AS builder

# Set the working directory
WORKDIR /app

# Copy the entire project into the container
# This works because the Dockerfile is in the same folder as pom.xml
COPY . .

# Run the Maven build command
RUN mvn clean package -DskipTests


# --- Stage 2: Create the final, lightweight image ---
FROM openjdk:17-slim

# Set the working directory
WORKDIR /app

# Copy ONLY the built .jar file from the 'builder' stage
# The path is now simpler
COPY --from=builder /app/target/parking-lot-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# The command to execute when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]