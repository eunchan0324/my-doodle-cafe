// src/pages/customer/RecommendMenu.tsx
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import CustomerLayout from '../../layouts/CustomerLayout';
import api from '../../api/axios';

type RecommendType = 'BEST' | 'NEW' | 'NONE';

type CustomerRecommendMenuDto = {
  menuName: string;
  recommendType: RecommendType;
};

const RECOMMEND_LABEL: Record<RecommendType, string> = {
  BEST: 'BEST',
  NEW: 'NEW',
  NONE: '',
};

const RECOMMEND_COLOR: Record<RecommendType, string> = {
  BEST: 'bg-crayon text-white',
  NEW: 'bg-doodle-yellow text-ink',
  NONE: 'bg-ink/20 text-ink',
};

export default function RecommendMenu() {
  const navigate = useNavigate();
  const [menus, setMenus] = useState<CustomerRecommendMenuDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [storeName, setStoreName] = useState<string>('');

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      navigate('/login');
      return;
    }

    const storeId = sessionStorage.getItem('selectedStoreId');
    const storedName = sessionStorage.getItem('selectedStoreName');
    
    if (!storeId) {
      navigate('/customer/select_store');
      return;
    }

    setStoreName(storedName || '매장');

    let isMounted = true;

    async function fetchRecommend() {
      try {
        const response = await api.get<CustomerRecommendMenuDto[]>(
          `/api/v1/stores/${storeId}/menus/recommend`
        );
        if (!isMounted) return;
        setMenus(response.data);
        setErrorMessage(null);
      } catch (error) {
        if (!isMounted) return;
        setErrorMessage('추천 메뉴를 불러오지 못했어요.');
      } finally {
        if (!isMounted) return;
        setLoading(false);
      }
    }

    fetchRecommend();
    return () => {
      isMounted = false;
    };
  }, [navigate]);

  const bestMenus = menus.filter((m) => m.recommendType === 'BEST');
  const newMenus = menus.filter((m) => m.recommendType === 'NEW');

  return (
    <CustomerLayout>
      <div className="py-6 px-4">
        <h1 className="font-doodle text-3xl text-ink">추천 메뉴</h1>
        <p className="text-sm text-ink/60 mt-1">{storeName}</p>

        {loading && (
          <div className="card p-6 mt-6">
            <p className="font-sans text-center text-ink/60">
              추천 메뉴를 불러오는 중이에요…
            </p>
          </div>
        )}

        {!loading && errorMessage && (
          <div className="card p-6 mt-6">
            <p className="font-sans text-center text-ink/60">{errorMessage}</p>
          </div>
        )}

        {!loading && !errorMessage && (menus.length === 0 || (bestMenus.length === 0 && newMenus.length === 0)) && (
          <div className="card p-6 mt-6">
            <p className="font-sans text-center text-ink/60">
              아직 추천 메뉴가 없어요.
            </p>
          </div>
        )}

        {!loading && !errorMessage && (bestMenus.length > 0 || newMenus.length > 0) && (
          <div className="mt-6 space-y-6">
            {/* BEST 메뉴 */}
            {bestMenus.length > 0 && (
              <section>
                <div className="flex items-center gap-2 mb-3">
                  <span className={`px-3 py-1 text-sm font-bold rounded-full border-2 border-ink ${RECOMMEND_COLOR.BEST}`}>
                    🔥 BEST
                  </span>
                  <span className="text-sm text-ink/60">인기 메뉴</span>
                </div>
                <div className="space-y-3">
                  {bestMenus.map((menu, index) => (
                    <div
                      key={`best-${index}`}
                      className="card p-4 flex items-center justify-between"
                    >
                      <span className="font-sans text-lg text-ink">
                        {menu.menuName}
                      </span>
                      <span className={`px-2 py-1 text-xs font-bold rounded-full border-2 border-ink ${RECOMMEND_COLOR.BEST}`}>
                        {RECOMMEND_LABEL.BEST}
                      </span>
                    </div>
                  ))}
                </div>
              </section>
            )}

            {/* NEW 메뉴 */}
            {newMenus.length > 0 && (
              <section>
                <div className="flex items-center gap-2 mb-3">
                  <span className={`px-3 py-1 text-sm font-bold rounded-full border-2 border-ink ${RECOMMEND_COLOR.NEW}`}>
                    ✨ NEW
                  </span>
                  <span className="text-sm text-ink/60">신메뉴</span>
                </div>
                <div className="space-y-3">
                  {newMenus.map((menu, index) => (
                    <div
                      key={`new-${index}`}
                      className="card p-4 flex items-center justify-between"
                    >
                      <span className="font-sans text-lg text-ink">
                        {menu.menuName}
                      </span>
                      <span className={`px-2 py-1 text-xs font-bold rounded-full border-2 border-ink ${RECOMMEND_COLOR.NEW}`}>
                        {RECOMMEND_LABEL.NEW}
                      </span>
                    </div>
                  ))}
                </div>
              </section>
            )}
          </div>
        )}
      </div>
    </CustomerLayout>
  );
}
