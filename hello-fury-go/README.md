# Hello Fury (Go)

This example demonstrates using Apache Fury with Go.

## Prerequisites

- Go 1.16+

## Setup Instructions

> ⚠️ If this is your first time cloning the repo, run the following to initialize the module:
>
> ```bash
> go mod init hello-fury-go
> ```
>
> Otherwise, skip this step.

```bash
# Download dependencies
go mod tidy
```

## Build Instructions

```bash
# Build the executable
go build -o hello_fury
```

## Run Instructions

```bash
# Run without building
go run main.go

# Or run the built executable
./hello_fury
```

## Run Tests

```bash
go test
```
