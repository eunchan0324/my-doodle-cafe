import api from './axios';
import type { SellerDto, SellerCreateRequest, SellerUpdateRequest, SimpleStoreResponse } from '../types/admin';

const BASE_URL = '/api/v1/admin/sellers';

/**
 * 판매자 배정 가능한 지점 목록 조회
 */
export const getAvailableStores = async (): Promise<SimpleStoreResponse[]> => {
  const response = await api.get<SimpleStoreResponse[]>(`${BASE_URL}/available-stores`);
  return response.data;
};

/**
 * 판매자 계정 생성
 */
export const createSeller = async (data: SellerCreateRequest): Promise<SellerDto> => {
  const response = await api.post<SellerDto>(BASE_URL, data);
  return response.data;
};

/**
 * 판매자 계정 전체 조회
 */
export const getAllSellers = async (): Promise<SellerDto[]> => {
  const response = await api.get<SellerDto[]>(BASE_URL);
  return response.data;
};

/**
 * 판매자 계정 수정
 */
export const updateSeller = async (
  sellerId: number,
  data: SellerUpdateRequest
): Promise<SellerDto> => {
  const response = await api.patch<SellerDto>(`${BASE_URL}/${sellerId}`, data);
  return response.data;
};

/**
 * 판매자 계정 삭제
 */
export const deleteSeller = async (sellerId: number): Promise<void> => {
  await api.delete(`${BASE_URL}/${sellerId}`);
};
