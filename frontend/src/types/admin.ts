// src/types/admin.ts
// Admin 관련 타입 정의

export type Category = 'COFFEE' | 'BEVERAGE' | 'DESSERT';

export const CATEGORY_LABELS: Record<Category, string> = {
  COFFEE: '커피',
  BEVERAGE: '음료',
  DESSERT: '디저트',
};

export interface AdminMenuResponse {
  id: string;
  name: string;
  price: number;
  category: Category;
  description: string;
}

export interface AdminMenuCreateRequest {
  name: string;
  price: number;
  category: Category;
  description: string;
}

export interface AdminSalesResponse {
  storeName: string;
  orderCount: number;
  totalSales: number;
}

// 판매자 계정 관련
export interface SellerDto {
  id: number;
  loginId: string;
  name: string;
  storeId: number | null;
  storeName: string;
  // password는 보안상 보통 리스폰스에 포함 안하지만, User Code에서는 DTO에 포함되어 있음.
  // 필요하다면 사용하겠지만, 프론트 목록에는 표시 안하는게 관례.
}

export interface SellerCreateRequest {
  loginId: string;
  password: string;
  name: string;
  storeId: number;
}

export interface SellerUpdateRequest {
  password?: string;
  name?: string;
  storeId?: number;
}

// 지점 선택용 (StoreResponse)
export interface SimpleStoreResponse {
  id: number;
  name: string;
}
