// src/pages/seller/Orders.tsx
// 판매자 주문 접수 - 칸반 보드

import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Clock, Check, Bell } from 'lucide-react';
import api from '../../api/axios';

// 주문 상태 타입
type OrderStatus = 'ORDER_PLACED' | 'PREPARING' | 'READY' | 'COMPLETED';

// 온도/컵/샷 옵션 타입
type Temperature = 'HOT' | 'ICE';
type CupType = 'DISPOSABLE' | 'STORE' | 'PERSONAL';
type ShotOption = 'NONE' | 'BASIC' | 'LIGHT' | 'EXTRA' | 'DECAFFEINATED';

// 주문 아이템 타입 (SellerOrderItemDto)
type OrderItem = {
  menuName: string;
  quantity: number;
  temperature: Temperature;
  cupType: CupType;
  shotOption: ShotOption;
};

// 주문 타입 (엔티티 그대로)
type Order = {
  orderId: string;
  waitingNumber: number;
  status: OrderStatus;       // 엔티티 필드명
  items: OrderItem[];        // 엔티티 필드명
  totalPrice: number;
  orderTime: string;         // 엔티티 필드명
};

// 칸반 컬럼 설정
const COLUMNS: { status: OrderStatus; label: string; bgColor: string; opacity?: string }[] = [
  { status: 'ORDER_PLACED', label: '주문 접수', bgColor: 'bg-orange-50' },
  { status: 'PREPARING', label: '준비 중', bgColor: 'bg-white' },
  { status: 'READY', label: '준비 완료', bgColor: 'bg-ink/5', opacity: 'opacity-90' },
];

// 옵션 라벨 변환
const TEMPERATURE_LABEL: Record<Temperature, string> = {
  HOT: 'HOT',
  ICE: 'ICE',
};

const CUP_LABEL: Record<CupType, string> = {
  DISPOSABLE: '일회용',
  STORE: '매장컵',
  PERSONAL: '개인컵',
};

const SHOT_LABEL: Record<ShotOption, string> = {
  NONE: '',
  BASIC: '기본',
  LIGHT: '연하게',
  EXTRA: '샷추가',
  DECAFFEINATED: '디카페인',
};

// 경과 시간 계산
function getElapsedTime(orderTime: string): string {
  const created = new Date(orderTime);
  const now = new Date();
  const diffMs = now.getTime() - created.getTime();
  const diffMin = Math.floor(diffMs / 60000);

  if (diffMin < 1) return '방금 전';
  if (diffMin < 60) return `${diffMin}분 전`;
  const diffHour = Math.floor(diffMin / 60);
  return `${diffHour}시간 ${diffMin % 60}분 전`;
}

