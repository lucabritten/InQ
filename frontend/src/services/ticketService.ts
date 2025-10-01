import api from './api';
import type { Ticket, TicketDTO } from '../types';

export const ticketService = {
  getAll: async (): Promise<Ticket[]> => {
    const response = await api.get<Ticket[]>('/tickets');
    return response.data;
  },

  getOne: async (id: number): Promise<Ticket> => {
    const response = await api.get<Ticket>(`/tickets/${id}`);
    return response.data;
  },

  create: async (ticketDTO: TicketDTO): Promise<Ticket> => {
    const response = await api.post<Ticket>('/tickets', ticketDTO);
    return response.data;
  },

  update: async (id: number, ticketDTO: TicketDTO): Promise<Ticket> => {
    const response = await api.put<Ticket>(`/tickets/${id}`, ticketDTO);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/tickets/${id}`);
  },

  useTicket: async (id: number): Promise<Ticket> => {
    const response = await api.patch<Ticket>(`/tickets/${id}/use`);
    return response.data;
  },

  getQRCode: async (id: number): Promise<string> => {
    const response = await api.get(`/tickets/${id}/qrcode`, {
      responseType: 'blob',
    });
    return URL.createObjectURL(response.data);
  },

  getPDF: async (id: number): Promise<string> => {
    const response = await api.get(`/tickets/${id}/pdf`, {
      responseType: 'blob',
    });
    return URL.createObjectURL(response.data);
  },
};
