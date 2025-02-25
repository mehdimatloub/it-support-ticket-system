# Use an official OpenJDK 17 image as a base
FROM openjdk:17-jdk-slim

# Create a directory for the application
WORKDIR /app

# Copy the built JAR file from the target folder to the container
COPY target/it-support-ticket-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 (or the port your app uses)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
