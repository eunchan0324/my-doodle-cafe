// src/layouts/SellerLayout.tsx
// Seller용 태블릿 레이아웃 (포스기 느낌)

import type { ReactNode } from 'react';

interface SellerLayoutProps {
  children: ReactNode;
}

export default function SellerLayout({ children }: SellerLayoutProps) {
  return (
    <div className="min-h-screen">
      {/* 태블릿 컨테이너 */}
      <div className="container-tablet h-screen flex flex-col">
        {/* 상단 헤더 (나중에 추가) */}
        {/* <header className="h-nav-tablet bg-white border-b-2 border-ink flex items-center px-6">
          <h2 className="font-doodle text-3xl">My Doodle Cafe - 판매자</h2>
        </header> */}

        {/* 메인 컨텐츠 (2단 레이아웃 영역) */}
        <main className="flex-1 overflow-hidden">
          {children}
        </main>
      </div>
    </div>
  );
}
