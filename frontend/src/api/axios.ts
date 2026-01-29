import axios from 'axios';

const api = axios.create({
  // 1. 벡앤드 주소
  // baseURL: 'http://localhost:8080',
  baseURL: 'http://218.156.123.200:8080',

  // 2. 요청 시 세션 쿠키를 자동으로 담아 보냄 (로그인 유지 필수 설정)
  withCredentials: true,

  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  if (typeof window !== 'undefined') {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // 토큰 만료 또는 미인증 → 로그인 페이지로
      localStorage.removeItem('accessToken');
      localStorage.removeItem('userRole');
      window.location.href = '/login';
    }
    if (error.response?.status === 403) {
      window.location.href = '/forbidden';
    }
    return Promise.reject(error);
  }
);

export default api;