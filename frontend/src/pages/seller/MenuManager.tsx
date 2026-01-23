// src/pages/seller/MenuManager.tsx
// 판매자 메뉴 관리 - 2 Tab 구조

import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Search, Coffee, Plus } from 'lucide-react';
import api from '../../api/axios';

// 타입 정의 (백엔드 DTO와 일치)
type SalesStatus = 'ON_SALE' | 'STOP' | 'SOLD_OUT';
type RecommendType = 'NONE' | 'BEST' | 'NEW' | 'SEASON';
type Category = 'COFFEE' | 'BEVERAGE' | 'DESSERT';

// SellerMenuResponse (Tab 1: 우리 가게 메뉴)
type StoreMenu = {
  menuId: string;
  name: string;
  price: number;
  category: Category;
  stock: number;
  status: SalesStatus;
  recommendType: RecommendType;
};

// SellerMenuManageDto (Tab 2: 전체 메뉴)
type AllMenu = {
  menuId: string;
  name: string;
  price: number;
  category: Category;
  selling: boolean; // 백엔드는 'selling' 필드 사용
};

// Request DTOs
type SellerMenuUpdateRequest = {
  stock: number;
  status: SalesStatus;
};

type SellerRecommendUpdateRequest = {
  recommendType: RecommendType;
};

const CATEGORY_LABEL: Record<Category, string> = {
  COFFEE: '커피',
  BEVERAGE: '음료',
  DESSERT: '디저트',
};

