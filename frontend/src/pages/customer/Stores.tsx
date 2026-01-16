// src/pages/customer/Stores.tsx
import CustomerLayout from '../../layouts/CustomerLayout';

export default function Stores() {
  return (
    <CustomerLayout>
      <div className="py-6">
        <h1 className="font-doodle text-4xl text-ink mb-4">
          지점 선택
        </h1>
        <p className="font-sans text-base text-ink/70">
          🏪 원하시는 지점을 선택해주세요
        </p>
        
        {/* 나중에 지점 카드 리스트가 들어갈 자리 */}
        <div className="mt-6 space-y-4">
          <div className="card p-6">
            <p className="font-sans text-center text-ink/50">
              지점 목록이 곧 추가됩니다! 🎨
            </p>
          </div>
        </div>
      </div>
    </CustomerLayout>
  );
}
