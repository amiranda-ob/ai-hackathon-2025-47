# Configuration

This directory contains configuration classes for the AI Chatbot application.

## OpenAiConfig

The `OpenAiConfig` class configures the OpenAI API client. It creates a bean of type `OpenAiApi` that is used by the `OpenAiChatClient` service to communicate with the OpenAI API.

## Environment Variables

The following environment variables can be used to configure the application:

- `OPENAI_API_KEY`: The API key for OpenAI (required)
- `SPRING_PROFILES_ACTIVE`: The Spring profile to use (default: dev)

## Profiles

The application supports the following profiles:

- `dev`: Development profile with debug logging
- `prod`: Production profile with minimal logging

## Configuration Files

The application uses the following configuration files:

- `application.yml`: Base configuration
- `application-dev.yml`: Development configuration
- `application-prod.yml`: Production configuration

## Example Usage

```bash
# Run with development profile
export OPENAI_API_KEY=your-api-key
./mvnw spring-boot:run

# Run with production profile
export OPENAI_API_KEY=your-api-key
export SPRING_PROFILES_ACTIVE=prod
./mvnw spring-boot:run
``` 