export default function MenuManager() {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState<'MY_MENU' | 'ADD_MENU'>('MY_MENU');
  const [storeMenus, setStoreMenus] = useState<StoreMenu[]>([]);
  const [allMenus, setAllMenus] = useState<AllMenu[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<Category | 'ALL'>('ALL');

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

    fetchStoreMenus();
  }, [navigate, storeId]);

  const fetchStoreMenus = async () => {
    if (!storeId) return;

    try {
      const response = await api.get<StoreMenu[]>(`/api/v1/stores/${storeId}/management/menus`);
      setStoreMenus(response.data);
    } catch (error) {
      console.error('메뉴 목록 조회 실패:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchAllMenus = async () => {
    if (!storeId) return;

    try {
      const response = await api.get<AllMenu[]>(`/api/v1/stores/${storeId}/management/menus/all`);
      setAllMenus(response.data);
    } catch (error) {
      console.error('전체 메뉴 조회 실패:', error);
    }
  };

  // Tab 전환 시 데이터 로드
  useEffect(() => {
    if (activeTab === 'ADD_MENU' && allMenus.length === 0) {
      fetchAllMenus();
    }
  }, [activeTab]);

  const filteredMenus = storeMenus.filter((menu) => {
    const matchesSearch = menu.name.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesCategory = selectedCategory === 'ALL' || menu.category === selectedCategory;
    return matchesSearch && matchesCategory;
  });

  if (loading) {
    return (
      <div className="h-full flex items-center justify-center">
        <p className="font-sans text-ink/60">메뉴 목록을 불러오는 중...</p>
      </div>
    );
  }

  return (
    <div className="h-full flex flex-col p-6">
      {/* 헤더 & 탭 */}
      <div className="flex justify-between items-end mb-8">
        <div>
          <h1 className="font-doodle text-4xl text-ink mb-3">메뉴 관리</h1>
          <p className="font-sans text-ink/60 text-base">재고, 판매 상태, 추천 설정을 한 곳에서 관리하세요.</p>
        </div>

        {/* 탭 버튼 */}
        <div className="flex gap-3">
          <TabButton
            label="우리 가게 메뉴판"
            isActive={activeTab === 'MY_MENU'}
            onClick={() => setActiveTab('MY_MENU')}
          />
          <TabButton
            label="+ 새 메뉴 가져오기"
            isActive={activeTab === 'ADD_MENU'}
            onClick={() => setActiveTab('ADD_MENU')}
          />
        </div>
      </div>

      {/* 메인 컨텐츠 */}
      <div className="flex-1 bg-white border-2 border-ink rounded-2xl p-6 shadow-doodle overflow-hidden flex flex-col">
        {activeTab === 'MY_MENU' ? (
          <>
            {/* 검색 & 필터 */}
            <div className="flex gap-4 mb-6">
              <div className="relative flex-1">
                <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-ink/40" size={22} />
                <input
                  type="text"
                  placeholder="메뉴명 검색..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="w-full pl-12 pr-4 py-4 text-base bg-paper border-2 border-transparent focus:border-crayon rounded-xl outline-none transition-all font-sans"
                />
              </div>
              <select
                value={selectedCategory}
                onChange={(e) => setSelectedCategory(e.target.value as Category | 'ALL')}
                className="px-6 py-4 text-base border-2 border-ink/20 rounded-xl font-sans text-ink min-w-[160px]"
              >
                <option value="ALL">전체 카테고리</option>
                <option value="COFFEE">커피</option>
                <option value="BEVERAGE">음료</option>
                <option value="DESSERT">디저트</option>
              </select>
            </div>

            {/* 메뉴 리스트 - 태블릿 최적화 */}
            <div className="flex-1 overflow-y-auto pr-2 grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4 auto-rows-max">
              {filteredMenus.map((menu) => (
                <MenuControlCard
                  key={menu.menuId}
                  menu={menu}
                  onUpdate={fetchStoreMenus}
                />
              ))}
            </div>
          </>
        ) : (
          <AddMenuTab
            allMenus={allMenus}
            onUpdate={() => {
              fetchStoreMenus();
              fetchAllMenus();
            }}
          />
        )}
      </div>
    </div>
  );
}

// 탭 버튼
function TabButton({
  label,
  isActive,
  onClick,
}: {
  label: string;
  isActive: boolean;
  onClick: () => void;
}) {
  return (
    <button
      type="button"
      onClick={onClick}
      className={`
        px-8 py-4 rounded-t-xl font-doodle text-xl border-t-2 border-x-2 border-ink transition-all
        ${isActive
          ? 'bg-white text-crayon -mb-[2px] pb-5 z-10 font-bold'
          : 'bg-ink/5 text-ink/40 border-transparent hover:bg-ink/10'}
      `}
    >
      {label}
    </button>
  );
}

// 메뉴 통합 관리 카드
function MenuControlCard({
  menu,
  onUpdate,
}: {
  menu: StoreMenu;
  onUpdate: () => void;
}) {
  const storeId = sessionStorage.getItem('sellerStoreId');
  const [localStock, setLocalStock] = useState(menu.stock);
  const [isEditingStock, setIsEditingStock] = useState(false);
  
  const isHidden = menu.status === 'STOP';
  const isSoldOut = menu.status === 'SOLD_OUT';

  // menu.stock이 변경되면 localStock도 동기화
  useEffect(() => {
    setLocalStock(menu.stock);
  }, [menu.stock]);

  const handleStatusToggle = async (newStatus: SalesStatus) => {
    if (!storeId) return;

    try {
      const payload: SellerMenuUpdateRequest = {
        stock: localStock,
        status: newStatus,
      };
      await api.patch(`/api/v1/stores/${storeId}/management/menus/${menu.menuId}/update`, payload);
      onUpdate();
    } catch (error) {
      console.error('상태 변경 실패:', error);
    }
  };

  const handleStockUpdate = async () => {
    if (!storeId || localStock === menu.stock) {
      setIsEditingStock(false);
      return;
    }

    try {
      const payload: SellerMenuUpdateRequest = {
        stock: localStock,
        status: menu.status,
      };
      await api.patch(`/api/v1/stores/${storeId}/management/menus/${menu.menuId}/update`, payload);
      setIsEditingStock(false);
      onUpdate();
    } catch (error) {
      console.error('재고 변경 실패:', error);
      setLocalStock(menu.stock); // 실패 시 원래 값으로 복구
      setIsEditingStock(false);
    }
  };

  const handleRecommendToggle = async (newType: RecommendType) => {
    if (!storeId) return;

    try {
      const payload: SellerRecommendUpdateRequest = {
        recommendType: newType,
      };
      await api.patch(`/api/v1/stores/${storeId}/management/menus/${menu.menuId}/recommend`, payload);
      onUpdate();
    } catch (error) {
      console.error('추천 변경 실패:', error);
    }
  };

  return (
    <div
      className={`
      flex flex-col gap-4 p-5 border-2 rounded-xl transition-all min-h-[180px]
      ${isHidden
          ? 'border-dashed border-ink/30 bg-ink/5 opacity-60'
          : 'border-ink bg-white shadow-doodle'}
    `}
    >
      {/* 상단: 메뉴 정보 */}
      <div className="flex items-start gap-3">
        <div className="w-20 h-20 bg-ink/5 rounded-lg flex items-center justify-center border border-ink/20 flex-shrink-0">
          <Coffee size={28} className="text-ink/40" />
        </div>
        <div className="flex-1 min-w-0">
          <h3 className="font-doodle text-2xl text-ink truncate">{menu.name}</h3>
          <p className="font-sans text-base text-ink/60 mt-1">
            {menu.price.toLocaleString()}원
          </p>
          <p className="font-sans text-sm text-ink/40 mt-0.5">
            {CATEGORY_LABEL[menu.category]}
          </p>
        </div>
      </div>

      {/* 중단: 재고 관리 */}
      <div className="flex items-center gap-2 bg-paper px-3 py-2 rounded-lg">
        <span className="font-sans text-sm font-medium text-ink/60">재고:</span>
        {isEditingStock ? (
          <div className="flex items-center gap-2">
            <input
              type="number"
              value={localStock}
              onChange={(e) => setLocalStock(Number(e.target.value))}
              onBlur={handleStockUpdate}
              onKeyDown={(e) => {
                if (e.key === 'Enter') handleStockUpdate();
                if (e.key === 'Escape') {
                  setLocalStock(menu.stock);
                  setIsEditingStock(false);
                }
              }}
              autoFocus
              className="w-24 px-3 py-2 text-base border-2 border-crayon rounded-lg focus:outline-none font-sans"
            />
            <span className="font-sans text-sm text-ink/60">개</span>
          </div>
        ) : (
          <button
            type="button"
            onClick={() => setIsEditingStock(true)}
            className="flex items-center gap-2 px-3 py-1.5 rounded-lg hover:bg-crayon/10 transition-colors"
          >
            <span className="font-sans text-base font-bold text-ink">{localStock}개</span>
            <span className="font-sans text-sm text-ink/40">(수정)</span>
          </button>
        )}
      </div>

      {/* 하단: 컨트롤 패널 */}
      <div className="flex flex-col gap-3 pt-3 border-t border-ink/10">
        {/* 상태 표시 */}
        {isSoldOut && (
          <div className="px-3 py-2 rounded-lg font-sans text-sm bg-danger/10 text-danger border border-danger/30 text-center font-medium">
            품절 (재고 부족)
          </div>
        )}
        
        {/* 판매 중지 버튼 */}
        <button
          type="button"
          onClick={() => handleStatusToggle(isHidden ? 'ON_SALE' : 'STOP')}
          className={`
           w-full px-4 py-3 rounded-lg font-sans text-base border-2 transition-all font-medium
           ${isHidden
              ? 'bg-ink text-white border-ink'
              : 'bg-white text-ink border-ink hover:bg-ink hover:text-white'}
         `}
        >
          {isHidden ? '판매 시작' : '판매 중지'}
        </button>

        {/* 추천 태그 */}
        <div className="flex gap-2">
          {(['BEST', 'NEW', 'SEASON'] as const).map((tag) => {
            const isActive = menu.recommendType === tag;
            const labels: Record<typeof tag, string> = {
              BEST: '베스트',
              NEW: '신메뉴',
              SEASON: '시즌',
            };
            
            return (
              <button
                key={tag}
                type="button"
                onClick={() => handleRecommendToggle(isActive ? 'NONE' : tag)}
                className={`
                 flex-1 px-3 py-2 rounded-lg border-2 font-sans text-sm transition-all whitespace-nowrap font-medium
                 ${isActive
                    ? 'bg-crayon border-crayon text-white'
                    : 'bg-white border-ink/20 text-ink/60 hover:border-crayon hover:text-crayon'}
               `}
              >
                {labels[tag]}
              </button>
            );
          })}
        </div>
      </div>
    </div>
  );
}

// Tab 2: 새 메뉴 가져오기
function AddMenuTab({
  allMenus,
  onUpdate,
}: {
  allMenus: AllMenu[];
  onUpdate: () => void;
}) {
  const storeId = sessionStorage.getItem('sellerStoreId');
  const [selectedMenuIds, setSelectedMenuIds] = useState<string[]>([]);

  useEffect(() => {
    // 이미 판매 중인 메뉴는 선택 상태로
    const selling = allMenus.filter((m) => m.selling).map((m) => m.menuId);
    setSelectedMenuIds(selling);
  }, [allMenus]);

  const handleToggle = (menuId: string) => {
    setSelectedMenuIds((prev) =>
      prev.includes(menuId) ? prev.filter((id) => id !== menuId) : [...prev, menuId]
    );
  };

  const handleSave = async () => {
    if (!storeId) return;

    try {
      await api.post(`/api/v1/stores/${storeId}/management/menus/batch-update`, selectedMenuIds);
      onUpdate();
      alert('판매 메뉴 목록이 업데이트되었습니다!');
    } catch (error) {
      console.error('일괄 업데이트 실패:', error);
      alert('업데이트에 실패했습니다.');
    }
  };

  if (allMenus.length === 0) {
    return (
      <div className="flex-1 flex flex-col items-center justify-center text-ink/40">
        <div className="w-20 h-20 bg-ink/5 rounded-full flex items-center justify-center mb-4">
          <Plus size={40} strokeWidth={2.5} />
        </div>
        <p className="font-doodle text-xl">전체 메뉴를 불러오는 중...</p>
      </div>
    );
  }

  return (
    <div className="flex-1 flex flex-col">
      <div className="mb-4 flex justify-between items-center">
        <p className="font-sans text-sm text-ink/60">
          체크박스를 선택/해제하고 저장 버튼을 눌러주세요.
        </p>
        <button type="button" onClick={handleSave} className="btn btn-crayon">
          저장
        </button>
      </div>

      <div className="flex-1 overflow-y-auto space-y-2">
        {allMenus.map((menu) => (
          <label
            key={menu.menuId}
            className="flex items-center gap-3 p-3 border-2 border-ink/20 rounded-xl hover:bg-ink/5 cursor-pointer transition-colors"
          >
            <input
              type="checkbox"
              checked={selectedMenuIds.includes(menu.menuId)}
              onChange={() => handleToggle(menu.menuId)}
              className="w-5 h-5"
            />
            <div className="flex-1">
              <span className="font-sans text-ink">{menu.name}</span>
              <span className="ml-2 text-sm text-ink/60">
                {menu.price.toLocaleString()}원
              </span>
            </div>
            {menu.selling && (
              <span className="text-xs bg-crayon/10 text-crayon px-2 py-1 rounded">
                판매 중
              </span>
            )}
          </label>
        ))}
      </div>
    </div>
  );
}
