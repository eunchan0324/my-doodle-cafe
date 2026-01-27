import api from './axios';

export interface UserSignupRequest {
  loginId: string;
  password: string;
  name: string;
}

/**
 * 회원가입
 */
export const signup = async (data: UserSignupRequest): Promise<void> => {
  await api.post('/api/v1/users/signup', data);
};
