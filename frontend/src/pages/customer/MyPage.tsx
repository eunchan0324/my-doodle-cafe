import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Heart, Receipt, LogOut, ChevronRight } from 'lucide-react';
import CustomerLayout from '../../layouts/CustomerLayout';

export default function MyPage() {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      navigate('/login');
    }
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem('accessToken');
    sessionStorage.removeItem('selectedStoreId');
    sessionStorage.removeItem('selectedStoreName');
    sessionStorage.removeItem('customerActiveCategory');
    navigate('/login');
  };

  return (
    <CustomerLayout>
      <div className="py-6 px-4 space-y-6">
        <h1 className="font-doodle text-3xl text-ink">
          마이 페이지
        </h1>

        <div className="space-y-3">
          <button
            type="button"
            onClick={() => navigate('/customer/favorites')}
            className="card p-4 w-full flex items-center justify-between"
          >
            <div className="flex items-center gap-3">
              <Heart className="h-5 w-5 text-crayon" />
              <span className="font-sans text-base text-ink">찜 목록</span>
            </div>
            <ChevronRight className="h-5 w-5 text-ink/40" />
          </button>

          <button
            type="button"
            onClick={() => navigate('/customer/history')}
            className="card p-4 w-full flex items-center justify-between"
          >
            <div className="flex items-center gap-3">
              <Receipt className="h-5 w-5 text-doodle-blue" />
              <span className="font-sans text-base text-ink">주문 내역</span>
            </div>
            <ChevronRight className="h-5 w-5 text-ink/40" />
          </button>
        </div>

        <div className="pt-4">
          <button
            type="button"
            onClick={handleLogout}
            className="card p-4 w-full flex items-center gap-3 text-danger"
          >
            <LogOut className="h-5 w-5" />
            <span className="font-sans text-base">로그아웃</span>
          </button>
        </div>
      </div>
    </CustomerLayout>
  );
}
