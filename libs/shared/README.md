# Shared Library

This library contains code shared between the frontend and backend of the AI Hackathon 2025 project.

## Structure

```
shared/
├── src/                    # Source code
│   ├── lib/                # Implementations
│   └── index.ts            # Entry point
└── project.json            # NX configuration
```

## Usage

### In the Frontend

```typescript
import { SharedService } from '@ai-hackathon/shared';

// Use the service
const service = new SharedService();
```

### In the Backend

To use this library in the backend, you'll need to configure a Maven project that depends on this library.

## Development

To develop this library:

```bash
# Build the library
nx build shared

# Run tests
nx test shared

# Run linter
nx lint shared
``` 