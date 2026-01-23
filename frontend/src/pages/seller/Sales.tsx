// src/pages/seller/Sales.tsx
// 판매자 매출 조회 - "심바의 대왕 영수증" 컨셉

import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Receipt, Coins, Clock, ChevronDown, ChevronUp } from 'lucide-react';
import api from '../../api/axios';

// 타입 정의 (백엔드 API 응답 구조)
interface SellerOrderItemDto {
  menuName: string;
  quantity: number;
  optionSummary?: string;
}

interface SellerOrderResponse {
  orderId: string;
  waitingNumber: number;
  status: 'COMPLETED' | 'CANCELED';
  totalPrice: number;
  orderTime: string; // ISO String
  items: SellerOrderItemDto[];
}

interface SellerDailySalesResponse {
  totalSales: number;
  totalCount: number;
  orderHistory: SellerOrderResponse[];
}


export default function Sales() {
  const navigate = useNavigate();
  const [salesData, setSalesData] = useState<SellerDailySalesResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [expandedOrderId, setExpandedOrderId] = useState<string | null>(null);

  const storeId = sessionStorage.getItem('sellerStoreId');

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
      return;
    }

    fetchSalesData();
  }, [navigate, storeId]);

  const fetchSalesData = async () => {
    if (!storeId) return;

    try {
      const response = await api.get<SellerDailySalesResponse>(`/api/v1/stores/${storeId}/sales/today`);
      setSalesData(response.data);
    } catch (error) {
      console.error('매출 조회 실패:', error);
    } finally {
      setLoading(false);
    }
  };

  const toggleOrderDetail = (orderId: string) => {
    setExpandedOrderId(expandedOrderId === orderId ? null : orderId);
  };

  const formatTime = (isoString: string) => {
    const date = new Date(isoString);
    return date.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' });
  };

  const getOrderSummary = (items: SellerOrderItemDto[]) => {
    if (items.length === 0) return '주문 없음';
    if (items.length === 1) return items[0].menuName;
    return `${items[0].menuName} 외 ${items.length - 1}건`;
  };

  if (loading) {
    return (
      <div className="h-full flex items-center justify-center">
        <p className="font-sans text-ink/60">매출 데이터를 불러오는 중...</p>
      </div>
    );
  }

  if (!salesData) {
    return (
      <div className="h-full flex items-center justify-center">
        <p className="font-sans text-ink/60">매출 데이터가 없습니다.</p>
      </div>
    );
  }

  return (
    <div className="h-full p-6">
      {/* 헤더 */}
      <div className="mb-8">
        <h1 className="font-doodle text-4xl text-ink mb-3">오늘의 매출</h1>
        <p className="font-sans text-ink/60 text-base">
          {new Date().toLocaleDateString('ko-KR', { 
            year: 'numeric', 
            month: 'long', 
            day: 'numeric',
            weekday: 'short'
          })} 정산
        </p>
      </div>

      {/* 메인 레이아웃: 좌측(성적표+심바) + 우측(내역) */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 h-[calc(100%-140px)]">
        
        {/* 좌측: 요약 패널 + 심바 캐릭터 */}
        <div className="lg:col-span-1 flex flex-col gap-4">
          
          {/* 요약 패널 - 흰색 카드로 변경 */}
          <div className="bg-white border-2 border-ink rounded-tl-2xl rounded-tr-3xl rounded-bl-2xl rounded-br-xl p-5 shadow-[4px_4px_0px_0px_rgba(24,24,27,1)] flex flex-col gap-4">
            <div className="flex items-center gap-2">
              <Coins size={28} strokeWidth={2.5} className="text-crayon" />
              <h2 className="font-doodle text-2xl text-ink">오늘의 성적표</h2>
            </div>

            {/* 총 매출 - 주황색 강조 */}
            <div className="bg-crayon border-2 border-ink rounded-tl-xl rounded-tr-2xl rounded-bl-lg rounded-br-xl p-4 shadow-[2px_2px_0px_0px_rgba(24,24,27,1)]">
              <p className="font-sans text-white/90 text-xs mb-1">총 매출</p>
              <p className="font-sans text-4xl font-bold text-white">
                {salesData.totalSales.toLocaleString()}
                <span className="text-xl ml-2">원</span>
              </p>
            </div>

            {/* 판매 건수 - 회색 배경 */}
            <div className="bg-paper border-2 border-ink/20 rounded-tl-2xl rounded-tr-lg rounded-bl-xl rounded-br-2xl p-3">
              <p className="font-sans text-ink/60 text-xs mb-1">총 판매 건수</p>
              <p className="font-sans text-2xl font-bold text-ink">
                {salesData.totalCount}
                <span className="text-base ml-1">건</span>
              </p>
            </div>
          </div>

          {/* 심바 캐릭터 - 작게 */}
          <div className="relative bg-white border-2 border-ink rounded-tl-3xl rounded-tr-2xl rounded-bl-xl rounded-br-2xl p-4 shadow-[4px_4px_0px_0px_rgba(24,24,27,1)] flex flex-col items-center">
            {/* 마스킹 테이프 */}
            <div className="absolute -top-2 left-1/2 -translate-x-1/2 w-20 h-5 bg-ink/20 border border-ink/30 rounded-sm rotate-1 shadow-sm"></div>
            
            <img 
              src="/images/simba_cook.png" 
              alt="심바 셰프" 
              className="w-32 h-32 object-contain mb-2 mt-1"
            />
            <p className="font-doodle text-base text-ink text-center">오늘도 고생했어!</p>
            <p className="font-sans text-xs text-ink/60 mt-1 text-center">
              - 심바가 응원합니다 -
            </p>
          </div>
        </div>

        {/* 우측: 거래 내역 리스트 */}
        <div className="lg:col-span-2">
          <div className="bg-white border-2 border-ink rounded-tl-3xl rounded-tr-xl rounded-bl-2xl rounded-br-3xl shadow-[4px_4px_0px_0px_rgba(24,24,27,1)] h-full flex flex-col overflow-hidden">
            
            {/* 영수증 헤더 */}
            <div className="bg-paper border-b-2 border-dashed border-ink/30 p-6">
              <div className="flex items-center gap-3 mb-2">
                <Receipt size={28} strokeWidth={2.5} className="text-ink" />
                <h3 className="font-doodle text-2xl text-ink">거래 내역</h3>
              </div>
              <p className="font-sans text-sm text-ink/60">
                총 {salesData.orderHistory.length}건의 거래
              </p>
            </div>

            {/* 리스트 */}
            <div className="flex-1 overflow-y-auto p-4">
              {salesData.orderHistory.length === 0 ? (
                <div className="flex flex-col items-center justify-center h-full text-ink/40">
                  <Receipt size={48} strokeWidth={2} className="mb-4" />
                  <p className="font-sans text-lg">거래 내역이 없습니다</p>
                </div>
              ) : (
                <div className="space-y-2">
                  {salesData.orderHistory.map((order) => (
                    <OrderItem
                      key={order.orderId}
                      order={order}
                      isExpanded={expandedOrderId === order.orderId}
                      onToggle={() => toggleOrderDetail(order.orderId)}
                      formatTime={formatTime}
                      getOrderSummary={getOrderSummary}
                    />
                  ))}
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

// 개별 주문 아이템 컴포넌트
function OrderItem({
  order,
  isExpanded,
  onToggle,
  formatTime,
  getOrderSummary,
}: {
  order: SellerOrderResponse;
  isExpanded: boolean;
  onToggle: () => void;
  formatTime: (isoString: string) => string;
  getOrderSummary: (items: SellerOrderItemDto[]) => string;
}) {
  const isCanceled = order.status === 'CANCELED';

  return (
    <div
      className={`
        border-2 rounded-tl-xl rounded-tr-2xl rounded-bl-lg rounded-br-xl transition-all
        ${isCanceled 
          ? 'border-dashed border-danger/30 bg-danger/5' 
          : 'border-ink/20 bg-white hover:bg-paper hover:border-ink/40'}
      `}
    >
      {/* 주문 요약 (클릭 가능) */}
      <button
        type="button"
        onClick={onToggle}
        className="w-full p-4 flex items-center gap-4 text-left"
      >
        {/* 시간 */}
        <div className="flex items-center gap-2 min-w-[80px]">
          <Clock size={16} strokeWidth={2.5} className="text-ink/40" />
          <span className="font-sans text-sm text-ink/60">
            {formatTime(order.orderTime)}
          </span>
        </div>

        {/* 대기번호 */}
        <div className={`
          px-3 py-1 rounded-tl-lg rounded-tr-xl rounded-bl-md rounded-br-lg font-sans text-sm font-bold
          ${isCanceled 
            ? 'bg-danger/10 text-danger' 
            : 'bg-crayon/10 text-crayon'}
        `}>
          #{order.waitingNumber}
        </div>

        {/* 주문 요약 */}
        <div className="flex-1 flex items-center gap-3">
          <p className={`font-sans text-base ${isCanceled ? 'text-ink/40 line-through' : 'text-ink'}`}>
            {getOrderSummary(order.items)}
          </p>
          {/* 점선 */}
          <div className="flex-1 border-b border-dotted border-ink/20"></div>
          {isCanceled && (
            <span className="font-sans text-xs text-danger">취소됨</span>
          )}
        </div>

        {/* 금액 */}
        <p className={`font-sans text-lg font-bold min-w-[100px] text-right ${isCanceled ? 'text-danger/60' : 'text-ink'}`}>
          {order.totalPrice.toLocaleString()}원
        </p>

        {/* 확장 아이콘 */}
        {isExpanded ? (
          <ChevronUp size={20} strokeWidth={2.5} className="text-ink/40" />
        ) : (
          <ChevronDown size={20} strokeWidth={2.5} className="text-ink/40" />
        )}
      </button>

      {/* 상세 내역 (Accordion) */}
      {isExpanded && (
        <div className="px-4 pb-4 border-t border-ink/10">
          <div className="pt-3 space-y-2">
            {order.items.map((item, index) => (
              <div key={index} className="flex items-start justify-between py-2 border-b border-dashed border-ink/10 last:border-0">
                <div className="flex-1">
                  <p className="font-sans text-sm text-ink">
                    {item.menuName} <span className="text-ink/60">x {item.quantity}</span>
                  </p>
                  {item.optionSummary && (
                    <p className="font-sans text-xs text-ink/40 mt-1">
                      {item.optionSummary}
                    </p>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
