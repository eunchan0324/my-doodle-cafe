// src/pages/customer/Favorites.tsx
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { ChevronLeft, Heart } from 'lucide-react';
import CustomerLayout from '../../layouts/CustomerLayout';
import api from '../../api/axios';

type Category = 'COFFEE' | 'BEVERAGE' | 'DESSERT';

type FavoriteMenuResponse = {
  menuId: string;
  menuName: string;
  price: number;
  category: Category;
  createdAt: string;
};

const CATEGORY_LABEL: Record<Category, string> = {
  COFFEE: '커피',
  BEVERAGE: '음료',
  DESSERT: '디저트',
};

export default function Favorites() {
  const navigate = useNavigate();
  const [favorites, setFavorites] = useState<FavoriteMenuResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [togglingMenuId, setTogglingMenuId] = useState<string | null>(null);

  const storeId = sessionStorage.getItem('selectedStoreId');

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      navigate('/login');
      return;
    }

    if (!storeId) {
      navigate('/customer/select_store');
      return;
    }

    let isMounted = true;

    async function fetchFavorites() {
      try {
        const response = await api.get<FavoriteMenuResponse[]>('/api/v1/favorites');
        if (!isMounted) return;
        setFavorites(response.data);
        setErrorMessage(null);
      } catch (error) {
        if (!isMounted) return;
        setErrorMessage('찜 목록을 불러오지 못했어요.');
      } finally {
        if (!isMounted) return;
        setLoading(false);
      }
    }

    fetchFavorites();
    return () => {
      isMounted = false;
    };
  }, [navigate, storeId]);

  const handleGoToMenu = (menuId: string) => {
    if (storeId) {
      navigate(`/customer/stores/${storeId}/menus/${menuId}`);
    }
  };

  const handleToggleFavorite = async (menuId: string, event: React.MouseEvent) => {
    event.stopPropagation(); // 카드 클릭 이벤트 방지
    if (!storeId) return;

    setTogglingMenuId(menuId);
    try {
      await api.post(`/api/v1/stores/${storeId}/menus/${menuId}/toggle-favorite`);
      // 목록에서 제거
      setFavorites((prev) => prev.filter((item) => item.menuId !== menuId));
    } catch (error) {
      setErrorMessage('찜 취소에 실패했어요.');
    } finally {
      setTogglingMenuId(null);
    }
  };

  return (
    <CustomerLayout>
      <div className="py-6 px-4">
        <div className="flex items-center gap-3 mb-6">
          <button
            type="button"
            onClick={() => navigate(-1)}
            className="inline-flex items-center text-ink"
            aria-label="뒤로가기"
          >
            <ChevronLeft className="h-6 w-6" strokeWidth={2.5} />
          </button>
          <h1 className="font-doodle text-3xl text-ink">찜 목록</h1>
        </div>

        {loading && (
          <div className="card p-6">
            <p className="font-sans text-center text-ink/60">
              찜 목록을 불러오는 중이에요…
            </p>
          </div>
        )}

        {!loading && errorMessage && (
          <div className="card p-6">
            <p className="font-sans text-center text-ink/60">{errorMessage}</p>
          </div>
        )}

        {!loading && !errorMessage && favorites.length === 0 && (
          <div className="card p-6 text-center space-y-4">
            <Heart className="h-12 w-12 mx-auto text-ink/30" />
            <p className="font-sans text-ink/60">
              아직 찜한 메뉴가 없어요.
            </p>
            <button
              type="button"
              onClick={() => {
                const storeId = sessionStorage.getItem('selectedStoreId');
                if (storeId) {
                  navigate(`/customer/stores/${storeId}/menus`);
                } else {
                  navigate('/customer/select_store');
                }
              }}
              className="btn btn-primary"
            >
              메뉴 보러가기
            </button>
          </div>
        )}

        {!loading && !errorMessage && favorites.length > 0 && (
          <div className="space-y-3">
            {favorites.map((item) => (
              <div
                key={item.menuId}
                className="card p-4 w-full flex items-center justify-between"
              >
                <button
                  type="button"
                  onClick={() => handleGoToMenu(item.menuId)}
                  className="flex-1 text-left"
                >
                  <div className="flex items-center gap-2">
                    <h2 className="font-sans text-lg font-semibold text-ink">
                      {item.menuName}
                    </h2>
                    <span className="text-xs text-ink/50 bg-ink/10 px-2 py-0.5 rounded-full">
                      {CATEGORY_LABEL[item.category]}
                    </span>
                  </div>
                  <p className="text-sm text-ink/60 mt-1">
                    {item.price.toLocaleString()}원
                  </p>
                </button>
                <button
                  type="button"
                  onClick={(e) => handleToggleFavorite(item.menuId, e)}
                  disabled={togglingMenuId === item.menuId}
                  className="p-2 -mr-2 transition-transform active:scale-90"
                  aria-label="찜 취소"
                >
                  <Heart
                    className={`h-6 w-6 text-crayon ${togglingMenuId === item.menuId ? 'opacity-50' : ''}`}
                    fill="currentColor"
                  />
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
    </CustomerLayout>
  );
}
