import api from './api';
import type { Event, EventDTO } from '../types';

export const eventService = {
  getAll: async (): Promise<Event[]> => {
    const response = await api.get<Event[]>('/events');
    return response.data;
  },

  getOne: async (id: number): Promise<Event> => {
    const response = await api.get<Event>(`/events/${id}`);
    return response.data;
  },

  create: async (eventDTO: EventDTO): Promise<Event> => {
    const response = await api.post<Event>('/events', eventDTO);
    return response.data;
  },

  update: async (id: number, eventDTO: EventDTO): Promise<Event> => {
    const response = await api.put<Event>(`/events/${id}`, eventDTO);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/events/${id}`);
  },
};
