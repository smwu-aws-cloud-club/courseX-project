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

export const fetchCourses = async (code) => {
  try {
    const response = await authAxios.get('/api/courses', {
      params: {
        code,
      },
    });

    return response.data.data;
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

export const fetchEnrollments = async () => {
  try {
    const response = await authAxios.get('/api/enrollments');

    return response.data.data;
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
