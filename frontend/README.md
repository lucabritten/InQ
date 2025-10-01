# InQ Frontend

React + TypeScript frontend for the InQ Event Ticketing System.

## Features

- **Event Management**: Create, view, edit, and delete events
- **User Management**: Manage users and view their ticket history
- **Ticket Management**: Generate tickets with QR codes, view details, and download PDFs
- **QR Code Scanner**: Validate tickets by scanning QR codes using device camera or manual entry

## Tech Stack

- **React 19** with **TypeScript**
- **Vite** for build tooling
- **React Router** for navigation
- **TailwindCSS** for styling
- **Axios** for API calls
- **qrcode.react** for QR code generation
- **@zxing/library** for QR code scanning
- **Vitest** for testing

## Project Structure

```
frontend/
├── src/
│   ├── components/        # Reusable components
│   │   ├── Layout.tsx     # Main layout with navigation
│   │   ├── Loading.tsx    # Loading spinner
│   │   └── ErrorMessage.tsx
│   ├── pages/            # Page components
│   │   ├── Home.tsx
│   │   ├── EventList.tsx
│   │   ├── EventForm.tsx
│   │   ├── EventDetails.tsx
│   │   ├── UserList.tsx
│   │   ├── UserForm.tsx
│   │   ├── UserDetails.tsx
│   │   ├── TicketList.tsx
│   │   ├── TicketForm.tsx
│   │   ├── TicketDetails.tsx
│   │   └── QRScanner.tsx
│   ├── services/         # API service layer
│   │   ├── api.ts        # Axios instance
│   │   ├── eventService.ts
│   │   ├── userService.ts
│   │   └── ticketService.ts
│   ├── types/            # TypeScript types
│   │   └── index.ts
│   └── App.tsx           # Main app with routing
└── package.json
```

## Getting Started

### Prerequisites

- Node.js 20+ and npm
- Backend API running on `http://localhost:8080`

### Installation

```bash
cd frontend
npm install
```

### Development

Start the development server:

```bash
npm run dev
```

The app will be available at `http://localhost:5173`

### Build for Production

```bash
npm run build
```

The production build will be in the `dist/` folder.

### Testing

Run tests:

```bash
npm test
```

### Linting

```bash
npm run lint
```

## API Configuration

The frontend connects to the backend API at `http://localhost:8080/api`. To change this, edit `src/services/api.ts`:

```typescript
const api = axios.create({
  baseURL: 'http://your-backend-url/api',
  // ...
});
```

## Features Overview

### Event Management
- View all events in a grid layout
- Create new events with name, location, and date
- Edit existing events
- Delete events
- View event details with ticket count

### User Management
- View all users in a table
- Create new users with name, email, and age
- Edit user information
- Delete users
- View user details with associated tickets

### Ticket Management
- View all tickets with status indicators
- Create tickets for specific event/user combinations
- View ticket details including QR code
- Download ticket as PDF
- Ticket status: VALID, USED, or INVALID

### QR Code Validation
- Use device camera to scan QR codes
- Manual QR code entry
- Automatically validates tickets via backend API
- Updates ticket status to USED upon validation

## Browser Compatibility

- Modern browsers with ES6+ support
- Camera access required for QR scanning feature

