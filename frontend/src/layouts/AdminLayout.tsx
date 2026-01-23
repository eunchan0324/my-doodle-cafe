// src/layouts/AdminLayout.tsx
import { NavLink, Outlet, useLocation, useNavigate } from 'react-router-dom';
import { LogOut } from 'lucide-react';

const tabs = [
  { label: '메뉴 관리', path: '/admin/menus' },
  { label: '지점 관리', path: '/admin/stores' },
  { label: '계정 관리', path: '/admin/accounts' },
  { label: '매출 분석', path: '/admin/sales' },
];

export default function AdminLayout() {
  const location = useLocation();
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('userRole');
    sessionStorage.clear();
    navigate('/login');
  };

  return (
    <div className="min-h-screen bg-[#E5E5E5] font-sans text-ink">
      <header className="flex items-center justify-between px-10 py-6">
        <div className="flex items-center gap-3">
          <span className="text-2xl">🦁</span>
          <span className="text-2xl font-doodle text-ink">Doodle Admin</span>
        </div>
        <button
          type="button"
          onClick={handleLogout}
          className="flex items-center gap-2 rounded-lg border-2 border-ink bg-white px-4 py-2 text-sm font-sans text-ink shadow-[3px_3px_0px_0px_rgba(0,0,0,0.2)] transition-transform hover:-translate-y-0.5"
        >
          <LogOut size={16} strokeWidth={2.5} />
          로그아웃
        </button>
      </header>

      <main className="mx-auto w-full max-w-6xl px-8 pb-12">
        <div className="flex items-end gap-2">
          {tabs.map((tab) => {
            const isActive = location.pathname.startsWith(tab.path);
            return (
              <NavLink
                key={tab.path}
                to={tab.path}
                className={`relative flex items-center justify-center border-2 border-ink px-6 font-doodle transition-all ${
                  isActive
                    ? 'z-10 h-14 bg-[#FAFAFA] text-ink'
                    : 'h-12 bg-[#D4D4D4] text-gray-500 hover:text-ink'
                } rounded-t-2xl`}
              >
                {tab.label}
              </NavLink>
            );
          })}
        </div>

        <section className="relative -mt-[2px] rounded-b-2xl border-2 border-ink bg-[#FAFAFA] p-8 shadow-[6px_6px_0px_rgba(0,0,0,0.1)]">
          <div className="absolute left-2 top-10 flex flex-col gap-6">
            <span className="h-3 w-3 rounded-full bg-ink" />
            <span className="h-3 w-3 rounded-full bg-ink" />
            <span className="h-3 w-3 rounded-full bg-ink" />
          </div>
          <div className="pl-6">
            <Outlet />
          </div>
        </section>
      </main>
    </div>
  );
}
