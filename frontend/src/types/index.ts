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

export interface ApiError {
  status?: string;
  message: string;
  errors?: string[];
}
