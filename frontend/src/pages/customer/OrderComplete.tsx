// src/pages/customer/OrderComplete.tsx
import { useLocation, useNavigate } from 'react-router-dom';
import CustomerLayout from '../../layouts/CustomerLayout';

type LocationState = {
  orderId?: string;
  waitingNumber?: number;
  message?: string;
};

export default function OrderComplete() {
  const navigate = useNavigate();
  const location = useLocation();
  const state = location.state as LocationState | null;

  const waitingNumber = state?.waitingNumber;
  const message = state?.message ?? '주문이 완료되었습니다!';

  const storeId = sessionStorage.getItem('selectedStoreId');

  const handleGoToMenu = () => {
    if (storeId) {
      navigate(`/customer/stores/${storeId}/menus`);
    } else {
      navigate('/customer/select_store');
    }
  };

  return (
    <CustomerLayout>
      <div className="py-12 px-4 flex flex-col items-center justify-center min-h-[60vh]">
        <div className="card p-8 text-center space-y-6 max-w-sm w-full">
          <div className="text-6xl">🎉</div>
          <h1 className="font-doodle text-3xl text-ink">
            주문 완료!
          </h1>
          <p className="text-ink/70 font-sans">
            {message}
          </p>
          <div className="bg-paper border-2 border-ink rounded-xl p-6">
            <p className="text-sm text-ink/60">대기 번호</p>
            <p className="font-doodle text-5xl text-ink mt-2">
              {waitingNumber ?? '-'}
            </p>
          </div>
          <button
            type="button"
            onClick={handleGoToMenu}
            className="btn btn-primary w-full"
          >
            메뉴로 돌아가기
          </button>
        </div>
      </div>
    </CustomerLayout>
  );
}
