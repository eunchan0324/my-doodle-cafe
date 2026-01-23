// src/pages/customer/Cart.tsx
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { ChevronLeft, Minus, Plus, Trash2 } from 'lucide-react';
import CustomerLayout from '../../layouts/CustomerLayout';
import api from '../../api/axios';
import {
  getCart,
  updateCartItemQuantity,
  removeFromCart,
  clearCart,
  calcItemTotalPrice,
  calcCartTotalPrice,
  CUP_PRICE_DELTA,
  SHOT_PRICE_DELTA,
  type CartItem,
} from '../../utils/cart';

const CUP_LABEL: Record<string, string> = {
  DISPOSABLE: '일회용컵',
  STORE: '매장컵',
  PERSONAL: '개인컵',
};

const SHOT_LABEL: Record<string, string> = {
  NONE: '없음',
  BASIC: '기본',
  LIGHT: '연하게',
  EXTRA: '샷 추가',
  DECAFFEINATED: '디카페인',
};

const TEMP_LABEL: Record<string, string> = {
  ICE: 'ICE',
  HOT: 'HOT',
};

type OrderResponse = {
  orderId: string;
  waitingNumber: number;
  message: string;
};

export default function Cart() {
  const navigate = useNavigate();
  const [cart, setCart] = useState<CartItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      navigate('/login');
      return;
    }
    setCart(getCart());
  }, [navigate]);

  const refreshCart = () => {
    setCart(getCart());
  };

  const handleQuantityChange = (index: number, delta: number) => {
    const item = cart[index];
    if (!item) return;
    const newQuantity = item.quantity + delta;
    if (newQuantity < 1) return;
    updateCartItemQuantity(index, newQuantity);
    refreshCart();
  };

  const handleRemove = (index: number) => {
    removeFromCart(index);
    refreshCart();
  };

  const handleClearCart = () => {
    clearCart();
    refreshCart();
  };

  const handleOrder = async () => {
    if (cart.length === 0) return;

    // 모든 아이템이 같은 storeId인지 체크 (다르면 에러)
    const storeIds = [...new Set(cart.map((item) => item.storeId))];
    if (storeIds.length > 1) {
      setErrorMessage('여러 매장의 메뉴를 한 번에 주문할 수 없어요. 장바구니를 확인해주세요.');
      return;
    }

    const storeId = storeIds[0];

    const requestBody = {
      storeId,
      items: cart.map((item) => ({
        menuId: item.menuId,
        quantity: item.quantity,
        cupType: item.cupType,
        temperature: item.temperature,
        shotOption: item.shotOption,
      })),
    };

    try {
      setLoading(true);
      setErrorMessage(null);
      const response = await api.post<OrderResponse>('/api/v1/orders', requestBody);
      clearCart();
      navigate('/customer/order-complete', {
        state: {
          orderId: response.data.orderId,
          waitingNumber: response.data.waitingNumber,
          message: response.data.message,
        },
      });
    } catch (error) {
      setErrorMessage('주문에 실패했어요. 다시 시도해주세요.');
    } finally {
      setLoading(false);
    }
  };

  const totalPrice = calcCartTotalPrice(cart);

  return (
    <CustomerLayout>
      <div className="py-6 px-4">
        <div className="flex items-center justify-between mb-6">
          <button
            type="button"
            onClick={() => navigate(-1)}
            className="inline-flex items-center gap-1 text-ink font-doodle"
            aria-label="뒤로가기"
          >
            <ChevronLeft className="h-6 w-6" strokeWidth={2.5} />
          </button>
          <h1 className="font-doodle text-2xl text-ink">장바구니</h1>
          <div className="w-6" />
        </div>

        {cart.length === 0 ? (
          <div className="card p-6">
            <p className="font-sans text-center text-ink/60">
              장바구니가 비어있어요.
            </p>
            <button
              type="button"
              onClick={() => navigate(-1)}
              className="btn btn-primary w-full mt-4"
            >
              메뉴 보러가기
            </button>
          </div>
        ) : (
          <div className="space-y-4">
            {cart.map((item, index) => {
              const optionDelta =
                CUP_PRICE_DELTA[item.cupType] + SHOT_PRICE_DELTA[item.shotOption];
              const unitPrice = item.basePrice + optionDelta;
              const itemTotal = calcItemTotalPrice(item);

              return (
                <div key={`${item.menuId}-${index}`} className="card p-4">
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <h2 className="font-sans text-lg font-semibold text-ink">
                        {item.name}
                      </h2>
                      <p className="text-sm text-ink/60 mt-1">
                        {TEMP_LABEL[item.temperature]} · {CUP_LABEL[item.cupType]}
                        {item.shotOption !== 'NONE' && ` · ${SHOT_LABEL[item.shotOption]}`}
                      </p>
                      <p className="text-sm text-ink/80 mt-1">
                        {unitPrice.toLocaleString()}원 × {item.quantity}
                      </p>
                    </div>
                    <button
                      type="button"
                      onClick={() => handleRemove(index)}
                      className="text-ink/40 hover:text-danger"
                      aria-label="삭제"
                    >
                      <Trash2 className="h-5 w-5" />
                    </button>
                  </div>

                  <div className="flex items-center justify-between mt-3">
                    <div className="flex items-center gap-3">
                      <button
                        type="button"
                        onClick={() => handleQuantityChange(index, -1)}
                        className="w-8 h-8 flex items-center justify-center border-2 border-ink rounded-full"
                        disabled={item.quantity <= 1}
                      >
                        <Minus className="h-4 w-4" />
                      </button>
                      <span className="font-sans text-base w-6 text-center">
                        {item.quantity}
                      </span>
                      <button
                        type="button"
                        onClick={() => handleQuantityChange(index, 1)}
                        className="w-8 h-8 flex items-center justify-center border-2 border-ink rounded-full"
                      >
                        <Plus className="h-4 w-4" />
                      </button>
                    </div>
                    <span className="font-sans font-semibold text-ink">
                      {itemTotal.toLocaleString()}원
                    </span>
                  </div>
                </div>
              );
            })}

            <div className="flex justify-end">
              <button
                type="button"
                onClick={handleClearCart}
                className="text-sm text-ink/50 underline"
              >
                전체 비우기
              </button>
            </div>

            <div className="card p-4">
              <div className="flex items-center justify-between font-sans text-lg">
                <span className="text-ink">총 결제 금액</span>
                <span className="font-bold text-ink">
                  {totalPrice.toLocaleString()}원
                </span>
              </div>
            </div>

            {errorMessage && (
              <p className="text-sm text-danger text-center">{errorMessage}</p>
            )}

            <button
              type="button"
              onClick={handleOrder}
              className="btn btn-crayon w-full text-lg"
              disabled={loading || cart.length === 0}
            >
              {loading ? '주문 중...' : `${totalPrice.toLocaleString()}원 주문하기`}
            </button>
          </div>
        )}
      </div>
    </CustomerLayout>
  );
}
