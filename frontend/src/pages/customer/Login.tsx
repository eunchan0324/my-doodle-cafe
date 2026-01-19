import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import CustomerLayout from '../../layouts/CustomerLayout';
import api from '../../api/axios';

type LoginResponse = {
  accessToken: string;
  message: string;
};

export default function CustomerLogin() {
  const navigate = useNavigate();
  const [loginId, setLoginId] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!loginId || !password) {
      setErrorMessage('아이디와 비밀번호를 입력해주세요.');
      return;
    }

    try {
      setLoading(true);
      const response = await api.post<LoginResponse>('/api/v1/auth/login', {
        loginId,
        password,
      });
      localStorage.setItem('accessToken', response.data.accessToken);
      setErrorMessage(null);
      navigate('/customer/select_store');
    } catch (error) {
      setErrorMessage('로그인에 실패했어요. 다시 확인해주세요.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <CustomerLayout showNav={false}>
      <div className="py-8">
        <div className="card p-6 space-y-4">
          <h1 className="font-doodle text-3xl text-ink">
            고객 로그인
          </h1>
          <p className="text-sm text-ink/60">
            주문을 시작하려면 로그인해주세요.
          </p>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm text-ink/70" htmlFor="loginId">
                아이디
              </label>
              <input
                id="loginId"
                type="text"
                className="input mt-2"
                value={loginId}
                onChange={(event) => setLoginId(event.target.value)}
                placeholder="Id"
              />
            </div>

            <div>
              <label className="block text-sm text-ink/70" htmlFor="password">
                비밀번호
              </label>
              <input
                id="password"
                type="password"
                className="input mt-2"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                placeholder="password"
              />
            </div>

            {errorMessage && (
              <p className="text-sm text-danger">
                {errorMessage}
              </p>
            )}

            <button
              type="submit"
              className="btn btn-primary w-full"
              disabled={loading}
            >
              {loading ? '로그인 중…' : '로그인'}
            </button>
          </form>
        </div>
      </div>
    </CustomerLayout>
  );
}