export default function SellerOrders() {
  const navigate = useNavigate();
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const storeId = sessionStorage.getItem('sellerStoreId');

  const fetchOrders = async () => {
    if (!storeId) return;

    try {
      setErrorMessage(null);
      const response = await api.get<Order[]>(`/api/v1/stores/${storeId}/orders`);
      setOrders(response.data);
    } catch (error) {
      console.error('주문 목록 조회 실패:', error);
      setErrorMessage('주문 목록을 불러오지 못했어요.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    const role = localStorage.getItem('userRole');

    if (!token) {
      navigate('/login');
      return;
    }

    if (role !== 'SELLER') {
      navigate('/forbidden');
      return;
    }

    if (!storeId) {
      setErrorMessage('매장 정보가 없어요. 다시 로그인해주세요.');
      setLoading(false);
      return;
    }

    fetchOrders();
  }, [navigate, storeId]);

  const handleStatusChange = async (orderId: string, newStatus: OrderStatus) => {
    if (!storeId) return;

    try {
      await api.post(`/api/v1/stores/${storeId}/orders/${orderId}/status`, {
        orderStatus: newStatus,
      });
      // 성공 시 목록 새로고침
      fetchOrders();
    } catch (error) {
      console.error('상태 변경 실패:', error);
      setErrorMessage('상태 변경에 실패했어요.');
    }
  };

  const getOrdersByStatus = (status: OrderStatus) =>
    orders.filter((order) => order.status === status);

  if (loading) {
    return (
      <div className="h-full flex items-center justify-center">
        <p className="font-sans text-ink/60">주문 목록을 불러오는 중...</p>
      </div>
    );
  }

  if (errorMessage && orders.length === 0) {
    return (
      <div className="h-full flex items-center justify-center">
        <div className="text-center space-y-4">
          <p className="font-sans text-ink/60">{errorMessage}</p>
          <button
            type="button"
            onClick={() => navigate('/login')}
            className="btn btn-primary"
          >
            다시 로그인
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="h-full flex flex-col p-6">
      {/* 헤더 */}
      <header className="mb-6 flex items-center justify-between">
        <div>
          <div className="inline-block">
            <h1 className="font-doodle text-3xl text-ink px-4 py-2 bg-white border-2 border-ink shadow-doodle">
              📋 주문 접수
            </h1>
          </div>
          <p className="mt-2 text-sm text-ink/60 font-sans">
            📅 {new Date().toLocaleDateString('ko-KR', { month: 'long', day: 'numeric', weekday: 'short' })} · 총 {orders.length}건
          </p>
        </div>
        <button
          type="button"
          onClick={fetchOrders}
          className="btn btn-primary"
        >
          새로고침
        </button>
      </header>

      {/* 칸반 보드 */}
      <div className="flex-1 grid grid-cols-3 gap-4 overflow-hidden">
        {COLUMNS.map((column) => (
          <div
            key={column.status}
            className={`flex flex-col rounded-2xl border-2 border-ink overflow-hidden ${column.bgColor} ${column.opacity || ''}`}
          >
            {/* 컬럼 헤더 */}
            <div className="px-4 py-3 border-b-2 border-ink/20 flex items-center justify-between">
              <h2 className="font-doodle text-xl text-ink">{column.label}</h2>
              <span className="px-2 py-1 text-sm font-sans bg-ink/10 rounded-full">
                {getOrdersByStatus(column.status).length}
              </span>
            </div>

            {/* 주문 카드 목록 */}
            <div className="flex-1 overflow-auto p-3 space-y-3">
              {getOrdersByStatus(column.status).map((order) => (
                <OrderCard
                  key={order.orderId}
                  order={order}
                  onStatusChange={handleStatusChange}
                />
              ))}

              {getOrdersByStatus(column.status).length === 0 && (
                <p className="text-center text-ink/40 text-sm py-8">
                  주문이 없어요
                </p>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

// 주문 카드 컴포넌트
function OrderCard({
  order,
  onStatusChange,
}: {
  order: Order;
  onStatusChange: (orderId: string, status: OrderStatus) => void;
}) {
  const elapsedTime = getElapsedTime(order.orderTime);
  const isUrgent = order.status === 'ORDER_PLACED' && elapsedTime.includes('분') && parseInt(elapsedTime) >= 5;

  // 옵션 문자열 생성
  const getOptionText = (item: OrderItem): string => {
    const parts: string[] = [];
    if (item.temperature) parts.push(TEMPERATURE_LABEL[item.temperature]);
    if (item.cupType && item.cupType !== 'DISPOSABLE') parts.push(CUP_LABEL[item.cupType]);
    if (item.shotOption && item.shotOption !== 'NONE' && item.shotOption !== 'BASIC') parts.push(SHOT_LABEL[item.shotOption]);
    return parts.join(' / ');
  };

  return (
    <div className="bg-white border-2 border-ink rounded-xl shadow-doodle overflow-hidden hover:-translate-y-1 transition-transform">
      {/* 카드 헤더 */}
      <div className="px-4 py-3 border-b border-dashed border-ink/30 flex items-center justify-between">
        <span className="font-doodle text-2xl text-ink">#{order.waitingNumber}</span>
        <span className={`flex items-center gap-1 text-xs font-sans ${isUrgent ? 'text-danger' : 'text-ink/50'}`}>
          <Clock className="h-3 w-3" />
          {elapsedTime}
        </span>
      </div>

      {/* 메뉴 리스트 */}
      <div className="px-4 py-3 space-y-2">
        {order.items.map((item, idx) => (
          <div key={idx} className="flex justify-between items-start text-sm">
            <div>
              <span className="font-sans text-ink">{item.menuName}</span>
              {getOptionText(item) && (
                <p className="text-xs text-ink/50">{getOptionText(item)}</p>
              )}
            </div>
            <span className="font-sans text-ink/70">×{item.quantity}</span>
          </div>
        ))}
      </div>

      {/* 총액 */}
      <div className="px-4 py-2 bg-ink/5 text-right">
        <span className="font-sans text-sm font-semibold text-ink">
          {order.totalPrice.toLocaleString()}원
        </span>
      </div>

      {/* 액션 버튼 */}
      <div className="px-4 py-3 border-t border-dashed border-ink/30">
        {order.status === 'ORDER_PLACED' && (
          <div className="flex gap-2">
            <button
              type="button"
              onClick={() => onStatusChange(order.orderId, 'PREPARING')}
              className="flex-1 flex items-center justify-center gap-1 px-3 py-2 bg-ink text-white rounded-lg font-sans text-sm hover:bg-ink/80 transition-colors"
            >
              <Check className="h-4 w-4" />
              준비 시작
            </button>
          </div>
        )}

        {order.status === 'PREPARING' && (
          <button
            type="button"
            onClick={() => onStatusChange(order.orderId, 'READY')}
            className="w-full flex items-center justify-center gap-1 px-3 py-2 border-2 border-crayon text-crayon rounded-lg font-sans text-sm hover:bg-crayon/5 transition-colors"
          >
            <Bell className="h-4 w-4" />
            준비 완료
          </button>
        )}

        {order.status === 'READY' && (
          <button
            type="button"
            onClick={() => onStatusChange(order.orderId, 'COMPLETED')}
            className="w-full flex items-center justify-center gap-1 px-3 py-2 border-2 border-ink/30 text-ink/50 rounded-lg font-sans text-sm hover:bg-ink/5 transition-colors"
          >
            <Check className="h-4 w-4" />
            픽업 완료
          </button>
        )}
      </div>
    </div>
  );
}
