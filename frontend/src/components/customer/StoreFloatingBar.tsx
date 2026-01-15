// src/components/customer/StoreFloatingBar.tsx
import { useEffect, useState } from 'react';
import type { MouseEvent } from 'react';
import { MapPin } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

interface StoreFloatingBarProps {
  storeName?: string;
  cartCount?: number;
  onClick?: (event: MouseEvent<HTMLButtonElement>) => void;
}

export default function StoreFloatingBar({
  storeName = '매장 선택',
  cartCount = 0,
  onClick,
}: StoreFloatingBarProps) {
  const navigate = useNavigate();
  const [currentStoreName, setCurrentStoreName] = useState(storeName);

  useEffect(() => {
    const storedName = sessionStorage.getItem('selectedStoreName');
    if (storedName) {
      setCurrentStoreName(storedName);
    } else {
      setCurrentStoreName(storeName);
    }
  }, [storeName]);
  const handleClick = (event: MouseEvent<HTMLButtonElement>) => {
    if (onClick) {
      onClick(event);
      return;
    }
    navigate('/customer/select_store');
  };

  const handleCartClick = () => {
    navigate('/customer/cart');
  };

  return (
    <div className="fixed left-0 right-0 bottom-16 z-40 px-4">
      <div className="w-full max-w-mobile mx-auto flex items-center gap-3">
        <button
          type="button"
          onClick={handleClick}
          className="flex-grow flex items-center justify-center gap-2
                     bg-crayon/30 text-ink font-doodle text-base
                     border-2 border-ink
                     shadow-[3px_5px_0px_0px_#18181B]
                     rounded-tl-2xl rounded-tr-lg rounded-bl-3xl rounded-br-xl
                     -rotate-2 hover:rotate-1 hover:scale-105
                     transition-transform duration-200
                     py-3 px-4"
        >
          <MapPin className="h-5 w-5 text" strokeWidth={2.3} aria-hidden />
          <span>{currentStoreName}</span>
        </button>

        <button
          type="button"
          onClick={handleCartClick}
          className="relative h-12 w-12 flex items-center justify-center
                     bg-ink text-white border-2 border-ink
                     shadow-[3px_5px_0px_0px_#18181B]
                     rounded-[40%_60%_60%_40%/40%_40%_60%_60%]
                     rotate-3"
          aria-label="장바구니"
        >
          <span className="absolute inset-0 m-auto h-5 w-5 rounded-full bg-danger text-white text-[9px] font-bold flex items-center justify-center border-2 border-ink">
            {cartCount}
          </span>
        </button>
      </div>
    </div>
  );
}
