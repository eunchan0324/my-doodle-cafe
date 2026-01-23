// src/components/admin/MenuFormModal.tsx
import { useEffect, useState } from 'react';
import { X } from 'lucide-react';
import type { AdminMenuCreateRequest, Category } from '../../types/admin';
import { CATEGORY_LABELS } from '../../types/admin';

interface MenuFormModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: AdminMenuCreateRequest) => Promise<void>;
  initialData?: AdminMenuCreateRequest & { id?: string };
  mode: 'create' | 'edit';
}

export default function MenuFormModal({
  isOpen,
  onClose,
  onSubmit,
  initialData,
  mode,
}: MenuFormModalProps) {
  const [formData, setFormData] = useState<AdminMenuCreateRequest>({
    name: '',
    price: 0,
    category: 'COFFEE',
    description: '',
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (initialData) {
      setFormData({
        name: initialData.name,
        price: initialData.price,
        category: initialData.category,
        description: initialData.description,
      });
    } else {
      setFormData({
        name: '',
        price: 0,
        category: 'COFFEE',
        description: '',
      });
    }
  }, [initialData, isOpen]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      await onSubmit(formData);
      onClose();
    } catch (error) {
      console.error('메뉴 저장 실패:', error);
      alert('메뉴 저장에 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4">
      <div className="w-full max-w-lg rounded-2xl border-2 border-ink bg-white p-6 shadow-[6px_6px_0px_rgba(0,0,0,0.2)]">
        {/* 헤더 */}
        <div className="mb-6 flex items-center justify-between">
          <h2 className="font-doodle text-2xl text-ink">
            {mode === 'create' ? '새 메뉴 추가' : '메뉴 수정'}
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
          {/* 메뉴명 */}
          <div>
            <label htmlFor="name" className="mb-1 block text-sm font-medium text-ink/70">
              메뉴명
            </label>
            <input
              id="name"
              type="text"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              className="w-full rounded-lg border-2 border-gray-300 px-4 py-2 font-sans transition-colors focus:border-crayon focus:outline-none"
              placeholder="예: 아메리카노"
              required
            />
          </div>

          {/* 가격 */}
          <div>
            <label htmlFor="price" className="mb-1 block text-sm font-medium text-ink/70">
              가격 (원)
            </label>
            <input
              id="price"
              type="number"
              value={formData.price}
              onChange={(e) => setFormData({ ...formData, price: parseInt(e.target.value) || 0 })}
              className="w-full rounded-lg border-2 border-gray-300 px-4 py-2 font-sans transition-colors focus:border-crayon focus:outline-none"
              placeholder="4500"
              min="0"
              step="100"
              required
            />
          </div>

          {/* 카테고리 */}
          <div>
            <label htmlFor="category" className="mb-1 block text-sm font-medium text-ink/70">
              카테고리
            </label>
            <select
              id="category"
              value={formData.category}
              onChange={(e) => setFormData({ ...formData, category: e.target.value as Category })}
              className="w-full rounded-lg border-2 border-gray-300 px-4 py-2 font-sans transition-colors focus:border-crayon focus:outline-none"
              required
            >
              {Object.entries(CATEGORY_LABELS).map(([value, label]) => (
                <option key={value} value={value}>
                  {label}
                </option>
              ))}
            </select>
          </div>

          {/* 설명 */}
          <div>
            <label htmlFor="description" className="mb-1 block text-sm font-medium text-ink/70">
              설명
            </label>
            <textarea
              id="description"
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              className="w-full rounded-lg border-2 border-gray-300 px-4 py-2 font-sans transition-colors focus:border-crayon focus:outline-none"
              placeholder="메뉴에 대한 설명을 입력하세요"
              rows={3}
              required
            />
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
              disabled={loading}
              className="flex-1 rounded-lg border-2 border-ink bg-crayon px-4 py-2 font-doodle text-white shadow-[3px_3px_0px_0px_rgba(0,0,0,0.2)] transition-all hover:-translate-y-0.5 disabled:opacity-50"
            >
              {loading ? '저장 중...' : mode === 'create' ? '추가하기' : '수정하기'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
