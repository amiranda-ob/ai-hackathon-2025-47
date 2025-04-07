# UI Components Library

This library contains reusable UI components for the AI Hackathon 2025 project.

## Structure

```
ui/
├── src/                    # Source code
│   ├── lib/                # Components
│   │   ├── button/         # Button component
│   │   ├── card/           # Card component
│   │   └── ...             # Other components
│   └── index.ts            # Entry point
└── project.json            # NX configuration
```

## Usage

```typescript
import { Button, Card } from '@ai-hackathon/ui';

// Use the components
<Button variant="primary">Click here</Button>
<Card title="Title">Card content</Card>
```

## Development

To develop this library:

```bash
# Build the library
nx build ui

# Run tests
nx test ui

# Run linter
nx lint ui
```

## Storybook

This library uses Storybook to document and test components.

```bash
# Start Storybook
nx run ui:storybook
``` 