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
