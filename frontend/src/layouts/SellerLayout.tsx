// src/layouts/SellerLayout.tsx
// Seller용 태블릿 레이아웃 (POS 시스템 - 좌측 사이드바)

import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { ClipboardList, Coffee, BarChart3, LogOut } from 'lucide-react';

const NAV_ITEMS = [
  { to: '/seller/orders', icon: ClipboardList, label: '주문 접수' },
  { to: '/seller/menus', icon: Coffee, label: '메뉴 관리' },
  { to: '/seller/sales', icon: BarChart3, label: '매출 조회' },
];

export default function SellerLayout() {
  const navigate = useNavigate();
  const storeName = sessionStorage.getItem('sellerStoreName') || '매장';

  const handleLogout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('userRole');
    sessionStorage.removeItem('sellerStoreId');
    sessionStorage.removeItem('sellerStoreName');
    navigate('/login');
  };

  return (
    <div
      className="min-h-screen flex"
      style={{
        backgroundImage: 'url(/images/paper-texture.png)',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundAttachment: 'fixed',
      }}
    >
      {/* 좌측 사이드바 */}
      <aside className="w-64 bg-white/90 backdrop-blur border-r-4 border-ink flex flex-col">
        {/* 로고 영역 */}
        <div className="p-6 border-b-2 border-ink/20">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 rounded-full bg-doodle-yellow border-2 border-ink flex items-center justify-center">
              <Coffee className="h-6 w-6 text-ink" strokeWidth={2.5} aria-label="logo" />
            </div>
            <div>
              <h1 className="font-doodle text-xl text-ink">My Doodle</h1>
              <p className="text-xs text-ink/60">Barista Mode</p>
            </div>
          </div>
          <p className="mt-3 text-sm font-sans text-ink/70 truncate">
            📍 {storeName}
          </p>
        </div>

        {/* 네비게이션 */}
        <nav className="flex-1 py-4">
          {NAV_ITEMS.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) =>
                `flex items-center gap-3 px-6 py-4 transition-all ${
                  isActive
                    ? 'text-crayon border-l-4 border-crayon bg-crayon/5'
                    : 'text-ink/60 border-l-4 border-transparent hover:bg-ink/5'
                }`
              }
            >
              {({ isActive }) => (
                <>
                  <item.icon
                    className={`h-6 w-6 transition-transform ${isActive ? 'scale-110' : ''}`}
                    strokeWidth={2.5}
                  />
                  <span className="font-sans text-base font-medium">
                    {item.label}
                  </span>
                </>
              )}
            </NavLink>
          ))}
        </nav>

        {/* 로그아웃 버튼 */}
        <div className="p-4 border-t-2 border-ink/20">
          <button
            type="button"
            onClick={handleLogout}
            className="w-full flex items-center justify-center gap-2 px-4 py-3 text-ink/60 hover:text-danger hover:bg-danger/5 rounded-xl transition-colors"
          >
            <LogOut className="h-5 w-5" strokeWidth={2.5} />
            <span className="font-sans text-sm">퇴근하기</span>
          </button>
        </div>
      </aside>

      {/* 우측 메인 콘텐츠 */}
      <main className="flex-1 overflow-auto">
        <Outlet />
      </main>
    </div>
  );
}
