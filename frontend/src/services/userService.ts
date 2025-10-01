import api from './api';
import type { User, UserDTO } from '../types';

export const userService = {
  getAll: async (): Promise<User[]> => {
    const response = await api.get<User[]>('/users');
    return response.data;
  },

  getOne: async (id: number): Promise<User> => {
    const response = await api.get<User>(`/users/${id}`);
    return response.data;
  },

  create: async (userDTO: UserDTO): Promise<User> => {
    const response = await api.post<User>('/users', userDTO);
    return response.data;
  },

  update: async (id: number, userDTO: UserDTO): Promise<User> => {
    const response = await api.put<User>(`/users/${id}`, userDTO);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/users/${id}`);
  },
};
