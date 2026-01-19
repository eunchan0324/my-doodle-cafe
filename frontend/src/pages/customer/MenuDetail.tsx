import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { ChevronLeft, Heart } from 'lucide-react';
import CustomerLayout from '../../layouts/CustomerLayout';
import api from '../../api/axios';

type Category = 'COFFEE' | 'BEVERAGE' | 'DESSERT';
type RecommendType = 'BEST' | 'NEW' | 'NONE';
type SalesStatus = 'ON_SALE' | 'STOP' | 'SOLD_OUT';
type Temperature = 'ICE' | 'HOT';
type CupType = 'DISPOSABLE' | 'STORE' | 'PERSONAL';
type ShotOption = 'NONE' | 'BASIC' | 'LIGHT' | 'EXTRA' | 'DECAFFEINATED';

type CustomerMenuDetailResponse = {
  menuId: string;
  name: string;
  price: number;
  description?: string | null;
  category?: Category;
  recommendType?: RecommendType | null;
  status?: SalesStatus | null;
  isFavorite?: boolean;
};

const TEMPERATURE_OPTIONS: { value: Temperature; label: string }[] = [
  { value: 'ICE', label: 'ICE' },
  { value: 'HOT', label: 'HOT' },
];

const CUP_OPTIONS: { value: CupType; label: string; priceDelta: number }[] = [
  { value: 'DISPOSABLE', label: '일회용컵', priceDelta: 0 },
  { value: 'STORE', label: '매장컵', priceDelta: 0 },
  { value: 'PERSONAL', label: '개인컵', priceDelta: -300 },
];

const SHOT_OPTIONS: { value: ShotOption; label: string; priceDelta: number }[] = [
  { value: 'NONE', label: '없음', priceDelta: 0 },
  { value: 'BASIC', label: '기본', priceDelta: 0 },
  { value: 'LIGHT', label: '연하게', priceDelta: 0 },
  { value: 'EXTRA', label: '샷 추가', priceDelta: 500 },
  { value: 'DECAFFEINATED', label: '디카페인', priceDelta: 1000 },
];

