/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        // My Doodle Cafe - Naive Design 컬러 시스템
        paper: '#F8F4F0',        // 배경: 아주 연한 종이색 (따뜻한 베이지 화이트)
        ink: '#18181B',          // 메인 블랙: 먹색 느낌의 차콜
        crayon: {
          DEFAULT: '#F97316',    // 강조: 크레파스 오렌지
          hover: '#EA580C',      // 호버: 진한 오렌지
        },
        
        // 추가 컬러 (필요시 사용)
        doodle: {
          yellow: '#FDE047',     // 형광펜 노랑
          pink: '#FB7185',       // 크레파스 핑크
          blue: '#60A5FA',       // 크레파스 블루
          green: '#4ADE80',      // 크레파스 그린
        },
      },
      fontFamily: {
        doodle: ['KCCImkwontaek', 'cursive'],            // 타이틀: 붓글씨 (투박한 손글씨)
        sans: ['Pretendard', 'system-ui', 'sans-serif'], // 본문: 가독성 좋은 고딕
      },
      borderRadius: {
        'doodle': '12px',        // 손그림 느낌의 라운드
      },
      boxShadow: {
        'doodle': '4px 4px 0px 0px #18181B',  // 삐뚤빼뚤한 그림자 (Naive 스타일)
      },
      backgroundImage: {
        // 나중에 종이 질감 패턴 이미지 추가 시 사용
        // 'paper-texture': "url('/assets/paper-texture.png')",
      },
      animation: {
        'bounce-slow': 'bounce 2s infinite',
      },
      // 레이아웃별 반응형 브레이크포인트
      screens: {
        'mobile': '375px',      // Customer (모바일)
        'tablet': '768px',      // Seller (포스기/태블릿)
        'desktop': '1024px',    // Admin (데스크탑)
        'wide': '1440px',       // Admin (와이드 모니터)
      },
      // 레이아웃별 최대 너비
      maxWidth: {
        'mobile': '430px',      // Customer 최대 너비
        'tablet': '1024px',     // Seller 최대 너비
        'desktop': '1920px',    // Admin 최대 너비
      },
      // 레이아웃별 간격
      spacing: {
        'mobile-safe': 'max(env(safe-area-inset-bottom), 1rem)',
        'nav-mobile': '60px',   // 모바일 하단 네비게이션 높이
        'nav-tablet': '80px',   // 태블릿 상단 네비게이션 높이
        'sidebar': '240px',     // 데스크탑 사이드바 너비
      },
    },
  },
  plugins: [],
}
