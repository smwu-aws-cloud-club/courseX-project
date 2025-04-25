import axios from 'axios';

export const defaultAxios = axios.create({
  baseURL: process.env.REACT_APP_SERVER_DOMAIN,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const authAxios = axios.create({
  baseURL: process.env.REACT_APP_SERVER_DOMAIN,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

authAxios.interceptors.request.use(
  (config) => {
    const userId = localStorage.getItem('userId');

    if (userId) {
      config.headers['X-USER-ID'] = userId;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);
