// src/layouts/AdminLayout.tsx
// Admin용 데스크탑 레이아웃 (사이드바 + 메인)

import type { ReactNode } from 'react';

interface AdminLayoutProps {
  children: ReactNode;
}

export default function AdminLayout({ children }: AdminLayoutProps) {
  return (
    <div className="flex h-screen">
      {/* 사이드바 (나중에 추가) */}
      {/* <aside className="w-sidebar bg-white border-r-2 border-ink">
        <div className="p-6">
          <h2 className="font-doodle text-3xl text-ink">관리자</h2>
        </div>
        // 사이드바 메뉴
      </aside> */}

      {/* 메인 영역 */}
      <main className="flex-1 overflow-auto">
        {/* 상단 헤더 (나중에 추가) */}
        {/* <header className="h-16 bg-white border-b-2 border-ink px-6 flex items-center">
          // 헤더 내용
        </header> */}

        {/* 컨텐츠 */}
        <div className="p-6">
          {children}
        </div>
      </main>
    </div>
  );
}
