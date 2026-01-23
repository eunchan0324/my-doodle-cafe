// src/pages/Login.tsx
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Coffee } from 'lucide-react';
import api from '../api/axios';

type Role = 'CUSTOMER' | 'SELLER' | 'ADMIN';

type LoginResponse = {
  accessToken: string;
  role: Role;
  storeId?: string;
  storeName?: string;
  message?: string;
};

const ROLE_REDIRECT: Record<Role, string> = {
  CUSTOMER: '/customer/select_store',
  SELLER: '/seller/orders',
  ADMIN: '/admin/dashboard',
};

export default function Login() {
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

      const { accessToken, role, storeId, storeName } = response.data;

      // 토큰 & role 저장
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('userRole', role);

      // 판매자인 경우 storeId, storeName 저장
      if (role === 'SELLER' && storeId) {
        sessionStorage.setItem('sellerStoreId', storeId);
        if (storeName) {
          sessionStorage.setItem('sellerStoreName', storeName);
        }
      }

      setErrorMessage(null);

      // role에 따라 리다이렉트
      const redirectPath = ROLE_REDIRECT[role] || '/';
      navigate(redirectPath);
    } catch (error) {
      setErrorMessage('로그인에 실패했어요. 아이디와 비밀번호를 확인해주세요.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      className="min-h-screen flex items-center justify-center p-6"
      style={{
        backgroundImage: 'url(/images/paper-texture.png)',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      }}
    >
      <div className="w-full max-w-sm">
        <div className="card p-8 space-y-6">
          {/* 로고 & 타이틀 */}
          <div className="text-center space-y-2">
            <Coffee className="h-12 w-12 mx-auto text-ink" strokeWidth={1.5} />
            <h1 className="font-doodle text-3xl text-ink">
              My Doodle Cafe
            </h1>
            <p className="text-sm text-ink/60">
              로그인하고 주문을 시작하세요!
            </p>
          </div>

          {/* 로그인 폼 */}
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm text-ink/70 mb-1" htmlFor="loginId">
                아이디
              </label>
              <input
                id="loginId"
                type="text"
                className="input"
                value={loginId}
                onChange={(e) => setLoginId(e.target.value)}
                placeholder="아이디를 입력하세요"
                autoComplete="username"
              />
            </div>

            <div>
              <label className="block text-sm text-ink/70 mb-1" htmlFor="password">
                비밀번호
              </label>
              <input
                id="password"
                type="password"
                className="input"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="비밀번호를 입력하세요"
                autoComplete="current-password"
              />
            </div>

            {errorMessage && (
              <p className="text-sm text-danger text-center">
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

          {/* 안내 문구 */}
          <p className="text-xs text-center text-ink/40">
            고객 · 바리스타 · 매니저 모두 이곳에서 로그인
          </p>
        </div>

        {/* 홈으로 돌아가기 */}
        <button
          type="button"
          onClick={() => navigate('/')}
          className="w-full mt-4 text-sm text-ink/50 hover:text-ink transition-colors"
        >
          ← 메인으로 돌아가기
        </button>
      </div>
    </div>
  );
}
