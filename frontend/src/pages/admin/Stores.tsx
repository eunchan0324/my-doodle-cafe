import { Edit, Plus, Search, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";
import {
  getAllStores,
  createStore,
  updateStore,
  deleteStore,
  type AdminStoreResponse,
  type StoreCreateRequest,
} from "../../api/adminStoreApi";

const AdminStores = () => {
  const [stores, setStores] = useState<AdminStoreResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Modals state
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [selectedStore, setSelectedStore] = useState<AdminStoreResponse | null>(null);
  const [newStoreName, setNewStoreName] = useState(""); // For create modal
  const [editStoreName, setEditStoreName] = useState(""); // For edit modal


  const fetchStores = async () => {
    try {
      setLoading(true);
      const data = await getAllStores();
      setStores(data);
    } catch (err) {
      setError("지점 정보를 불러오는 데 실패했습니다.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStores();
  }, []);

  const handleCreateStore = async () => {
    try {
      if (!newStoreName.trim()) {
        alert("지점 이름을 입력해주세요.");
        return;
      }
      const request: StoreCreateRequest = { name: newStoreName };
      await createStore(request);
      setShowCreateModal(false);
      setNewStoreName("");
      fetchStores(); // Refresh list
    } catch (err) {
      alert("지점 생성에 실패했습니다.");
      console.error(err);
    }
  };

  const handleUpdateStore = async () => {
    if (!selectedStore) return;
    try {
      if (!editStoreName.trim()) {
        alert("지점 이름을 입력해주세요.");
        return;
      }
      const request: StoreCreateRequest = { name: editStoreName };
      await updateStore(selectedStore.id, request);
      setShowEditModal(false);
      setSelectedStore(null);
      setEditStoreName("");
      fetchStores(); // Refresh list
    } catch (err) {
      alert("지점 수정에 실패했습니다.");
      console.error(err);
    }
  };

  const handleDeleteStore = async (id: number, name: string) => {
    if (window.confirm(`정말로 지점 '${name}'을(를) 삭제하시겠습니까?`)) {
      try {
        await deleteStore(id);
        fetchStores(); // Refresh list
      } catch (err) {
        alert("지점 삭제에 실패했습니다.");
        console.error(err);
      }
    }
  };

  return (
    <div className="p-8">
      <header className="flex justify-between items-center mb-8">
        <h1 className="font-doodle text-4xl text-ink">지점 관리</h1>
        {/* 4. 상단 컨트롤 바 */}
        <div className="flex items-center gap-4">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-ink/60" />
            <input
              type="text"
              placeholder="지점명 검색..."
              className="font-sans pl-10 pr-4 py-2 bg-white border-2 border-ink rounded-lg focus:border-crayon focus:ring-0"
            />
          </div>
          <button
            onClick={() => setShowCreateModal(true)}
            className="flex items-center gap-2 font-sans font-semibold text-white bg-crayon px-4 py-2 rounded-lg shadow-doodle transition-all hover:translate-x-1 hover:translate-y-1 hover:shadow-none"
          >
            <Plus className="w-5 h-5" />
            새 지점 등록
          </button>
        </div>
      </header>

      {/* 3. "Simple Polaroid" 갤러리 */}
      <main className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-x-6 gap-y-12">
        {loading && <p className="font-sans text-ink">로딩 중...</p>}
        {error && <p className="font-sans text-red-500">{error}</p>}
        {!loading && !error && stores.map((store, index) => (
          <div
            key={store.id}
            className={`group relative bg-white shadow-lg aspect-[3/4] 
              ${index % 2 === 0 ? '-rotate-1' : 'rotate-2'}
              transition-transform hover:scale-105 hover:rotate-0`}
          >
            {/* A. 마스킹 테이프 */}
            <div className="absolute top-0 left-1/2 -translate-x-1/2 -translate-y-1/2 w-1/3 h-6 bg-yellow-200/50 transform rotate-[-4deg] shadow-sm"></div>
            
            <div className="flex flex-col h-full">
              {/* B. 이미지 영역 */}
              <div className="relative h-[75%] border-b-2 border-[#18181B]">
                <img
                  src={`https://picsum.photos/seed/${store.id}/400/500`}
                  alt={store.name}
                  className="w-full h-full object-cover"
                />
                {/* D. 인터랙션 오버레이 */}
                                  <div className="absolute inset-0 bg-black/50 flex items-center justify-center gap-4 opacity-0 group-hover:opacity-100 transition-opacity">
                                    <button
                                      onClick={() => {
                                        setSelectedStore(store);
                                        setEditStoreName(store.name);
                                        setShowEditModal(true);
                                      }}
                                      className="text-white p-3 bg-white/20 rounded-full hover:bg-white/30"
                                    >
                                      <Edit className="w-6 h-6" />
                                    </button>
                                    <button
                                      onClick={() => handleDeleteStore(store.id, store.name)}
                                      className="text-white p-3 bg-white/20 rounded-full hover:bg-white/30"
                                    >
                                      <Trash2 className="w-6 h-6" />
                                    </button>
                                  </div>
              </div>
              
              {/* C. 텍스트 영역 */}
              <div className="h-[25%] flex flex-col items-center justify-center p-2 relative">
                <span className="absolute top-1 right-1 font-sans font-bold text-xs bg-red-100 text-red-700 border border-red-300 px-1.5 py-0.5 rounded-sm transform -rotate-6">
                  #{store.id}
                </span>
                <p className="font-doodle text-2xl text-center text-ink mt-2">
                  {store.name}
                </p>
              </div>
            </div>
          </div>
        ))}
      </main>

      {/* Create Store Modal */}
      {showCreateModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center p-4">
          <div className="bg-white p-6 rounded-lg shadow-xl border-2 border-ink w-full max-w-sm">
            <h2 className="font-doodle text-2xl text-ink mb-4">새 지점 등록</h2>
            <input
              type="text"
              placeholder="지점명"
              value={newStoreName}
              onChange={(e) => setNewStoreName(e.target.value)}
              className="input w-full mb-4 font-sans px-3 py-2 border-2 border-ink rounded-lg focus:border-crayon focus:ring-0"
            />
            <div className="flex justify-end gap-3">
              <button
                onClick={() => setShowCreateModal(false)}
                className="btn font-sans px-4 py-2 rounded-lg bg-gray-200 text-ink shadow-doodle transition-all hover:translate-x-1 hover:translate-y-1 hover:shadow-none"
              >
                취소
              </button>
              <button
                onClick={handleCreateStore}
                className="btn font-sans px-4 py-2 rounded-lg bg-crayon text-white shadow-doodle transition-all hover:translate-x-1 hover:translate-y-1 hover:shadow-none"
              >
                등록
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Edit Store Modal */}
      {showEditModal && selectedStore && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center p-4">
          <div className="bg-white p-6 rounded-lg shadow-xl border-2 border-ink w-full max-w-sm">
            <h2 className="font-doodle text-2xl text-ink mb-4">지점 수정</h2>
            <input
              type="text"
              placeholder="지점명"
              value={editStoreName}
              onChange={(e) => setEditStoreName(e.target.value)}
              className="input w-full mb-4 font-sans px-3 py-2 border-2 border-ink rounded-lg focus:border-crayon focus:ring-0"
            />
            <div className="flex justify-end gap-3">
              <button
                onClick={() => setShowEditModal(false)}
                className="btn font-sans px-4 py-2 rounded-lg bg-gray-200 text-ink shadow-doodle transition-all hover:translate-x-1 hover:translate-y-1 hover:shadow-none"
              >
                취소
              </button>
              <button
                onClick={handleUpdateStore}
                className="btn font-sans px-4 py-2 rounded-lg bg-crayon text-white shadow-doodle transition-all hover:translate-x-1 hover:translate-y-1 hover:shadow-none"
              >
                저장
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminStores;