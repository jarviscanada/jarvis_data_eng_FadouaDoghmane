# Java Grep Application

## Introduction
This project implements a simplified version of the Unix `grep` command using Java. The application searches recursively through a directory, reads files line by line, and writes all lines matching a user-provided regular expression to an output file. The design follows a clear contract defined by the `JavaGrep` interface, encouraging clean separation of concerns and consistent implementations. The project uses Core Java (IO, Collections, Regex), SLF4J for logging, Java Streams and Lambdas (in the Lambda version), Maven for build management, IntelliJ IDEA as the IDE, and Docker for packaging and distribution.

## Quick Start
You can run the application either directly with Java or via Docker.

### Run with Java
1. Package the application:
```bash
mvn clean package
```
2. Run the JAR:
```bash
java -jar target/grep-<version>.jar <regex> <rootDir> <outFile>
```
Example:
```bash
java -jar target/grep.jar ".*Romeo.*Juliet.*" ./data ./log/grep.out
```

### Run with Docker
See the **Deployment** section below for full Docker instructions.

## Implementation

### Pseudocode
The `process` method represents the high-level workflow of the application:

```
matchedLines = []
for each file in listFiles(rootDir)
  for each line in readLines(file)
    if containsPattern(line)
      matchedLines.add(line)
writeToFile(matchedLines)
```

### Performance Issue
A potential performance issue is high memory usage when storing all matched lines in a list before writing them to a file, especially for large directories. This can be improved by streaming matched lines directly to the output file or by writing results incrementally per file. Using Java Streams with lazy evaluation can further reduce memory overhead.

## Test
Manual testing was performed by preparing sample directories with multiple text files containing known patterns. The application was run with different regex inputs, including edge cases such as no matches and invalid paths. Results were validated by comparing the output file contents with expected matches using a text editor and command-line tools like `grep`.

## Deployment
The application is dockerized for easy distribution and consistent runtime behavior.

### Docker Steps
```bash
cd core_java/grep

# Login to Docker Hub
docker_user=your_docker_id
docker login -u ${docker_user} --password-stdin

# Create Dockerfile
cat > Dockerfile << EOF
FROM openjdk:8-alpine
COPY target/grep*.jar /usr/local/app/grep/lib/grep.jar
ENTRYPOINT ["java","-jar","/usr/local/app/grep/lib/grep.jar"]
EOF

# Package the app
mvn clean package

# Build Docker image
docker build -t ${docker_user}/grep .

# Verify image
docker image ls | grep grep

# Run container
docker run --rm   -v `pwd`/data:/data -v `pwd`/log:/log   ${docker_user}/grep ".*Romeo.*Juliet.*" /data /log/grep.out

# Push image to Docker Hub
docker push ${docker_user}/grep
```

After pushing, verify the image on Docker Hub.

## Improvement
1. Add automated unit and integration tests using JUnit and Mockito.
2. Improve performance by streaming results directly to the output file instead of buffering all matches in memory.
3. Enhance error handling and validation (e.g., better CLI argument parsing and custom exceptions).

