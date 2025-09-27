# --- Stage 1: Build the application ---
# Use an official Maven image to build the project
FROM maven:3.8.5-openjdk-17 AS builder

# Set the working directory
WORKDIR /app

# Copy the entire project into the container
COPY . .

# Run the Maven build command to create the .jar file
RUN mvn clean package -DskipTests


# --- Stage 2: Create the final, lightweight image ---
# Use a lightweight Java image for the final container
FROM openjdk:17-slim

# Set the working directory
WORKDIR /app

# Copy ONLY the built .jar file from the 'builder' stage
COPY --from=builder /app/target/parkinglotbackend-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# The command to execute when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]