**# Backend - Spring Boot

This is the backend application for the AI Hackathon 2025 project, developed with Spring Boot and Spring AI.

## Requirements

- Java 17+
- Maven 3.8+

## Installation

```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

## Project Structure

```
backend/
├── src/                    # Source code
│   ├── main/               # Main code
│   │   ├── java/           # Java code
│   │   └── resources/      # Resources (configuration, etc.)
│   └── test/               # Tests
└── pom.xml                 # Maven configuration
```

## REST API

The application exposes the following endpoints:

- `POST /api/chat`: Endpoint to interact with the chatbot

## Configuration

The application configuration is located in `src/main/resources/application.yml`.

To configure the OpenAI API key, set the `OPENAI_API_KEY` environment variable:

```bash
export OPENAI_API_KEY=your-api-key
```** 
