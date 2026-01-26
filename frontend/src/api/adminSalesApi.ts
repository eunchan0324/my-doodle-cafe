import api from './axios';
import type { AdminSalesResponse } from '../types/admin';

const BASE_URL = '/api/v1/sales';

/**
 * 관리자 전체 매출 조회
 */
export const getAllSales = async (): Promise<AdminSalesResponse[]> => {
  const response = await api.get<AdminSalesResponse[]>(BASE_URL);
  return response.data;
};
