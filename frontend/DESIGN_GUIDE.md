# 🎨 My Doodle Cafe - 디자인 가이드

## 🐕 프로젝트 컨셉
**"My Doodle Cafe"** - 강아지와 함께하는 나이브 아트 카페

### 디자인 철학
- **Naive Design (2026 트렌드)**: 손그림 느낌, 투박하지만 따뜻한
- **Doodle Style**: 낙서하듯 자유롭고 귀여운
- **Character-Driven**: 강아지 캐릭터 중심의 스토리텔링
- **Handcrafted Feel**: 정제되지 않은 손맛

---

## 🎨 컬러 시스템

### 기본 컬러

#### Paper (배경) - `#F8F4F0`
```css
bg-paper
```
**용도**: 전체 배경색 (따뜻한 베이지 화이트, 종이 느낌)
- Customer: 전체 배경
- Seller: 전체 배경
- Admin: 카드 외부 배경

#### Ink (메인 블랙) - `#18181B`
```css
text-ink, border-ink, bg-ink
```
**용도**: 텍스트, 테두리, 버튼 (100% 검정보다 부드러운 먹색)
- 모든 텍스트
- 카드/버튼 테두리 (2px)
- Primary 버튼 배경

#### Crayon (강조) - `#F97316`
```css
bg-crayon, text-crayon, border-crayon
hover:bg-crayon-hover
```
**용도**: 주문하기, CTA 버튼 (크레파스로 칠한 듯한 오렌지)
- 주문하기 버튼
- 장바구니 담기
- 중요한 액션

### 추가 컬러 (Doodle Palette)

```css
bg-doodle-yellow   (#FDE047) - 형광펜 노랑
bg-doodle-pink     (#FB7185) - 크레파스 핑크
bg-doodle-blue     (#60A5FA) - 크레파스 블루
bg-doodle-green    (#4ADE80) - 크레파스 그린
```

**용도**: 
- 강조 표시
- 카테고리 구분
- 스티커/뱃지 효과

---

## 🔤 타이포그래피

### 폰트 패밀리

#### 1. KCC임권택체 (타이틀용)
```css
font-doodle
```
**특징**: 붓글씨 느낌의 투박하고 힘있는 서체
**용도**:
- 브랜드 로고 (My Doodle Cafe)
- 페이지 메인 타이틀
- 섹션 헤딩
- 강조하고 싶은 문구

**사용 예시**:
```tsx
<h1 className="font-doodle text-4xl">My Doodle Cafe</h1>
```

#### 2. Pretendard (본문용)
```css
font-sans
```
**특징**: 가독성 좋은 산세리프
**용도**:
- 본문 텍스트
- 버튼 텍스트
- 설명/캡션
- 폼 입력 필드

---

### 폰트 크기 시스템

**Customer (모바일)**:
```
브랜드: font-doodle text-3xl (30px)
페이지 제목: font-doodle text-2xl (24px)
섹션 제목: font-sans text-lg font-semibold (18px)
본문: font-sans text-base (16px)
캡션: font-sans text-sm text-ink/70 (14px)
```

**Seller (태블릿)**:
```
페이지 제목: font-doodle text-3xl (30px)
카드 제목: font-sans text-xl font-bold (20px)
본문: font-sans text-lg (18px)
버튼: font-sans text-base font-medium (16px)
```

**Admin (데스크탑)**:
```
페이지 제목: font-doodle text-4xl (36px)
섹션 제목: font-sans text-2xl font-bold (24px)
본문: font-sans text-base (16px)
테이블: font-sans text-sm (14px)
```

---

## 🎭 3가지 레이아웃 전략

### 1️⃣ Customer (구매자) - 모바일 낙서장 느낌

**컨셉**: 스마트폰 메모장에 낙서하듯 자유롭고 귀여운

**특징**:
- 📱 세로 스크롤
- 🖍️ 손그림 테두리 (border-2 border-ink)
- 🐕 강아지 캐릭터 곳곳에 배치
- 🎨 밝고 따뜻한 paper 배경
- 💭 말풍선 스타일 카드

**레이아웃 스펙**:
```css
max-w-mobile (430px)
bg-paper
px-4
pb-nav-mobile (60px)
```

