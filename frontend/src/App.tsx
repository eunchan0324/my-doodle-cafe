// src/App.tsx
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Landing from './pages/Landing';
import AdminLogin from './pages/admin/Login';
import './App.css';
import SelectStore from './pages/customer/SelectStore';
import MenuList from './pages/customer/MenuList';
import MenuDetail from './pages/customer/MenuDetail';
import CustomerLogin from './pages/customer/Login';
import MyPage from './pages/customer/MyPage';
import Cart from './pages/customer/Cart';
import OrderComplete from './pages/customer/OrderComplete';
import OrderHistory from './pages/customer/OrderHistory';
import Forbidden from './pages/Forbidden';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* 랜딩 페이지 */}
        <Route path="/" element={<Landing />} />

        {/* Customer 라우트 */}
        <Route path="/customer/login" element={<CustomerLogin />} />
        <Route path="/customer/select_store" element={<SelectStore />} />
        <Route path="/customer/my" element={<MyPage />} />
        <Route path="/customer/cart" element={<Cart />} />
        <Route path="/customer/order-complete" element={<OrderComplete />} />
        <Route path="/customer/history" element={<OrderHistory />} />
        <Route path="/customer/stores/:storeId/menus" element={<MenuList />} />
        <Route path="/customer/stores/:storeId/menus/:menuId" element={<MenuDetail />} />

        {/* Seller 라우트 (나중에 추가) */}
        {/* <Route path="/seller/dashboard" element={<SellerDashboard />} /> */}

        {/* Admin 라우트 */}
        <Route path="/admin/login" element={<AdminLogin />} />
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
