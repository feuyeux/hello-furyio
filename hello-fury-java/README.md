# Hello Fury (Java)

This example demonstrates using Apache Fury with Java.

## Prerequisites

- Java JDK 8+
- Maven 3.5+

## Build Instructions

```bash
# Build the project
mvn clean package
```

## Run Instructions

```bash
# Recommended: Run the main class
mvn exec:java -Dexec.mainClass="org.feuyeux.io.App"

# Or use the build script (also builds and runs)
./build.sh run
```

## Run Tests

```bash
# Run the tests
mvn test
```