**버튼 스타일**:
```tsx
<button className="btn btn-crayon w-full">
  🛒 주문하기
</button>
```

**카드 스타일**:
```tsx
<div className="card">
  {/* border-2 border-ink + shadow-doodle */}
</div>
```

---

### 2️⃣ Seller (판매자) - 포스기 낙서 보드

**컨셉**: 매장 키친 보드에 주문 메모하듯

**특징**:
- 📊 가로 2단 레이아웃
- 🖐️ 큰 터치 영역
- 📋 주문 메모 카드 스타일
- ⚡ 빠른 액션 (승인/거부)
- 🎯 시각적 상태 구분 (색상 코딩)

**레이아웃 스펙**:
```css
max-w-tablet (1024px)
h-screen flex
bg-paper
```

**주문 카드**:
```tsx
<div className="bg-white border-2 border-ink shadow-doodle p-6">
  <h3 className="font-doodle text-xl">주문 #001</h3>
  {/* ... */}
</div>
```

---

### 3️⃣ Admin (관리자) - 웹 대시보드 (깔끔)

**컨셉**: 전통적 대시보드이지만 Doodle 요소 가미

**특징**:
- 🖥️ 사이드바 + 메인
- 📊 테이블/차트
- 🎨 Paper 배경 + White 카드
- 📈 데이터 중심
- 🖍️ 아이콘에 Doodle 느낌

**레이아웃 스펙**:
```css
flex h-screen bg-paper
sidebar: w-sidebar bg-white border-r-2 border-ink
main: flex-1 bg-paper
```

**카드**:
```tsx
<div className="bg-white border-2 border-ink rounded-lg p-6">
  <h2 className="font-doodle text-2xl">통계</h2>
  {/* ... */}
</div>
```

---

## 🧩 컴포넌트 스타일 가이드

### 버튼

#### Primary (기본)
```tsx
<button className="btn btn-primary">
  확인
</button>
```
**스타일**: 먹색 배경 + 흰색 텍스트 + doodle 그림자

#### Crayon (강조)
```tsx
<button className="btn btn-crayon">
  🎨 주문하기
</button>
```
**스타일**: 오렌지 배경 + 흰색 텍스트 + doodle 그림자
**호버**: 클릭하면 그림자 사라지며 살짝 이동 (누르는 느낌)

#### Danger (삭제/거부)
```tsx
<button className="btn btn-danger">
  삭제
</button>
```

---

### 카드

#### 기본 카드
```tsx
<div className="card">
  {/* 내용 */}
</div>
```
**스타일**:
- `bg-white`
- `border-2 border-ink`
- `shadow-doodle` (4px 4px 검정 그림자)
- `hover`: 살짝 이동하며 그림자 사라짐

#### 메뉴 카드 (Customer)
```tsx
<div className="card cursor-pointer">
  <img src="..." className="rounded-lg mb-2" />
  <h3 className="font-sans text-lg font-semibold">아메리카노</h3>
  <p className="text-sm text-ink/70">진한 에스프레소</p>
  <div className="flex items-center justify-between mt-3">
    <span className="font-doodle text-xl">₩4,500</span>
    <button className="btn btn-crayon">담기</button>
  </div>
</div>
```

---

### 입력 필드

```tsx
<input 
  type="text"
  placeholder="이메일" 
  className="input"
/>
```
**스타일**:
- `border-2 border-ink`
- `focus`: 크레파스 컬러 테두리

---

### 배지/스티커

```tsx
<span className="inline-block px-3 py-1 bg-doodle-yellow border-2 border-ink rounded-full text-sm font-semibold">
  인기 🔥
</span>
```

---

## 🐕 캐릭터 사용 가이드

### 강아지 캐릭터 PNG 배치

**사용 위치**:
1. **랜딩 페이지**: 메인 히어로에 큰 강아지
2. **빈 상태**: 장바구니 비었을 때 "텅~" 표정
3. **로딩**: 달리는 강아지 애니메이션
4. **성공**: 꼬리 흔드는 강아지
5. **에러**: 슬픈 표정 강아지
6. **장식**: 페이지 모서리에 작게

