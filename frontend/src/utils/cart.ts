// src/utils/cart.ts

export type Temperature = 'ICE' | 'HOT';
export type CupType = 'DISPOSABLE' | 'STORE' | 'PERSONAL';
export type ShotOption = 'NONE' | 'BASIC' | 'LIGHT' | 'EXTRA' | 'DECAFFEINATED';

export type CartItem = {
  menuId: string;
  name: string;
  basePrice: number;
  quantity: number;
  temperature: Temperature;
  cupType: CupType;
  shotOption: ShotOption;
  storeId: number;
  storeName: string;
};

const CART_KEY = 'cart';

export function getCart(): CartItem[] {
  try {
    const raw = localStorage.getItem(CART_KEY);
    if (!raw) return [];
    return JSON.parse(raw) as CartItem[];
  } catch {
    return [];
  }
}

export function saveCart(cart: CartItem[]): void {
  localStorage.setItem(CART_KEY, JSON.stringify(cart));
}

export function addToCart(item: CartItem): void {
  const cart = getCart();
  // 같은 메뉴 + 같은 옵션이면 수량만 증가
  const existingIndex = cart.findIndex(
    (existing) =>
      existing.menuId === item.menuId &&
      existing.temperature === item.temperature &&
      existing.cupType === item.cupType &&
      existing.shotOption === item.shotOption &&
      existing.storeId === item.storeId,
  );

  if (existingIndex >= 0) {
    cart[existingIndex].quantity += item.quantity;
  } else {
    cart.push(item);
  }

  saveCart(cart);
}

export function updateCartItemQuantity(index: number, quantity: number): void {
  const cart = getCart();
  if (index >= 0 && index < cart.length) {
    cart[index].quantity = Math.max(1, quantity);
    saveCart(cart);
  }
}

export function removeFromCart(index: number): void {
  const cart = getCart();
  if (index >= 0 && index < cart.length) {
    cart.splice(index, 1);
    saveCart(cart);
  }
}

export function clearCart(): void {
  localStorage.removeItem(CART_KEY);
}

export function getCartCount(): number {
  return getCart().reduce((sum, item) => sum + item.quantity, 0);
}

// 옵션별 가격 증감
export const CUP_PRICE_DELTA: Record<CupType, number> = {
  DISPOSABLE: 0,
  STORE: 0,
  PERSONAL: -300,
};

export const SHOT_PRICE_DELTA: Record<ShotOption, number> = {
  NONE: 0,
  BASIC: 0,
  LIGHT: 0,
  EXTRA: 500,
  DECAFFEINATED: 1000,
};

export function calcItemTotalPrice(item: CartItem): number {
  const optionDelta = CUP_PRICE_DELTA[item.cupType] + SHOT_PRICE_DELTA[item.shotOption];
  return (item.basePrice + optionDelta) * item.quantity;
}

export function calcCartTotalPrice(cart: CartItem[]): number {
  return cart.reduce((sum, item) => sum + calcItemTotalPrice(item), 0);
}
