// src/layouts/CustomerLayout.tsx
// Customer용 모바일 레이아웃

import type { ReactNode } from 'react';

interface CustomerLayoutProps {
  children: ReactNode;
}

export default function CustomerLayout({ children }: CustomerLayoutProps) {
  return (
    <div className="min-h-screen">
      {/* 모바일 컨테이너 */}
      <div className="container-mobile min-h-screen">
        {/* 상단 헤더 (나중에 추가) */}
        {/* <header className="py-4">
          <h2 className="font-doodle text-2xl">My Doodle Cafe</h2>
        </header> */}

        {/* 메인 컨텐츠 */}
        <main className="pb-nav-mobile">
          {children}
        </main>

        {/* 하단 네비게이션 (나중에 추가) */}
        {/* <nav className="fixed bottom-0 left-0 right-0 h-nav-mobile bg-white border-t-2 border-ink">
          // 하단 네비 버튼들
        </nav> */}
      </div>
    </div>
  );
}
