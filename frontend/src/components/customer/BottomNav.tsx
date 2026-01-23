// src/components/customer/BottomNav.tsx
import { NavLink, useNavigate, useLocation } from 'react-router-dom';
import { Coffee, Receipt, ThumbsUp, User } from 'lucide-react';

type NavItem = {
  label: string;
  path: string;
  icon: typeof Coffee;
  dynamic?: boolean;
};

const navItems: NavItem[] = [
  { label: '메뉴', path: '/customer/menus', icon: Coffee, dynamic: true },
  { label: '추천', path: '/customer/pick', icon: ThumbsUp },
  { label: '내역', path: '/customer/history', icon: Receipt },
  { label: '마이', path: '/customer/my', icon: User },
];

export default function BottomNav() {
  const navigate = useNavigate();
  const location = useLocation();

  // 메뉴 페이지 패턴 체크 (/customer/stores/:storeId/menus)
  const isMenuActive = /^\/customer\/stores\/\d+\/menus/.test(location.pathname);

  const handleMenuClick = (e: React.MouseEvent) => {
    e.preventDefault();
    const storeId = sessionStorage.getItem('selectedStoreId');
    if (storeId) {
      navigate(`/customer/stores/${storeId}/menus`);
    } else {
      navigate('/customer/select_store');
    }
  };

  return (
    <nav
      className="fixed bottom-0 left-0 right-0 z-50 bg-white"
      aria-label="하단 네비게이션"
      style={{
        backgroundImage:
          'url("data:image/svg+xml,%3Csvg width=\'160\' height=\'6\' viewBox=\'0 0 160 6\' xmlns=\'http://www.w3.org/2000/svg\'%3E%3Cpath d=\'M0 3 Q 10 1 20 3 T 40 3 T 60 3 T 80 3 T 100 3 T 120 3 T 140 3 T 160 3\' stroke=\'%2318181B\' stroke-width=\'2\' fill=\'none\' stroke-linecap=\'round\'/%3E%3C/svg%3E")',
        backgroundRepeat: 'repeat-x',
        backgroundPosition: 'top -2px left',
      }}
    >
      <div className="max-w-mobile mx-auto px-4">
        <ul className="flex items-center justify-between py-3.5">
          {navItems.map((item) => {
            const Icon = item.icon;
            return (
              <li key={item.path} className="flex-1">
                {item.dynamic ? (
                  <button
                    type="button"
                    onClick={handleMenuClick}
                    className={`w-full flex flex-col items-center justify-center gap-1 text-xs font-sans transition-all duration-150 ${
                      isMenuActive
                        ? 'text-crayon -translate-y-1'
                        : 'text-ink/40'
                    }`}
                  >
                    <Icon className="h-5 w-5" strokeWidth={2.2} />
                    <span>{item.label}</span>
                  </button>
                ) : (
                  <NavLink
                    to={item.path}
                    className={({ isActive }) =>
                      `flex flex-col items-center justify-center gap-1 text-xs font-sans transition-all duration-150 ${
                        isActive
                          ? 'text-crayon -translate-y-1'
                          : 'text-ink/40'
                      }`
                    }
                  >
                    <Icon className="h-5 w-5" strokeWidth={2.2} />
                    <span>{item.label}</span>
                  </NavLink>
                )}
              </li>
            );
          })}
        </ul>
      </div>
    </nav>
  );
}
