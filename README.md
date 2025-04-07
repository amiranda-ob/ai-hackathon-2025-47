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