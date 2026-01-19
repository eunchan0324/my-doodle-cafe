import axios from 'axios';

const api = axios.create({
  // 1. 벡앤드 주소
  baseURL: 'http://localhost:8080',

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

export default api;