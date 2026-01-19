// src/pages/customer/MenuList.tsx
import { useEffect, useMemo, useRef, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import CustomerLayout from '../../layouts/CustomerLayout';
import api from '../../api/axios';

type Category = 'COFFEE' | 'BEVERAGE' | 'DESSERT';
type RecommendType = 'BEST' | 'NEW' | 'NONE';
type SalesStatus = 'ON_SALE' | 'STOP' | 'SOLD_OUT';

type CustomerMenuResponse = {
  menuId: string;
  name: string;
  price: number;
  category: Category;
  recommendType: RecommendType;
  status: SalesStatus;
};

type StoreInfo = {
  id: number;
  name: string;
};

type MenuResponse = {
  store: StoreInfo;
  menus: Partial<Record<Category, CustomerMenuResponse[]>>;
};

const CATEGORY_LABEL: Record<Category, string> = {
  COFFEE: '커피',
  BEVERAGE: '음료',
  DESSERT: '디저트',
};

export default function MenuList() {
  const navigate = useNavigate();
  const { storeId } = useParams<{ storeId: string }>();
  const [menus, setMenus] = useState<MenuResponse['menus']>({});
  const [activeCategory, setActiveCategory] = useState<Category>('COFFEE');
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      navigate('/customer/login');
      return;
    }
    let isMounted = true;

    async function fetchMenus() {
      try {
        if (!storeId) {
          setErrorMessage('지점 정보가 없습니다. 다시 선택해주세요.');
          return;
        }
        const response = await api.get<MenuResponse>(`/api/v1/stores/${storeId}/menus`);
        if (!isMounted) return;
        setMenus(response.data.menus ?? {});
        setErrorMessage(null);

        const availableCategories = Object.keys(response.data.menus || {}) as Category[];
        if (availableCategories.length > 0) {
          setActiveCategory(availableCategories[0]);
        }
      } catch (error) {
        if (!isMounted) return;
        setErrorMessage('메뉴를 불러오지 못했어요. 지점을 다시 선택해주세요.');
      } finally {
        if (!isMounted) return;
        setLoading(false);
      }
    }

    fetchMenus();
    return () => {
      isMounted = false;
    };
  }, [navigate]);

  const categoryTabs = useMemo(() => {
    const ordered: Category[] = ['COFFEE', 'BEVERAGE', 'DESSERT'];
    return ordered.filter((category) => {
      const list = menus[category];
      return Array.isArray(list) && list.length > 0;
    });
  }, [menus]);

  const menuList = menus[activeCategory] ?? [];
  const sortedMenuList = useMemo(() => {
    const order: Record<SalesStatus, number> = {
      ON_SALE: 0,
      STOP: 1,
      SOLD_OUT: 2,
    };
    return [...menuList].sort((a, b) => order[a.status] - order[b.status]);
  }, [menuList]);

  const hasRestoredCategory = useRef(false);

  useEffect(() => {
    if (categoryTabs.length === 0) return;
    if (!hasRestoredCategory.current) {
      const stored = sessionStorage.getItem(`customerActiveCategory:${storeId}`) as Category | null;
      if (stored && categoryTabs.includes(stored)) {
        setActiveCategory(stored);
        hasRestoredCategory.current = true;
        return;
      }
      hasRestoredCategory.current = true;
    }
    if (!categoryTabs.includes(activeCategory)) {
      setActiveCategory(categoryTabs[0]);
    }
  }, [categoryTabs, activeCategory]);

  useEffect(() => {
    if (!hasRestoredCategory.current) return;
    if (!storeId) return;
    sessionStorage.setItem(`customerActiveCategory:${storeId}`, activeCategory);
  }, [activeCategory, storeId]);

  return (
    <CustomerLayout>
      <div className="py-6 px-4">
        {/* 상단 이미지 + 헤더 */}
        <div>
        <h1 className="font-doodle text-3xl text-ink">
            메뉴판
          </h1>
          <div className="w-full flex justify-center">
            <img
              src="/images/nala_menus.png"
              alt="메뉴를 고르는 날라"
              className="w-40 h-40 object-contain drop-shadow-md"
            />
          </div>
        </div>

        {/* 카테고리 탭 */}
        {!loading && !errorMessage && categoryTabs.length > 0 && (
          <div className="flex gap-3 overflow-x-auto pb-1">
            {categoryTabs.map((category) => (
              <button
                key={category}
                type="button"
                onClick={() => setActiveCategory(category)}
                className={`px-4 py-2 text-sm font-doodle border-2 border-ink rounded-full transition-all ${
                  activeCategory === category
                    ? 'bg-crayon text-white shadow-[2px_2px_0px_#18181B]'
                    : 'bg-white text-ink'
                }`}
              >
                {CATEGORY_LABEL[category]}
              </button>
            ))}
          </div>
        )}

        <div className="mt-6 grid grid-cols-1 gap-4">
          {loading && (
            <div className="card p-6">
              <p className="font-sans text-center text-ink/60">
                메뉴를 불러오는 중이에요…
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

          {!loading && !errorMessage && menuList.length === 0 && (
            <div className="card p-6">
              <p className="font-sans text-center text-ink/60">
                이 카테고리에 등록된 메뉴가 없어요.
              </p>
            </div>
          )}

          {!loading && !errorMessage && sortedMenuList.length > 0 && (
            <div className="grid grid-cols-1 gap-4">
              {sortedMenuList.map((menu) => {
                const isAvailable = menu.status === 'ON_SALE';
                const isRecommended =
                  menu.recommendType === 'BEST' || menu.recommendType === 'NEW';
                const recommendBadge = isRecommended ? menu.recommendType : null;

                return (
                  <button
                    key={menu.menuId}
                    type="button"
                    onClick={() => navigate(`/customer/stores/${storeId}/menus/${menu.menuId}`)}
                    aria-disabled={!isAvailable}
                    className={`card p-4 text-left transition-all duration-150 shadow-none translate-x-1 translate-y-1 hover:translate-x-1 hover:translate-y-1 hover:shadow-none ${
                      isAvailable ? 'bg-white' : 'bg-white/70 grayscale opacity-70'
                    }`}
                  >
                    <div className="flex items-center gap-4">
                      <div className="w-16 h-16 bg-paper border-2 border-ink rounded-[14px_10px_16px_8px/8px_16px_10px_14px] flex items-center justify-center">
                        <span className="text-2xl">🍩</span>
                      </div>
                      <div className="flex-1">
                        <div className="flex items-center gap-2">
                          <h2 className="font-sans text-lg font-semibold text-ink">
                            {menu.name}
                          </h2>
                          {recommendBadge && (
                            <span className="inline-block px-2 py-0.5 text-xs font-bold text-ink border-2 border-ink rounded-full bg-doodle-yellow">
                              {recommendBadge}
                            </span>
                          )}
                          {!isAvailable && (
                            <span className="inline-block px-2 py-0.5 text-xs font-bold text-white border-2 border-ink rounded-full bg-red-500">
                              품절
                            </span>
                          )}
                        </div>
                        <p className="mt-1 text-sm text-ink/60">
                          {menu.price.toLocaleString()}원
                        </p>
                      </div>
                    </div>
                  </button>
                );
              })}
            </div>
          )}
        </div>
      </div>
    </CustomerLayout>
  );
}
