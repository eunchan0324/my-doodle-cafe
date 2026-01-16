# 📋 카페오더 프론트엔드 개발 계획서

## 프로젝트 개요
- **목적**: 포트폴리오용 카페 주문 시스템
- **기술스택**: React 19 + TypeScript + Vite + React Router 7 + Axios
- **백엔드**: Spring Boot (REST API)
- **특징**: 모바일 우선 Customer UI + 데스크탑 Admin UI

---

## 🎯 단계별 개발 계획

### **STEP 0: 기본 구조 & 라우팅** ⏱️ 예상 시간: 1-2시간

#### 목표
- 프로젝트 폴더 구조 확립
- 라우팅 기본 골격 완성
- 랜딩 페이지 UI

#### 작업 목록
- [ ] 폴더 구조 생성
  ```
  src/
  ├─ pages/
  │  ├─ Landing.tsx
  │  ├─ customer/
  │  │  ├─ Stores.tsx
  │  │  └─ Menu.tsx
  │  └─ admin/
  │     └─ Login.tsx
  ├─ components/
  ├─ layouts/
  │  ├─ CustomerLayout.tsx  (모바일 스타일)
  │  └─ AdminLayout.tsx     (데스크탑 스타일)
  ├─ hooks/
  ├─ types/
  └─ utils/
  ```

- [ ] React Router 설정
  ```
  / → Landing (랜딩)
  /customer/stores → 지점 목록
  /customer/menu → 메뉴판
  /admin/login → 관리자 로그인
  ```

- [ ] 랜딩 페이지 UI
  - 심플한 로고/타이틀
  - 버튼 2개: [주문하러 가기] / [사장님/관리자 로그인]

#### 완료 기준
- `/` 접속 시 랜딩 페이지 표시
- 두 버튼 클릭 시 각각 `/customer/stores`, `/admin/login`으로 이동
- 라우팅 에러 없음

---

### **STEP 1: Customer - 지점/메뉴 조회 (비로그인)** ⏱️ 예상 시간: 3-4시간

#### 목표
- Store API 연동 (이미 완성된 백엔드 API 사용)
- 모바일 최적화 UI
- 비로그인 상태에서 조회 가능

#### 작업 목록

**Frontend:**
- [ ] TypeScript 타입 정의
  ```typescript
  // types/store.ts
  export interface Store {
    id: number;
    name: string;
    address: string;
    phone: string;
    // ... 백엔드 DTO와 동일하게
  }
  
  // types/menu.ts
  export interface Menu {
    id: number;
    name: string;
    price: number;
    category: 'COFFEE' | 'DESSERT' | ...;
    // ...
  }
  ```

- [ ] API 서비스 함수
  ```typescript
  // api/storeService.ts
  export const getStores = () => api.get<Store[]>('/api/stores');
  export const getStore = (id: number) => api.get<Store>(`/api/stores/${id}`);
  
  // api/menuService.ts
  export const getMenus = (storeId: number) => 
    api.get<Menu[]>(`/api/menus?storeId=${storeId}`);
  ```

- [ ] 지점 목록 페이지 (`/customer/stores`)
  - 카드 그리드 레이아웃 (2열)
  - 각 카드: 지점명, 주소, 영업시간
  - 클릭 시 → `/customer/menu?storeId=1`

- [ ] 메뉴판 페이지 (`/customer/menu`)
  - 상단: 선택된 지점 정보
  - 탭: 커피 / 디저트 / 기타
  - 메뉴 카드: 이미지(옵션), 이름, 가격

**Backend (Spring Boot):**
- [ ] SecurityConfig 수정
  ```java
  .requestMatchers("/api/stores/**").permitAll()
  .requestMatchers("/api/menus/**").permitAll()
  ```

- [ ] StoreController 확인
  - 이미 완성되어 있으니 JSON 응답 확인만

- [ ] MenuController 생성 (아직 없다면)
  ```java
  @GetMapping("/api/menus")
  public List<MenuDto> getMenus(@RequestParam Long storeId) { ... }
  ```

#### 완료 기준
- Postman에서 `/api/stores`, `/api/menus` 호출 성공 (인증 없이)
- React에서 실제 백엔드 데이터로 지점 목록 표시
- 지점 선택 → 해당 지점의 메뉴판 표시

---

### **STEP 2: 장바구니 (로컬 상태 관리)** ⏱️ 예상 시간: 2-3시간

#### 목표
- 로그인 없이 장바구니에 담기
- Zustand로 전역 상태 관리
- 모바일 UX (하단 플로팅 버튼)

#### 작업 목록

**라이브러리 설치:**
```bash
npm install zustand
```

- [ ] Zustand Store 생성
  ```typescript
  // store/cartStore.ts
  interface CartItem {
    menuId: number;
    name: string;
    price: number;
    quantity: number;
    options?: { temperature?: 'HOT' | 'ICE', size?: 'S' | 'M' | 'L' };
  }
  
  interface CartStore {
    items: CartItem[];
    addItem: (item: CartItem) => void;
    removeItem: (menuId: number) => void;
    clearCart: () => void;
    totalPrice: number;
    totalCount: number;
  }
  ```

