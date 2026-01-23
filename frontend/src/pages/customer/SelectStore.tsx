// src/pages/customer/SelectStore.tsx
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../api/axios';
import CustomerLayout from '../../layouts/CustomerLayout';

type Store = {
  id: number;
  name: string;
};

export default function SelectStore() {
  const navigate = useNavigate();
  const [stores, setStores] = useState<Store[]>([]);
  const [selectedStoreId, setSelectedStoreId] = useState<number | null>(null);
  const [selectedStoreName, setSelectedStoreName] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const characterImages = ['/images/nala_welcome.png', '/images/simba_manager.png'];

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      navigate('/login');
      return;
    }
  }, [navigate]);

  useEffect(() => {
    let isMounted = true;

    async function fetchStores() {
      try {
        const response = await api.get<Store[]>('/api/v1/stores');
        if (!isMounted) return;
        setStores(response.data);
        setErrorMessage(null);
      } catch (error) {
        if (!isMounted) return;
        setErrorMessage('지점 정보를 불러오지 못했어요. 잠시 후 다시 시도해주세요.');
      } finally {
        if (!isMounted) return;
        setLoading(false);
      }
    }

    fetchStores();
    return () => {
      isMounted = false;
    };
  }, []);

  const handleSelect = (storeId: number, storeName: string) => {
    setSelectedStoreId(storeId);
    setSelectedStoreName(storeName);
  };

  const handleProceed = () => {
    if (!selectedStoreId || !selectedStoreName) return;
    sessionStorage.setItem('selectedStoreId', String(selectedStoreId));
    sessionStorage.setItem('selectedStoreName', selectedStoreName);
    navigate(`/customer/stores/${selectedStoreId}/menus`);
  };

  return (
    <CustomerLayout showNav={false}>
      <div className="py-6 px-4 pb-28">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="font-doodle text-4xl text-ink">
              지점 선택
            </h1>
            <p className="font-sans text-base text-ink/70 mt-2">
              원하시는 지점을 선택해주세요
            </p>
          </div>
        </div>

        <div className="mt-6">
          {loading && (
            <div className="card p-6">
              <p className="font-sans text-center text-ink/60">
                지점 목록을 불러오는 중이에요…
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

          {!loading && !errorMessage && stores.length === 0 && (
            <div className="card p-6">
              <p className="font-sans text-center text-ink/60">
                아직 등록된 지점이 없어요.
              </p>
            </div>
          )}

          {!loading && !errorMessage && stores.length > 0 && (
            <div className="grid grid-cols-1 gap-4">
              {stores.map((store, index) => {
                const isSelected = selectedStoreId === store.id;
                const characterImg = characterImages[index % characterImages.length];
                return (
                  <button
                    key={store.id}
                    type="button"
                    onClick={() => handleSelect(store.id, store.name)}
                    className={`card relative group text-left px-5 py-4 pr-20 transition-all duration-150 rounded-[22px_14px_20px_12px/12px_20px_14px_22px] ${
                      isSelected
                        ? 'border-crayon bg-crayon/10'
                        : 'border-ink bg-white'
                    }`}
                  >
                    <div className="flex items-center justify-between">
                      <h2 className="font-sans text-lg font-semibold text-ink">
                        {store.name}
                      </h2>
                    </div>
                    <div className="absolute right-2 bottom-0 w-14 h-14 opacity-90 transition-transform duration-200 group-hover:scale-105">
                      <img
                        src={characterImg}
                        alt="store mascot"
                        className="w-full h-full object-contain drop-shadow-sm"
                      />
                    </div>
                  </button>
                );
              })}
            </div>
          )}
        </div>

        <div className="sticky bottom-0 mt-1 py-6">
          <button
            type="button"
            onClick={handleProceed}
            disabled={!selectedStoreId}
            className={`btn w-full text-lg font-normal ${
              selectedStoreId ? 'btn-crayon' : 'btn-primary opacity-50'
            }`}
          >
            {selectedStoreId ? '선택 완료 →' : '매장을 선택해주세요'}
          </button>
        </div>
      </div>
    </CustomerLayout>
  );
}
