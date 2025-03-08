# Use OpenJDK 8 as the base image
FROM openjdk:8-jdk

# Set working directory inside the container
WORKDIR /app

# Create necessary directories
RUN mkdir -p /app/src /app/out /app/META-INF

# Copy the entire src directory into the container
COPY src /app/src/

# Find all Java files and compile them
RUN find /app/src -name "*.java"  | xargs javac -source 8 -target 8 -d /app/out

# Create the MANIFEST.MF file with the Main-Class entry
RUN echo "Main-Class: src.com.es2.designpatterns.Main" > /app/META-INF/MANIFEST.MF

# Package compiled files into a JAR and include the manifest
RUN jar cmf /app/META-INF/MANIFEST.MF /app/MyApp.jar -C /app/out .

# Specify the command to run the application
CMD ["java", "-jar", "/app/MyApp.jar"]
