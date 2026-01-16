// src/pages/admin/Login.tsx

export default function AdminLogin() {
  return (
    <div className="min-h-screen flex items-center justify-center p-6">
      <div className="max-w-md w-full">
        <div className="card p-8">
          <h1 className="font-doodle text-4xl text-ink text-center mb-6">
            로그인
          </h1>
          
          <div className="space-y-4">
            <div>
              <label className="font-sans text-sm text-ink/70 mb-2 block">
                이메일
              </label>
              <input
                type="email"
                placeholder="이메일을 입력하세요"
                className="input"
              />
            </div>
            
            <div>
              <label className="font-sans text-sm text-ink/70 mb-2 block">
                비밀번호
              </label>
              <input
                type="password"
                placeholder="비밀번호를 입력하세요"
                className="input"
              />
            </div>

            <button className="btn btn-primary w-full mt-6">
              로그인
            </button>
          </div>

          <p className="font-sans text-xs text-center text-ink/50 mt-6">
            🔐 관리자 및 판매자 전용
          </p>
        </div>
      </div>
    </div>
  );
}
