// src/pages/admin/Accounts.tsx
import { useEffect, useState } from 'react';
import { Plus, Search, Pencil, Trash2 } from 'lucide-react';
import AdminTable from '../../components/admin/AdminTable';
import SellerFormModal from '../../components/admin/SellerFormModal';
import { getAllSellers, createSeller, updateSeller, deleteSeller, getAvailableStores } from '../../api/adminSellerApi';
import type { SellerDto, SellerCreateRequest, SimpleStoreResponse } from '../../types/admin';

export default function AdminAccounts() {
  const [sellers, setSellers] = useState<SellerDto[]>([]);
  const [filteredSellers, setFilteredSellers] = useState<SellerDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  // Modal State
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState<'create' | 'edit'>('create');
  const [editingSeller, setEditingSeller] = useState<SellerDto | undefined>(undefined);
  const [availableStores, setAvailableStores] = useState<SimpleStoreResponse[]>([]);

  useEffect(() => {
    loadSellers();
  }, []);

  useEffect(() => {
    // Filter logic
    if (searchTerm) {
      setFilteredSellers(sellers.filter(s =>
        s.name.includes(searchTerm) ||
        s.loginId.includes(searchTerm) ||
        s.storeName.includes(searchTerm)
      ));
    } else {
      setFilteredSellers(sellers);
    }
  }, [searchTerm, sellers]);

  const loadSellers = async () => {
    try {
      setLoading(true);
      const data = await getAllSellers();
      setSellers(data);
    } catch (error) {
      console.error('판매자 목록 로드 실패:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadAvailableStores = async () => {
    try {
      const data = await getAvailableStores();
      setAvailableStores(data);
    } catch (error) {
      console.error('지점 목록 로드 실패:', error);
    }
  };

  const handleOpenCreate = async () => {
    await loadAvailableStores();
    setModalMode('create');
    setEditingSeller(undefined);
    setIsModalOpen(true);
  };

  const handleOpenEdit = async (seller: SellerDto) => {
    await loadAvailableStores();
    setModalMode('edit');
    setEditingSeller(seller);
    setIsModalOpen(true);
  };

  const handleCreate = async (data: SellerCreateRequest) => {
    await createSeller(data);
    await loadSellers();
  };

  const handleUpdate = async (data: any) => {
    if (!editingSeller) return;
    await updateSeller(editingSeller.id, data);
    await loadSellers();
    setEditingSeller(undefined);
  };

  const handleDelete = async (id: number, name: string) => {
    if (!confirm(`"${name}" 판매자 계정을 삭제하시겠습니까?\n\n이 작업은 되돌릴 수 없습니다.`)) {
      return;
    }
    try {
      await deleteSeller(id);
      await loadSellers();
    } catch (error) {
      console.error('삭제 실패:', error);
      alert('삭제에 실패했습니다.');
    }
  };

  return (
    <div className="space-y-6">
      {/* 헤더 */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="font-doodle text-3xl text-ink">계정 관리</h1>
          <p className="mt-1 text-sm text-gray-500">지점별 판매자 계정을 관리합니다.</p>
        </div>
        <button
          type="button"
          onClick={handleOpenCreate}
          className="flex items-center gap-2 rounded-lg border-2 border-ink bg-crayon px-5 py-2.5 font-doodle text-white shadow-[3px_3px_0px_0px_rgba(0,0,0,0.2)] transition-all hover:-translate-y-0.5"
        >
          <Plus size={20} strokeWidth={2.5} />
          새 계정 생성
        </button>
      </div>

      {/* 검색 */}
      <div className="relative">
        <Search
          className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
          size={18}
          strokeWidth={2.5}
        />
        <input
          type="text"
          placeholder="이름, 아이디, 지점명으로 검색..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="w-full max-w-md rounded-lg border-2 border-gray-300 py-2 pl-10 pr-4 font-sans text-sm transition-colors focus:border-crayon focus:outline-none"
        />
      </div>

      {/* 테이블 */}
      {loading ? (
        <div className="rounded-xl border-2 border-ink bg-white p-12 text-center">
          <p className="font-sans text-gray-400">데이터를 불러오는 중...</p>
        </div>
      ) : filteredSellers.length === 0 ? (
        <div className="rounded-xl border-2 border-ink bg-white p-12 text-center">
          <p className="font-sans text-gray-400">
            {searchTerm ? '검색 결과가 없습니다.' : '등록된 판매자 계정이 없습니다.'}
          </p>
        </div>
      ) : (
        <AdminTable headers={['이름', '아이디', '담당 지점', '관리']}>
          {filteredSellers.map((seller) => (
            <tr key={seller.id}>
              <td className="border-t border-dotted border-gray-300 px-4 py-3 font-doodle text-base">
                {seller.name}
              </td>
              <td className="border-t border-dotted border-gray-300 px-4 py-3 font-sans text-sm text-gray-600">
                {seller.loginId}
              </td>
              <td className="border-t border-dotted border-gray-300 px-4 py-3 font-sans text-sm">
                <span className={`inline-block rounded-full px-3 py-1 text-xs font-medium ${seller.storeId ? 'bg-doodle-yellow text-ink' : 'bg-gray-100 text-gray-500'}`}>
                  {seller.storeName}
                </span>
              </td>
              <td className="border-t border-dotted border-gray-300 px-4 py-3">
                <div className="flex gap-2">
                  <button
                    type="button"
                    onClick={() => handleOpenEdit(seller)}
                    className="rounded-lg border border-ink bg-white p-2 transition-colors hover:bg-gray-50"
                    title="수정"
                  >
                    <Pencil size={16} strokeWidth={2.5} />
                  </button>
                  <button
                    type="button"
                    onClick={() => handleDelete(seller.id, seller.name)}
                    className="rounded-lg border border-danger bg-white p-2 text-danger transition-colors hover:bg-red-50"
                    title="삭제"
                  >
                    <Trash2 size={16} strokeWidth={2.5} />
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </AdminTable>
      )}

      {/* 모달 */}
      <SellerFormModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSubmit={modalMode === 'create' ? handleCreate : handleUpdate}
        mode={modalMode}
        initialData={editingSeller}
        availableStores={availableStores}
      />
    </div>
  );
}
