// src/api/adminMenuApi.ts
import api from './axios';
import type { AdminMenuResponse, AdminMenuCreateRequest } from '../types/admin';

const BASE_URL = '/api/v1/admin/menus';

/**
 * 전체 메뉴 조회
 */
export const getAllMenus = async (): Promise<AdminMenuResponse[]> => {
  const response = await api.get<AdminMenuResponse[]>(BASE_URL);
  return response.data;
};

/**
 * 메뉴 상세 조회
 */
export const getMenuDetail = async (menuId: string): Promise<AdminMenuResponse> => {
  const response = await api.get<AdminMenuResponse>(`${BASE_URL}/${menuId}`);
  return response.data;
};

/**
 * 메뉴 생성
 */
export const createMenu = async (data: AdminMenuCreateRequest): Promise<AdminMenuResponse> => {
  const response = await api.post<AdminMenuResponse>(BASE_URL, data);
  return response.data;
};

/**
 * 메뉴 수정
 */
export const updateMenu = async (
  menuId: string,
  data: AdminMenuCreateRequest
): Promise<AdminMenuResponse> => {
  const response = await api.put<AdminMenuResponse>(`${BASE_URL}/${menuId}`, data);
  return response.data;
};

/**
 * 메뉴 삭제
 */
export const deleteMenu = async (menuId: string): Promise<void> => {
  await api.delete(`${BASE_URL}/${menuId}`);
};