**크기 가이드**:
```css
w-16 h-16   - 아이콘용
w-32 h-32   - 일반 장식
w-48 h-48   - 빈 상태
w-64 h-64   - 히어로
```

---

## 🎨 종이 질감 배경

### 현재 설정
```css
background: #F8F4F0; /* 단색 */
```

### 옵션 1: CSS 노이즈 텍스처 (추천)
```css
/* index.css에 추가 */
body::before {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url("data:image/svg+xml,%3Csvg...%3E");
  opacity: 0.05;
  pointer-events: none;
}
```

### 옵션 2: 이미지 텍스처
```css
background: #F8F4F0 url('/assets/paper-texture.png');
background-size: 400px 400px;
opacity: 0.03;
```

---

## 📱 반응형 가이드

### Breakpoints
```js
mobile:  375px   // Customer
tablet:  768px   // Seller
desktop: 1024px  // Admin
wide:    1440px  // Admin 와이드
```

### 예시
```tsx
<div className="
  grid grid-cols-1
  tablet:grid-cols-2
  desktop:grid-cols-3
">
  {/* 카드들 */}
</div>
```

---

## ✨ 애니메이션 가이드

### Doodle 버튼 효과
```css
/* 이미 .btn에 적용됨 */
hover:translate-x-1 hover:translate-y-1 hover:shadow-none
transition-all duration-150
```

### 카드 호버
```css
/* 이미 .card에 적용됨 */
hover:translate-x-1 hover:translate-y-1 hover:shadow-none
```

### 페이지 전환 (나중에)
```tsx
<motion.div
  initial={{ opacity: 0, y: 20 }}
  animate={{ opacity: 1, y: 0 }}
  transition={{ duration: 0.3 }}
>
```

---

## 🎯 디자인 원칙

1. **손그림 느낌 유지**
   - 완벽한 정렬보다 자연스러움
   - border-2로 투박한 테두리
   - shadow-doodle로 입체감

2. **따뜻함과 친근함**
   - Paper 배경으로 부드러움
   - 강아지 캐릭터로 친근함
   - 크레파스 컬러로 밝음

3. **가독성 우선**
   - 본문은 Pretendard (깔끔)
   - 충분한 대비 (ink vs paper)
   - 적절한 여백

4. **재미 요소**
   - 이모지 활용 🐕☕🎨
   - 강아지 캐릭터 곳곳에
   - 말풍선, 스티커 효과

5. **일관성**
   - 같은 컴포넌트는 같은 스타일
   - 통일된 간격 시스템
   - 통일된 컬러 사용

---

## 📋 체크리스트

### 컴포넌트 개발 시
- [ ] font-doodle (타이틀) / font-sans (본문) 구분
- [ ] border-2 border-ink 적용
- [ ] shadow-doodle 그림자
- [ ] hover 시 translate 효과
- [ ] bg-paper 배경 (또는 bg-white)
- [ ] 강아지 캐릭터 배치 고려

### 페이지 개발 시
- [ ] 레이아웃에 맞는 max-width
- [ ] 적절한 padding/margin
- [ ] 반응형 breakpoint 확인
- [ ] 이모지로 재미 요소 추가
- [ ] 빈 상태에 캐릭터 배치

---

## 🎨 컬러 참고

```
Paper:  #F8F4F0  ░░░░░░ (따뜻한 베이지 화이트)
Ink:    #18181B  ██████ (먹색 차콜)
Crayon: #F97316  ██████ (크레파스 오렌지)

Yellow: #FDE047  ██████ (형광펜 노랑)
Pink:   #FB7185  ██████ (크레파스 핑크)
Blue:   #60A5FA  ██████ (크레파스 블루)
Green:  #4ADE80  ██████ (크레파스 그린)
```

---

## 🔗 참고 자료

- **Naive Design 트렌드**: 2026년 손그림 느낌 UI
- **Pretendard 폰트**: https://github.com/orioncactus/pretendard
- **KCC임권택체**: 눈누 https://noonnu.cc/
- **레퍼런스**: Café de la Presse 스타일

---

**⚠️ 모든 UI 개발 시 이 가이드를 참고하세요!**

**🐕 강아지와 함께하는 따뜻한 카페를 만들어요!**
