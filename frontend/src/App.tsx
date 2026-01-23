// src/App.tsx
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Landing from './pages/Landing';
import Login from './pages/Login';
import './App.css';
import SelectStore from './pages/customer/SelectStore';
import MenuList from './pages/customer/MenuList';
import MenuDetail from './pages/customer/MenuDetail';
import MyPage from './pages/customer/MyPage';
import Cart from './pages/customer/Cart';
import OrderComplete from './pages/customer/OrderComplete';
import OrderHistory from './pages/customer/OrderHistory';
import RecommendMenu from './pages/customer/RecommendMenu';
import Favorites from './pages/customer/Favorites';
import SellerLayout from './layouts/SellerLayout';
import SellerOrders from './pages/seller/Orders';
import Forbidden from './pages/Forbidden';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* 랜딩 페이지 */}
        <Route path="/" element={<Landing />} />

        {/* 통합 로그인 */}
        <Route path="/login" element={<Login />} />

        {/* 기존 로그인 경로 리다이렉트 (호환성) */}
        <Route path="/customer/login" element={<Navigate to="/login" replace />} />
        <Route path="/admin/login" element={<Navigate to="/login" replace />} />

        {/* Customer 라우트 */}
        <Route path="/customer/select_store" element={<SelectStore />} />
        <Route path="/customer/my" element={<MyPage />} />
        <Route path="/customer/cart" element={<Cart />} />
        <Route path="/customer/order-complete" element={<OrderComplete />} />
        <Route path="/customer/history" element={<OrderHistory />} />
        <Route path="/customer/pick" element={<RecommendMenu />} />
        <Route path="/customer/favorites" element={<Favorites />} />
        <Route path="/customer/stores/:storeId/menus" element={<MenuList />} />
        <Route path="/customer/stores/:storeId/menus/:menuId" element={<MenuDetail />} />

        {/* Seller 라우트 (중첩 라우트) */}
        <Route path="/seller" element={<SellerLayout />}>
          <Route index element={<Navigate to="/seller/orders" replace />} />
          <Route path="orders" element={<SellerOrders />} />
          {/* <Route path="sales" element={<SellerSales />} /> */}
        </Route>

        {/* Admin 라우트 (나중에 추가) */}
        {/* <Route path="/admin/dashboard" element={<AdminDashboard />} /> */}

        {/* 403 권한 없음 페이지 */}
        <Route path="/forbidden" element={<Forbidden />} />

        {/* 404 페이지 (나중에 추가) */}
        {/* <Route path="*" element={<NotFound />} /> */}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
