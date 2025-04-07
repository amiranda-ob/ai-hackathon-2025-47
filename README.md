# AI Hackathon 2025 - Monorepo

This is a monorepo project that integrates:
- Backend: Spring Boot with Spring AI
- Frontend: Angular with NX
- Infrastructure: Common configuration

## Project Structure

```
/
├── apps/                  # Final applications
│   ├── backend/           # Spring Boot application
│   └── frontend/          # Angular application
├── libs/                  # Shared libraries
│   ├── shared/            # Code shared between frontend and backend
│   └── ui/                # Reusable UI components
├── tools/                 # Tools and scripts
└── nx.json                # NX configuration
```

## Requirements

- Java 17+
- Node.js 18+
- NPM 9+
- NX CLI

## Installation

### Backend (Spring Boot)

```bash
cd apps/backend
./mvnw spring-boot:run
```

### Frontend (Angular)

```bash
npm install
nx serve frontend
```

## Development

This monorepo uses NX to manage the development of multiple applications and libraries in a single repository.

### NX Commands

- `nx serve frontend`: Starts the development server for the Angular application
- `nx build frontend`: Builds the Angular application for production
- `nx test frontend`: Runs tests for the Angular application
- `nx lint frontend`: Runs the linter on the Angular application

## License

[MIT](https://choosealicense.com/licenses/mit/)

# AI Chatbot

## Prerequisites

### Ollama
For the application to work correctly, you need to have Ollama installed and running on your system. You can verify if Ollama is running and which model is in use with the following command:

```bash
ollama ps
```

This command will show you the models that are currently running. The application is configured to use the `llama2` model by default.

## Setup

### Backend (Spring Boot)

1. Navigate to the backend directory:
   ```bash
   cd apps/backend
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will run on `http://localhost:8080`

### Frontend (React)

1. Navigate to the frontend directory:
   ```bash
   cd apps/frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Run the application in development mode:
   ```bash
   npm run dev
   ```

The application will run on `http://localhost:5173`

## Project Structure

```
.
├── apps/
│   ├── backend/           # Spring Boot application
│   └── frontend/          # React application
└── README.md
```

## Technologies Used

- Backend: Spring Boot, Spring AI
- Frontend: React, TypeScript, Tailwind CSS
- Database: SQLite
- AI Model: Llama2 (via Ollama) 