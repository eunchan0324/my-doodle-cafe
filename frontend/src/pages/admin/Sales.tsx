// src/pages/admin/Sales.tsx
import { useEffect, useState } from 'react';
import { TrendingUp, ShoppingBag } from 'lucide-react';
import AdminTable from '../../components/admin/AdminTable';
import { getAllSales } from '../../api/adminSalesApi';
import type { AdminSalesResponse } from '../../types/admin';

export default function AdminSales() {
  const [salesData, setSalesData] = useState<AdminSalesResponse[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadSales();
  }, []);

  const loadSales = async () => {
    try {
      setLoading(true);
      const data = await getAllSales();
      setSalesData(data);
    } catch (error) {
      console.error('매출 데이터 로드 실패:', error);
    } finally {
      setLoading(false);
    }
  };

  // 총계 계산
  const totalOrderCount = salesData.reduce((acc, curr) => acc + curr.orderCount, 0);
  const totalSalesAmount = salesData.reduce((acc, curr) => acc + curr.totalSales, 0);

  return (
    <div className="space-y-6">
      {/* 헤더 */}
      <div>
        <h1 className="font-doodle text-3xl text-ink">매출 분석</h1>
        <p className="mt-1 text-sm text-gray-500">지점별 매출 현황을 확인할 수 있습니다.</p>
      </div>

      {/* 요약 카드 */}
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <div className="rounded-xl border-2 border-ink bg-white p-6 shadow-doodle transition-transform hover:-translate-y-1 hover:shadow-none">
          <div className="flex items-center gap-3">
            <div className="flex h-12 w-12 items-center justify-center rounded-full border-2 border-ink bg-doodle-yellow text-ink">
              <ShoppingBag size={24} strokeWidth={2.5} />
            </div>
            <div>
              <p className="font-sans text-sm text-gray-500">총 주문 건수</p>
              <p className="font-doodle text-2xl text-ink">
                {totalOrderCount.toLocaleString()}건
              </p>
            </div>
          </div>
        </div>

        <div className="rounded-xl border-2 border-ink bg-white p-6 shadow-doodle transition-transform hover:-translate-y-1 hover:shadow-none">
          <div className="flex items-center gap-3">
            <div className="flex h-12 w-12 items-center justify-center rounded-full border-2 border-ink bg-crayon text-white">
              <TrendingUp size={24} strokeWidth={2.5} />
            </div>
            <div>
              <p className="font-sans text-sm text-gray-500">총 매출액</p>
              <p className="font-doodle text-2xl text-ink">
                {totalSalesAmount.toLocaleString()}원
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* 테이블 */}
      {loading ? (
        <div className="rounded-xl border-2 border-ink bg-white p-12 text-center">
          <p className="font-sans text-gray-400">데이터를 불러오는 중...</p>
        </div>
      ) : salesData.length === 0 ? (
        <div className="rounded-xl border-2 border-ink bg-white p-12 text-center">
          <p className="font-sans text-gray-400">데이터가 없습니다.</p>
        </div>
      ) : (
        <AdminTable headers={['지점명', '주문 수', '총 매출']}>
          {salesData.map((item, index) => (
            <tr key={index}>
              <td className="border-t border-dotted border-gray-300 px-4 py-3 font-doodle text-base">
                {item.storeName}
              </td>
              <td className="border-t border-dotted border-gray-300 px-4 py-3 font-sans text-sm">
                {item.orderCount.toLocaleString()}건
              </td>
              <td className="border-t border-dotted border-gray-300 px-4 py-3 font-sans text-sm font-bold text-crayon">
                {item.totalSales.toLocaleString()}원
              </td>
            </tr>
          ))}
        </AdminTable>
      )}
    </div>
  );
}