- [ ] 메뉴 상세 모달
  - 메뉴 클릭 시 모달 팝업
  - 옵션 선택 (온도, 사이즈 등)
  - [장바구니 담기] 버튼

- [ ] 하단 플로팅 버튼
  - "3개 담김 | 15,000원" 표시
  - 클릭 시 → `/customer/cart` 이동

- [ ] 장바구니 페이지 (`/customer/cart`)
  - 담긴 항목 리스트
  - 수량 조절 (+/-)
  - [주문하기] 버튼 → STEP 3으로 연결

#### 완료 기준
- 메뉴 담기 → 하단 버튼에 개수/가격 실시간 반영
- 새로고침해도 장바구니 유지 (localStorage 활용)
- 장바구니 페이지에서 수정/삭제 가능

---

### **STEP 3: 로그인 & 주문** ⏱️ 예상 시간: 4-5시간

#### 목표
- 주문 시도 시 로그인 요구
- 로그인 후 장바구니 유지
- 실제 주문 API 연동

#### 작업 목록

**Frontend:**
- [ ] Auth Store (Zustand)
  ```typescript
  // store/authStore.ts
  interface AuthStore {
    isLoggedIn: boolean;
    user: User | null;
    login: (email: string, password: string) => Promise<void>;
    logout: () => void;
  }
  ```

- [ ] 로그인 모달
  - 이메일/비밀번호 입력
  - [로그인] / [회원가입] 버튼
  - 에러 메시지 표시

- [ ] Protected Route 로직
  ```typescript
  // 장바구니 페이지에서 [주문하기] 클릭 시
  if (!isLoggedIn) {
    setShowLoginModal(true);
    return;
  }
  // 로그인 성공 시
  navigate('/customer/order');
  ```

- [ ] 주문서 작성 페이지 (`/customer/order`)
  - 장바구니 항목 요약
  - 요청사항 입력
  - [결제하기] 버튼

- [ ] API 인터셉터 (axios)
  ```typescript
  // 로그인 시 토큰 저장
  // 이후 모든 요청에 토큰 자동 포함
  api.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
  });
  ```

**Backend:**
- [ ] AuthController
  ```java
  @PostMapping("/api/auth/login")
  public LoginResponse login(@RequestBody LoginRequest req) { ... }
  // JWT 또는 세션 반환
  ```

- [ ] OrderController
  ```java
  @PostMapping("/api/orders")
  @PreAuthorize("hasRole('CUSTOMER')")
  public OrderResponse createOrder(@RequestBody OrderRequest req) { ... }
  ```

#### 완료 기준
- 비로그인 상태에서 [주문하기] → 로그인 모달 표시
- 로그인 성공 → 장바구니 데이터 유지된 채로 주문서 페이지 이동
- 주문 완료 → 백엔드에 주문 데이터 저장 확인

---

## 📦 추가 개발 항목 (STEP 4 이후)

### STEP 4: Admin/Seller 페이지
- [ ] Admin 대시보드 (Store CRUD)
- [ ] Seller 대시보드 (주문 현황)

### STEP 5: 실시간 기능 (멀티스레딩)
- [ ] WebSocket 연동
- [ ] 주문 상태 실시간 업데이트

### STEP 6: 결제 연동
- [ ] 토스페이먼츠 API

---

## 🛠️ 개발 환경

### 현재 설치됨
- ✅ React 19
- ✅ TypeScript
- ✅ React Router 7
- ✅ Axios

### 추가 설치 필요
- [ ] Zustand (상태 관리)
- ✅ TailwindCSS (스타일링) - 설치 완료

---

## 📌 프로젝트 진행 상황

### 완료된 백엔드 API
- ✅ Store CRUD (Postman 테스트 완료)

### 완료된 프론트엔드 설정
- ✅ Tailwind CSS 설정 (Cozy & Modern 디자인)
- ✅ Pretendard 웹폰트 적용
- ✅ 3가지 레이아웃 전략 수립 (Customer/Seller/Admin)
- ✅ 디자인 가이드 문서 작성 (`DESIGN_GUIDE.md`)

### 진행 중
- [ ] STEP 0: 기본 구조 & 라우팅

---

## 💡 개발 원칙

1. **디자인 가이드 준수**: 모든 UI 개발 시 `DESIGN_GUIDE.md` 필수 참고
   - Customer: 모바일 앱 레이아웃
   - Seller: 포스기/태블릿 레이아웃
   - Admin: 웹 대시보드 레이아웃
2. **모바일 우선**: Customer UI는 모바일 뷰 기준으로 개발
3. **단계별 검증**: 각 STEP 완료 시 Postman + 브라우저에서 테스트
4. **타입 안정성**: TypeScript 타입 정의를 먼저 작성
5. **재사용성**: 공통 컴포넌트는 `components/` 에 분리
6. **포트폴리오 지향**: 기술 스택 활용도를 보여주는 것이 목표

---

## 📝 참고사항

- 백엔드 URL: `http://localhost:8080`
- 세션 기반 인증: `withCredentials: true` 설정됨
- CORS 설정 필요: Spring Boot SecurityConfig에서 허용
- **디자인 가이드**: `DESIGN_GUIDE.md` - UI 개발 전 필수 확인!