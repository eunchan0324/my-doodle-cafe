// src/pages/Landing.tsx
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

interface DesktopCardProps {
  role: 'customer' | 'seller' | 'admin';
  img: string;
  title: string;
  label: string;
  path: string;
  isAccent?: boolean;
}

function DesktopCard({ role, img, title, label, path, isAccent = false }: DesktopCardProps) {
  const [isHovered, setIsHovered] = useState(false);
  const navigate = useNavigate();

  return (
    // 수정: h-full 제거, justify-end 제거 (부모가 h-auto가 되므로 필요 없음)
    <div
      className="relative flex-1 flex flex-col items-center cursor-pointer group"
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
      onClick={() => navigate(path)}
    >
      {/* 캐릭터 이미지 (1.3배 크게) */}
      <div
        className={`relative transition-transform duration-300 ${
          role === 'admin' ? 'w-[180%] max-w-none' : 'w-full max-w-lg'
        } ${isHovered ? '-translate-y-4 scale-105' : ''}`}
      >
        <img
          src={img}
          alt={title}
          className="w-full h-auto object-contain drop-shadow-2xl"
        />
      </div>

      {/* Role Label (손글씨 제목) */}
      <h2 className={`font-doodle text-3xl text-ink mt-6 transition-all duration-300 ${isHovered ? 'scale-110' : ''}`}>
        {label}
      </h2>

      {/* 호버 시 나타나는 입장 버튼 */}
      <div
        className={`transition-all duration-300 mt-3 ${
          isHovered ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'
        }`}
      >
      <button
        className={`btn ${isAccent ? 'btn-crayon' : 'btn-primary'} px-10 py-3 text-2xl font-normal whitespace-nowrap`}
      >
        입장하기 →
      </button>
      </div>
    </div>
  );
}

export default function Landing() {
  const navigate = useNavigate();

  return (
    // justify-center -> justify-start (헤더를 위쪽 기준으로 잡음)
    // pt-40 헤더의 상단 여백 고정 (기존 위치와 비슷하게)
    <div className="min-h-screen flex flex-col items-center justify-start pt-24 md:pt-40 p-6 relative"
         style={{
           backgroundImage: 'url(/images/paper-texture.png)',
           backgroundSize: 'cover',
           backgroundPosition: 'center',
           backgroundAttachment: 'fixed'
         }}>
      
      {/* 타이틀 (공통) */}
      {/* 수정 3: mt-10 제거 (상단 padding으로 제어하므로) */}
      <h1 className="text-5xl md:text-9xl font-doodle mb-10 text-ink text-center">
        My Doodle Cafe
      </h1>
      <p className="font-doodle text-xl md:text-4xl text-ink/80 mb-12 text-center">
        낙서 같은 하루에, 커피 한 컵 ☕
      </p>

      {/* 모바일 전용 레이아웃 (PC에선 숨김) */}
      <div className="flex flex-col items-center w-full md:hidden flex-1 justify-center pb-20">
        {/* 심바가 주인공! - GIF 자동 재생 */}
        <div className="relative mb-8" style={{ width: '100vw', maxWidth: '600px', height: '90vw', maxHeight: '400px' }}>
          <img 
            src="/images/simba_action.gif" 
            alt="Welcome" 
            className="w-full h-full object-contain drop-shadow-2xl"
          />
        </div>
        
        {/* 버튼 항상 노출 */}
        <button
          onClick={() => navigate('/customer/login')}
          className="w-full max-w-xs btn btn-crayon text-2xl py-3 font-normal animate-bounce-slow"
        >
          주문하기 →
        </button>
      </div>

      {/* 데스크탑 전용 레이아웃 (모바일에선 숨김) */}
      {/* 수정 4: h-[65vh] 제거 -> h-auto (내용물 크기만큼만 차지) */}
      {/* 수정 5: mt-6 -> mt-12 (헤더와 캐릭터 사이의 간격 조절) */}
      {/* 수정 6: gap-8 -> gap-16, max-w-6xl -> max-w-7xl (카드 간격 & 전체 너비 증가) */}
      <div className="hidden md:flex flex-row gap-24 w-full max-w-[1400px] items-end justify-center h-auto mt-24 mb-20 px-8">
        {/* 구매자 */}
        <DesktopCard
          role="customer"
          img="/images/nala_welcome.png"
          title="입장하기"
          label="주문하러 가기"
          path="/customer/login"
          isAccent={true}
        />

        {/* 판매자 */}
        <DesktopCard
          role="seller"
          img="/images/simba_manager.png"
          title="입장하기"
          label="바리스타 모드"
          path="/admin/login"
        />

        {/* 관리자 */}
        <DesktopCard
          role="admin"
          img="/images/admin_duo.png"
          title="입장하기"
          label="매니저 오피스"
          path="/admin/login"
        />
      </div>

      {/* 푸터 */}
      <div className="absolute bottom-4 text-center w-full">
        <p className="font-sans text-xs text-ink/40">
          My Doodle Cafe © 2026 - Naive Design
        </p>
      </div>
    </div>
  );
}