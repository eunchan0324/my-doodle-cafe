// src/pages/admin/Menus.tsx
import { useEffect, useState } from 'react';
import { Plus, Search, Pencil, Trash2 } from 'lucide-react';
import AdminTable from '../../components/admin/AdminTable';
import MenuFormModal from '../../components/admin/MenuFormModal';
import { getAllMenus, createMenu, updateMenu, deleteMenu } from '../../api/adminMenuApi';
import type { AdminMenuResponse, AdminMenuCreateRequest, Category } from '../../types/admin';
import { CATEGORY_LABELS } from '../../types/admin';

export default function AdminMenus() {
  const [menus, setMenus] = useState<AdminMenuResponse[]>([]);
  const [filteredMenus, setFilteredMenus] = useState<AdminMenuResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<Category | 'ALL'>('ALL');
  
  // 모달 상태
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState<'create' | 'edit'>('create');
  const [editingMenu, setEditingMenu] = useState<AdminMenuResponse | null>(null);

  // 메뉴 목록 로드
  const loadMenus = async () => {
    try {
      setLoading(true);
      const data = await getAllMenus();
      setMenus(data);
      setFilteredMenus(data);
    } catch (error) {
      console.error('메뉴 로드 실패:', error);
      alert('메뉴 목록을 불러오는데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadMenus();
  }, []);

  // 검색 & 필터링
  useEffect(() => {
    let result = menus;

    // 카테고리 필터
    if (selectedCategory !== 'ALL') {
      result = result.filter((menu) => menu.category === selectedCategory);
    }

    // 검색어 필터
    if (searchTerm) {
      result = result.filter((menu) =>
        menu.name.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    setFilteredMenus(result);
  }, [menus, searchTerm, selectedCategory]);

  // 메뉴 추가
  const handleCreate = async (data: AdminMenuCreateRequest) => {
    await createMenu(data);
    await loadMenus();
  };

  // 메뉴 수정
  const handleEdit = (menu: AdminMenuResponse) => {
    setEditingMenu(menu);
    setModalMode('edit');
    setIsModalOpen(true);
  };

  const handleUpdate = async (data: AdminMenuCreateRequest) => {
    if (!editingMenu) return;
    await updateMenu(editingMenu.id, data);
    await loadMenus();
    setEditingMenu(null);
  };

  // 메뉴 삭제
  const handleDelete = async (menuId: string, menuName: string) => {
    if (!confirm(`"${menuName}" 메뉴를 삭제하시겠습니까?\n\n이 작업은 되돌릴 수 없습니다.`)) {
      return;
    }

    try {
      await deleteMenu(menuId);
      await loadMenus();
      alert('메뉴가 삭제되었습니다.');
    } catch (error) {
      console.error('메뉴 삭제 실패:', error);
      alert('메뉴 삭제에 실패했습니다.');
    }
  };

  // 새 메뉴 추가 버튼
  const openCreateModal = () => {
    setEditingMenu(null);
    setModalMode('create');
    setIsModalOpen(true);
  };

  return (
    <div className="space-y-6">
      {/* 헤더 */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="font-doodle text-3xl text-ink">메뉴 관리</h1>
          <p className="mt-1 text-sm text-gray-500">전체 메뉴를 생성, 수정, 삭제할 수 있습니다.</p>
        </div>
        <button
          type="button"
          onClick={openCreateModal}
          className="flex items-center gap-2 rounded-lg border-2 border-ink bg-crayon px-5 py-2.5 font-doodle text-white shadow-[3px_3px_0px_0px_rgba(0,0,0,0.2)] transition-all hover:-translate-y-0.5"
        >
          <Plus size={20} strokeWidth={2.5} />
          새 메뉴 추가
        </button>
      </div>

      {/* 검색 & 필터 */}
      <div className="flex gap-3">
        {/* 검색 */}
        <div className="relative flex-1">
          <Search
            className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
            size={18}
            strokeWidth={2.5}
          />
          <input
            type="text"
            placeholder="메뉴명으로 검색..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full rounded-lg border-2 border-gray-300 py-2 pl-10 pr-4 font-sans text-sm transition-colors focus:border-crayon focus:outline-none"
          />
        </div>

        {/* 카테고리 필터 */}
        <select
          value={selectedCategory}
          onChange={(e) => setSelectedCategory(e.target.value as Category | 'ALL')}
          className="rounded-lg border-2 border-gray-300 px-4 py-2 font-doodle text-sm transition-colors focus:border-crayon focus:outline-none"
        >
          <option value="ALL">전체 카테고리</option>
          {Object.entries(CATEGORY_LABELS).map(([value, label]) => (
            <option key={value} value={value}>
              {label}
            </option>
          ))}
        </select>
      </div>

      {/* 메뉴 테이블 */}
      {loading ? (
        <div className="rounded-xl border-2 border-ink bg-white p-12 text-center">
          <p className="font-sans text-gray-400">메뉴를 불러오는 중...</p>
        </div>
      ) : filteredMenus.length === 0 ? (
        <div className="rounded-xl border-2 border-ink bg-white p-12 text-center">
          <p className="font-sans text-gray-400">
            {searchTerm || selectedCategory !== 'ALL'
              ? '검색 결과가 없습니다.'
              : '등록된 메뉴가 없습니다. 새 메뉴를 추가해보세요!'}
          </p>
        </div>
      ) : (
        <AdminTable headers={['메뉴명', '가격', '카테고리', '설명', '작업']}>
          {filteredMenus.map((menu) => (
            <tr key={menu.id}>
              <td className="border-t border-dotted border-gray-300 px-4 py-3 font-doodle text-base">
                {menu.name}
              </td>
              <td className="border-t border-dotted border-gray-300 px-4 py-3 font-sans text-sm">
                {menu.price.toLocaleString()}원
              </td>
              <td className="border-t border-dotted border-gray-300 px-4 py-3 font-sans text-sm">
                <span className="inline-block rounded-full bg-gray-100 px-3 py-1 text-xs font-medium text-ink">
                  {CATEGORY_LABELS[menu.category]}
                </span>
              </td>
              <td className="border-t border-dotted border-gray-300 px-4 py-3 font-sans text-sm text-gray-600">
                {menu.description.length > 40
                  ? `${menu.description.substring(0, 40)}...`
                  : menu.description}
              </td>
              <td className="border-t border-dotted border-gray-300 px-4 py-3">
                <div className="flex gap-2">
                  <button
                    type="button"
                    onClick={() => handleEdit(menu)}
                    className="rounded-lg border border-ink bg-white p-2 transition-colors hover:bg-gray-50"
                    title="수정"
                  >
                    <Pencil size={16} strokeWidth={2.5} />
                  </button>
                  <button
                    type="button"
                    onClick={() => handleDelete(menu.id, menu.name)}
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

      {/* 메뉴 추가/수정 모달 */}
      <MenuFormModal
        isOpen={isModalOpen}
        onClose={() => {
          setIsModalOpen(false);
          setEditingMenu(null);
        }}
        onSubmit={modalMode === 'create' ? handleCreate : handleUpdate}
        initialData={editingMenu || undefined}
        mode={modalMode}
      />
    </div>
  );
}
