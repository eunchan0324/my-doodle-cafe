// src/api/adminStoreApi.ts
import api from './axios';

// API 응답에 대한 타입 정의
export interface AdminStoreResponse {
  id: number;
  name: string;
}

// 지점 생성/수정 요청에 대한 타입 정의
export interface StoreCreateRequest {
  name: string;
}

const BASE_URL = '/api/v1/stores';

/**
 * 전체 지점 조회
 */
export const getAllStores = async (): Promise<AdminStoreResponse[]> => {
  const response = await api.get<AdminStoreResponse[]>(BASE_URL);
  return response.data;
};

/**
 * 지점 생성
 */
export const createStore = async (data: StoreCreateRequest): Promise<AdminStoreResponse> => {
  const response = await api.post<AdminStoreResponse>(BASE_URL, data);
  return response.data;
};

/**
 * 지점 수정
 */
export const updateStore = async (
  id: number,
  data: StoreCreateRequest
): Promise<AdminStoreResponse> => {
  const response = await api.put<AdminStoreResponse>(`${BASE_URL}/${id}`, data);
  return response.data;
};

/**
 * 지점 삭제
 */
export const deleteStore = async (id: number): Promise<void> => {
  await api.delete(`${BASE_URL}/${id}`);
};
