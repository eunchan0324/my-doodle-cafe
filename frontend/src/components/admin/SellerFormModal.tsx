// src/components/admin/SellerFormModal.tsx
import { useEffect, useState } from 'react';
import { X } from 'lucide-react';
import type { SellerCreateRequest, SellerDto, SimpleStoreResponse } from '../../types/admin';

interface SellerFormModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: SellerCreateRequest | any) => Promise<void>; // Type depends on mode, handling in parent likely or separate
  initialData?: SellerDto;
  mode: 'create' | 'edit';
  availableStores: SimpleStoreResponse[];
}

export default function SellerFormModal({
  isOpen,
  onClose,
  onSubmit,
  initialData,
  mode,
  availableStores,
}: SellerFormModalProps) {
  // Form states
  const [loginId, setLoginId] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [storeId, setStoreId] = useState<number | ''>(''); // '' represents no selection

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isOpen) {
      if (mode === 'edit' && initialData) {
        setLoginId(initialData.loginId);
        setName(initialData.name);
        setStoreId(initialData.storeId ?? '');
        setPassword(''); // Password reset on edit open usually? Or blank means no change.
      } else {
        // Create
        setLoginId('');
        setName('');
        setStoreId('');
        setPassword('');
      }
    }
  }, [isOpen, mode, initialData]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (storeId === '') {
      alert('지점을 선택해주세요.');
      return;
    }

    setLoading(true);
    try {
      const payload: any = {
        name,
        storeId: Number(storeId),
      };

      if (mode === 'create') {
        payload.loginId = loginId;
        payload.password = password;
      } else {
        // Edit mode
        if (password) {
          payload.password = password;
        }
      }

      await onSubmit(payload);
      onClose();
    } catch (error) {
      console.error('판매자 저장 실패:', error);
      alert('판매자 저장에 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  // When editing, we need to ensure the *current* store is in the list of options,
  // even if it's not "available" (because it's taken by this seller).
  // The parent passes `availableStores` which are strictly "unassigned" ones.
  // We should manually add the current store to the options if it exists.
  const currentStoreOption = (mode === 'edit' && initialData?.storeId)
    ? { id: initialData.storeId, name: initialData.storeName }
    : null;

  // Merge: availableStores + currentStoreOption (if not already there - though availableStores won't have it)
  // Display list
  const displayStores = [...availableStores];
  if (currentStoreOption) {
    // Check if already in list (unlikely based on logic but safe to check)
    if (!displayStores.find(s => s.id === currentStoreOption.id)) {
      displayStores.unshift(currentStoreOption);
    }
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4">
      <div className="w-full max-w-lg rounded-2xl border-2 border-ink bg-white p-6 shadow-[6px_6px_0px_rgba(0,0,0,0.2)]">
        {/* 헤더 */}
        <div className="mb-6 flex items-center justify-between">
          <h2 className="font-doodle text-2xl text-ink">
            {mode === 'create' ? '새 판매자 계정 생성' : '계정 정보 수정'}
          </h2>
          <button
            type="button"
            onClick={onClose}
            className="rounded-lg p-2 transition-colors hover:bg-gray-100"
          >
            <X size={20} strokeWidth={2.5} />
          </button>
        </div>

        {/* 폼 */}
        <form onSubmit={handleSubmit} className="space-y-4">
          {/* 아이디 (Create Only) */}
          <div>
            <label htmlFor="loginId" className="mb-1 block text-sm font-medium text-ink/70">
              아이디
            </label>
            <input
              id="loginId"
              type="text"
              value={loginId}
              onChange={(e) => setLoginId(e.target.value)}
              className="w-full rounded-lg border-2 border-gray-300 px-4 py-2 font-sans transition-colors focus:border-crayon focus:outline-none disabled:bg-gray-100 disabled:text-gray-500"
              placeholder="아이디 입력"
              required={mode === 'create'}
              disabled={mode === 'edit'}
            />
          </div>

          {/* 비밀번호 */}
          <div>
            <label htmlFor="password" className="mb-1 block text-sm font-medium text-ink/70">
              비밀번호 {mode === 'edit' && <span className="text-xs font-normal text-gray-500">(변경 시에만 입력)</span>}
            </label>
            <input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full rounded-lg border-2 border-gray-300 px-4 py-2 font-sans transition-colors focus:border-crayon focus:outline-none"
              placeholder={mode === 'create' ? "비밀번호 입력" : "변경하려면 입력하세요"}
              required={mode === 'create'}
            />
          </div>

          {/* 이름 */}
          <div>
            <label htmlFor="name" className="mb-1 block text-sm font-medium text-ink/70">
              이름
            </label>
            <input
              id="name"
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full rounded-lg border-2 border-gray-300 px-4 py-2 font-sans transition-colors focus:border-crayon focus:outline-none"
              placeholder="판매자 이름"
              required
            />
          </div>

          {/* 지점 선택 */}
          <div>
            <label htmlFor="storeId" className="mb-1 block text-sm font-medium text-ink/70">
              담당 지점
            </label>
            <select
              id="storeId"
              value={storeId}
              onChange={(e) => setStoreId(e.target.value === '' ? '' : Number(e.target.value))}
              className="w-full rounded-lg border-2 border-gray-300 px-4 py-2 font-sans transition-colors focus:border-crayon focus:outline-none"
              required
            >
              <option value="">지점을 선택하세요</option>
              {displayStores.map((store) => (
                <option key={store.id} value={store.id}>
                  {store.name}
                </option>
              ))}
            </select>
            {mode === 'create' && displayStores.length === 0 && (
              <p className="mt-1 text-xs text-red-500">
                * 배정 가능한 지점이 없습니다. 먼저 지점을 등록하거나 기존 판매자의 배정을 해제하세요.
              </p>
            )}
            {mode === 'edit' && (
              <p className="mt-1 text-xs text-gray-500">
                * 다른 지점으로 변경하면 해당 지점은 '배정됨' 상태가 됩니다.
              </p>
            )}
          </div>

          {/* 버튼 */}
          <div className="flex gap-3 pt-2">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 rounded-lg border-2 border-ink bg-white px-4 py-2 font-doodle text-ink transition-all hover:bg-gray-50"
            >
              취소
            </button>
            <button
              type="submit"
              disabled={loading || (mode === 'create' && displayStores.length === 0)}
              className="flex-1 rounded-lg border-2 border-ink bg-crayon px-4 py-2 font-doodle text-white shadow-[3px_3px_0px_0px_rgba(0,0,0,0.2)] transition-all hover:-translate-y-0.5 disabled:opacity-50"
            >
              {loading ? '저장 중...' : mode === 'create' ? '생성하기' : '수정하기'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
