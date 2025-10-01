export interface Event {
  id?: number;
  name: string;
  location: string;
  date: string;
  ticketIds?: number[];
}

export interface User {
  id?: number;
  name: string;
  age: number;
  emailAddress: string;
  ticketIds?: number[];
  createdAt?: string;
  updatedAt?: string;
}

export interface Ticket {
  id?: number;
  eventId: number;
  userId: number;
  status: 'VALID' | 'USED' | 'INVALID';
  qrCode?: string;
}

// DTO types for create/update operations (without id, createdAt, updatedAt, etc.)
export interface EventDTO {
  name: string;
  location: string;
  date: string;
}

export interface UserDTO {
  name: string;
  age: number;
  emailAddress: string;
  ticketIds?: number[];
}

export interface TicketDTO {
  eventId: number;
  userId: number;
}

export interface ApiError {
  status?: string;
  message: string;
  errors?: string[];
}
