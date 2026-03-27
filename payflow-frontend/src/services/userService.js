import api from './api';

export const userService = {
  async getMe() {
    const response = await api.get('/users/me');
    return response.data;
  },

  async deposit(amount) {
    const response = await api.post('/users/me/deposit', { amount });
    return response.data;
  },
};