export default function MenuDetail() {
  const { storeId, menuId } = useParams<{ storeId: string; menuId: string }>();
  const navigate = useNavigate();
  const [menu, setMenu] = useState<CustomerMenuDetailResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [actionMessage, setActionMessage] = useState<string | null>(null);
  const [quantity, setQuantity] = useState(1);
  const [temperature, setTemperature] = useState<Temperature>('ICE');
  const [cupType, setCupType] = useState<CupType>('DISPOSABLE');
  const [shotOption, setShotOption] = useState<ShotOption>('NONE');

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      navigate('/customer/login');
      return;
    }
    let isMounted = true;

    async function fetchMenuDetail() {
      if (!storeId || !menuId) {
        setErrorMessage('메뉴 정보를 찾을 수 없어요.');
        setLoading(false);
        return;
      }

      try {
        const response = await api.get<CustomerMenuDetailResponse>(
          `/api/v1/stores/${storeId}/menus/${menuId}`,
        );
        if (!isMounted) return;
        setMenu(response.data);
        setErrorMessage(null);
      } catch (error) {
        if (!isMounted) return;
        setErrorMessage('메뉴 상세 정보를 불러오지 못했어요.');
      } finally {
        if (!isMounted) return;
        setLoading(false);
      }
    }

    fetchMenuDetail();
    return () => {
      isMounted = false;
    };
  }, [menuId, navigate]);

  useEffect(() => {
    if (!menu) return;
    if (menu.category !== 'COFFEE') {
      setShotOption('NONE');
    } else if (shotOption === 'NONE') {
      setShotOption('BASIC');
    }
  }, [menu, shotOption]);

  const recommendBadge =
    menu?.recommendType === 'BEST'
      ? 'BEST'
      : menu?.recommendType === 'NEW'
        ? 'NEW'
        : null;

  const isUnsellable = menu?.status === 'SOLD_OUT' || menu?.status === 'STOP';
  const selectedCup = CUP_OPTIONS.find((option) => option.value === cupType);
  const selectedShot = SHOT_OPTIONS.find((option) => option.value === shotOption);
  const optionDelta = (selectedCup?.priceDelta ?? 0) + (selectedShot?.priceDelta ?? 0);
  const totalPrice = menu ? (menu.price + optionDelta) * quantity : 0;

  const handleToggleFavorite = async () => {
    if (!storeId || !menuId || !menu) return;
    try {
      const response = await api.post(
        `/api/v1/stores/${storeId}/menus/${menuId}/toggle-favorite`,
      );
      const nextFavorite =
        typeof response.data?.isFavorite === 'boolean'
          ? response.data.isFavorite
          : !menu.isFavorite;
      setMenu({ ...menu, isFavorite: nextFavorite });
      setActionMessage(response.data?.message ?? null);
    } catch (error) {
      setActionMessage('찜 상태를 변경하지 못했어요.');
    }
  };

  return (
    <CustomerLayout>
      <div className="py-6 px-4">
        {loading && (
          <div className="card p-6">
            <p className="font-sans text-center text-ink/60">
              메뉴 정보를 불러오는 중이에요…
            </p>
          </div>
        )}

        {!loading && errorMessage && (
          <div className="card p-6">
            <p className="font-sans text-center text-ink/60">
              {errorMessage}
            </p>
          </div>
        )}

        {!loading && !errorMessage && menu && (
          <div className="space-y-6">
            <div className="flex items-center justify-between">
              <button
                type="button"
                onClick={() => navigate(-1)}
                className="inline-flex items-center gap-1 text-ink font-doodle"
                aria-label="뒤로가기"
              >
                <ChevronLeft className="h-6 w-6" strokeWidth={2.5} />
              </button>
              <button
                type="button"
                onClick={handleToggleFavorite}
                className="inline-flex items-center gap-1 text-ink"
                aria-label="찜하기"
              >
                <Heart
                  className="h-6 w-6"
                  strokeWidth={2.5}
                  fill={menu.isFavorite ? 'currentColor' : 'none'}
                />
              </button>
            </div>

            <header className="space-y-2">
              <h1 className="font-doodle text-3xl text-ink">
                {menu.name}
              </h1>
              
              <p className="text-sm text-ink/60">
              {recommendBadge && (
                <span className="inline-block px-2 py-1 text-xs font-bold text-ink border-2 border-ink rounded-full bg-doodle-yellow">
                  {recommendBadge}
                </span>
              )}
                - {menu.description || '메뉴 설명이 준비중이에요.'}
                
              </p>
              <p className="text-lg font-semibold text-ink">
                {menu.price.toLocaleString()}원
              </p>
            </header>

            <section className="card p-5 space-y-4">
              <h2 className="font-doodle text-xl text-ink">
                주문서
              </h2>
              {actionMessage && (
                <p className="text-sm text-ink/60">
                  {actionMessage}
                </p>
              )}
              <div className="space-y-4">
                <div>
                  <h3 className="font-doodle text-lg text-ink">
                    수량
                  </h3>
                  <input
                    type="number"
                    min={1}
                    value={quantity}
                    onChange={(event) => setQuantity(Number(event.target.value) || 1)}
                    className="mt-2 w-full border-2 border-ink rounded-xl px-3 py-2 font-sans text-base"
                  />
                </div>

                <div>
                  <h3 className="font-doodle text-lg text-ink">
                    온도 선택
                  </h3>
                  <select
                    value={temperature}
                    onChange={(event) => setTemperature(event.target.value as Temperature)}
                    className="mt-2 w-full border-2 border-ink rounded-xl px-3 py-2 font-sans text-base"
                  >
                    {TEMPERATURE_OPTIONS.map((option) => (
                      <option key={option.value} value={option.value}>
                        {option.label}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <h3 className="font-doodle text-lg text-ink">
                    컵 종류
                  </h3>
                  <select
                    value={cupType}
                    onChange={(event) => setCupType(event.target.value as CupType)}
                    className="mt-2 w-full border-2 border-ink rounded-xl px-3 py-2 font-sans text-base"
                  >
                    {CUP_OPTIONS.map((option) => (
                      <option key={option.value} value={option.value}>
                        {option.label}
                        {option.priceDelta !== 0
                          ? ` (${option.priceDelta > 0 ? '+' : ''}${option.priceDelta.toLocaleString()}원)`
                          : ''}
                      </option>
                    ))}
                  </select>
                </div>

                {menu.category === 'COFFEE' ? (
                  <div>
                    <h3 className="font-doodle text-lg text-ink">
                      샷 옵션
                    </h3>
                    <select
                      value={shotOption}
                      onChange={(event) => setShotOption(event.target.value as ShotOption)}
                      className="mt-2 w-full border-2 border-ink rounded-xl px-3 py-2 font-sans text-base"
                    >
                      {SHOT_OPTIONS.filter((option) => option.value !== 'NONE').map((option) => (
                        <option key={option.value} value={option.value}>
                          {option.label}
                          {option.priceDelta > 0
                            ? ` (+${option.priceDelta.toLocaleString()}원)`
                            : ''}
                        </option>
                      ))}
                    </select>
                  </div>
                ) : null}
              </div>

              <div className="flex items-center justify-between font-sans text-base text-ink">
                <span>총 금액</span>
                <span className="font-semibold">
                  {totalPrice.toLocaleString()}원
                </span>
              </div>

              <button
                type="button"
                className="btn btn-primary w-full"
                disabled={isUnsellable}
              >
                {isUnsellable ? '[판매 불가] 주문 불가' : '장바구니에 담기'}
              </button>

              {isUnsellable && (
                <div className="text-sm">
                  {menu.status === 'SOLD_OUT' && (
                    <p className="text-danger">
                      * 현재 품절 상태입니다. 장바구니에 담을 수 없습니다.
                    </p>
                  )}
                  {menu.status === 'STOP' && (
                    <p className="text-ink/60">
                      * 현재 판매 중지 상태입니다. 장바구니에 담을 수 없습니다.
                    </p>
                  )}
                </div>
              )}
            </section>

          </div>
        )}
      </div>
    </CustomerLayout>
  );
}
