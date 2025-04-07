# Frontend - Angular

This is the frontend application for the AI Hackathon 2025 project, developed with Angular and NX.

## Requirements

- Node.js 18+
- NPM 9+
- NX CLI

## Installation

```bash
# Install dependencies
npm install

# Start development server
nx serve frontend
```

## Project Structure

```
frontend/
├── src/                    # Source code
│   ├── app/                # Main components
│   ├── assets/             # Static resources
│   ├── environments/       # Environment configurations
│   └── styles/             # Global styles
├── test/                   # Tests
└── angular.json            # Angular configuration
```

## NX Commands

- `nx serve frontend`: Starts the development server
- `nx build frontend`: Builds the application for production
- `nx test frontend`: Runs tests
- `nx lint frontend`: Runs the linter

## Backend Integration

The frontend application communicates with the backend through the REST API exposed at `http://localhost:8080/api`. 