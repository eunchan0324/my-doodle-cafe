// src/pages/customer/OrderHistory.tsx
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import CustomerLayout from '../../layouts/CustomerLayout';
import api from '../../api/axios';

type OrderStatus = 'ORDER_PLACED' | 'PREPARING' | 'READY' | 'COMPLETED';

type CustomerOrderSummary = {
  watingNumber: number;
  storeName: string;
  orderTime: string;
  totalPrice: number;
  orderStatus: OrderStatus;
  menuSummary: string;
};

const STATUS_LABEL: Record<OrderStatus, string> = {
  ORDER_PLACED: '주문 접수',
  PREPARING: '준비중',
  READY: '준비완료',
  COMPLETED: '픽업완료',
};

const STATUS_COLOR: Record<OrderStatus, string> = {
  ORDER_PLACED: 'bg-doodle-blue text-white',
  PREPARING: 'bg-doodle-yellow text-ink',
  READY: 'bg-crayon text-white',
  COMPLETED: 'bg-ink/30 text-ink',
};

function formatDateTime(isoString: string): string {
  const date = new Date(isoString);
  const month = date.getMonth() + 1;
  const day = date.getDate();
  const hours = date.getHours();
  const minutes = date.getMinutes().toString().padStart(2, '0');
  return `${month}/${day} ${hours}:${minutes}`;
}

export default function OrderHistory() {
  const navigate = useNavigate();
  const [orders, setOrders] = useState<CustomerOrderSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      navigate('/customer/login');
      return;
    }

    let isMounted = true;

    async function fetchOrders() {
      try {
        const response = await api.get<CustomerOrderSummary[]>('/api/v1/orders');
        if (!isMounted) return;
        setOrders(response.data);
        setErrorMessage(null);
      } catch (error) {
        if (!isMounted) return;
        setErrorMessage('주문 내역을 불러오지 못했어요.');
      } finally {
        if (!isMounted) return;
        setLoading(false);
      }
    }

    fetchOrders();
    return () => {
      isMounted = false;
    };
  }, [navigate]);

  return (
    <CustomerLayout>
      <div className="py-6 px-4">
        <h1 className="font-doodle text-3xl text-ink mb-6">주문 내역</h1>

        {loading && (
          <div className="card p-6">
            <p className="font-sans text-center text-ink/60">
              주문 내역을 불러오는 중이에요…
            </p>
          </div>
        )}

        {!loading && errorMessage && (
          <div className="card p-6">
            <p className="font-sans text-center text-ink/60">{errorMessage}</p>
          </div>
        )}

        {!loading && !errorMessage && orders.length === 0 && (
          <div className="card p-6">
            <p className="font-sans text-center text-ink/60">
              아직 주문 내역이 없어요.
            </p>
          </div>
        )}

        {!loading && !errorMessage && orders.length > 0 && (
          <div className="space-y-4">
            {orders.map((order, index) => (
              <div
                key={`${order.watingNumber}-${index}`}
                className="card p-4 space-y-3"
              >
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <span className="font-doodle text-2xl text-ink">
                      #{order.watingNumber}
                    </span>
                    <span
                      className={`px-2 py-1 text-xs font-bold rounded-full border-2 border-ink ${STATUS_COLOR[order.orderStatus]}`}
                    >
                      {STATUS_LABEL[order.orderStatus]}
                    </span>
                  </div>
                  <span className="text-sm text-ink/60">
                    {formatDateTime(order.orderTime)}
                  </span>
                </div>

                <div className="text-sm text-ink/80">
                  <p className="font-semibold">{order.storeName}</p>
                  <p className="mt-1 text-ink/60">{order.menuSummary}</p>
                </div>

                <div className="flex items-center justify-between pt-2 border-t border-ink/10">
                  <span className="text-sm text-ink/60">총 금액</span>
                  <span className="font-semibold text-ink">
                    {order.totalPrice.toLocaleString()}원
                  </span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </CustomerLayout>
  );
}
