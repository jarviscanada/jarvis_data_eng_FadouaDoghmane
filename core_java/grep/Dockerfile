# Java 8 runtime (small + maintained)
FROM amazoncorretto:8-alpine

# Copy the built JAR
COPY target/grep*.jar /usr/local/app/grep/lib/grep.jar

# Run the app
ENTRYPOINT ["java","-jar","/usr/local/app/grep/lib/grep.jar"]

