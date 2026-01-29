// src/pages/Signup.tsx
import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Coffee } from 'lucide-react';
import { signup, type UserSignupRequest } from '../api/authApi';

export default function Signup() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<UserSignupRequest>({
    loginId: '',
    password: '',
    name: '',
  });
  const [errors, setErrors] = useState<{ [key: string]: string }>({});
  const [loading, setLoading] = useState(false);

  const validate = () => {
    const newErrors: { [key: string]: string } = {};
    const { loginId, password, name } = formData;

    // 아이디: 4~20자, 영문 소문자/숫자
    const idRegex = /^[a-z0-9]+$/;
    if (!loginId) {
      newErrors.loginId = '아이디를 입력해주세요.';
    } else if (loginId.length < 4 || loginId.length > 20) {
      newErrors.loginId = '아이디는 4~20자여야 합니다.';
    } else if (!idRegex.test(loginId)) {
      newErrors.loginId = '아이디는 영문 소문자와 숫자만 가능합니다.';
    }

    // 비밀번호: 8~20자, 영문/숫자/특수문자
    // 특수문자: $ @ ! % * # ? & (백엔드 정규식 참고)
    const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,20}$/;
    if (!password) {
      newErrors.password = '비밀번호를 입력해주세요.';
    } else if (!pwRegex.test(password)) {
      newErrors.password = '8~20자, 영문/숫자/특수문자 포함 ($@!%*#?&)';
    }

    // 이름: 2~10자
    if (!name) {
      newErrors.name = '이름을 입력해주세요.';
    } else if (name.length < 2 || name.length > 10) {
      newErrors.name = '이름은 2~10자여야 합니다.';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    // 입력 시 해당 필드 에러 제거
    if (errors[name]) {
      setErrors((prev) => {
        const newErrors = { ...prev };
        delete newErrors[name];
        return newErrors;
      });
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validate()) return;

    setLoading(true);
    try {
      await signup(formData);
      alert('회원가입이 완료되었습니다!\n로그인해 주세요.');
      navigate('/login');
    } catch (error: any) {
      console.error('회원가입 실패:', error);
      if (error.response?.data?.message) {
        alert(error.response.data.message);
      } else {
        alert('회원가입 중 오류가 발생했습니다.');
      }
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
          {/* Header */}
          <div className="text-center space-y-2">
            <Coffee className="h-10 w-10 mx-auto text-crayon" strokeWidth={2} />
            <h1 className="font-doodle text-2xl text-ink">회원가입</h1>
            <p className="text-sm text-ink/60">My Doodle Cafe 멤버가 되어보세요!</p>
          </div>

          {/* Form */}
          <form onSubmit={handleSubmit} className="space-y-4">
            {/* 아이디 */}
            <div>
              <label className="block text-sm text-ink/70 mb-1" htmlFor="loginId">
                아이디
              </label>
              <input
                id="loginId"
                name="loginId"
                type="text"
                value={formData.loginId}
                onChange={handleChange}
                placeholder="영문 소문자, 숫자 4~20자"
                className={`input ${errors.loginId ? 'border-danger focus:border-danger' : ''}`}
              />
              {errors.loginId && <p className="mt-1 text-xs text-danger">{errors.loginId}</p>}
            </div>

            {/* 비밀번호 */}
            <div>
              <label className="block text-sm text-ink/70 mb-1" htmlFor="password">
                비밀번호
              </label>
              <input
                id="password"
                name="password"
                type="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="8~20자, 영문/숫자/특수문자 포함"
                className={`input ${errors.password ? 'border-danger focus:border-danger' : ''}`}
              />
              {errors.password && <p className="mt-1 text-xs text-danger">{errors.password}</p>}
            </div>

            {/* 이름 */}
            <div>
              <label className="block text-sm text-ink/70 mb-1" htmlFor="name">
                이름
              </label>
              <input
                id="name"
                name="name"
                type="text"
                value={formData.name}
                onChange={handleChange}
                placeholder="2~10자"
                className={`input ${errors.name ? 'border-danger focus:border-danger' : ''}`}
              />
              {errors.name && <p className="mt-1 text-xs text-danger">{errors.name}</p>}
            </div>

            {/* Submit Button */}
            <button
              type="submit"
              disabled={loading}
              className="btn btn-crayon w-full mt-2"
            >
              {loading ? '가입 중...' : '가입하기'}
            </button>
          </form>

          {/* Footer */}
          <div className="text-center text-sm">
            <span className="text-gray-500">이미 계정이 있으신가요? </span>
            <Link to="/login" className="font-semibold text-crayon hover:underline">
              로그인하기
            </Link>
          </div>
        </div>

        {/* Back to Home */}
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
