// src/App.tsx
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Landing from './pages/Landing';
import CustomerStores from './pages/customer/Stores';
import AdminLogin from './pages/admin/Login';
import './App.css';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* 랜딩 페이지 */}
        <Route path="/" element={<Landing />} />

        {/* Customer 라우트 */}
        <Route path="/customer/stores" element={<CustomerStores />} />
        {/* <Route path="/customer/menu" element={<CustomerMenu />} /> */}

        {/* Seller 라우트 (나중에 추가) */}
        {/* <Route path="/seller/dashboard" element={<SellerDashboard />} /> */}

        {/* Admin 라우트 */}
        <Route path="/admin/login" element={<AdminLogin />} />
        {/* <Route path="/admin/dashboard" element={<AdminDashboard />} /> */}

        {/* 404 페이지 (나중에 추가) */}
        {/* <Route path="*" element={<NotFound />} /> */}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
