import { authAxios } from 'axios.js';

export const enroll = async (code) => {
  try {
    const response = await authAxios.post(`/api/courses/${code}/enroll`);

    return response.data.message;
  } catch (error) {
    if (error.response) {
      throw new Error(error.response.data.message);
    }

    if (error.code === 'ECONNABORTED') {
      throw new Error('시간초과: 다시 시도해주세요');
    }

    throw new Error(error.message);
  }
};
