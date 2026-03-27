import api from './api';

export const transactionService = {
  async transfer(receiverId, amount) {
    const response = await api.post('/transactions', { receiverId, amount });
    return response.data;
  },

  async getHistory() {
    const response = await api.get('/transactions/history');
    return response.data;
  },
};
