package com.cafe.order.domain.cart.service;

import com.cafe.order.domain.cart.dto.CustomerCartItem;
import com.cafe.order.domain.menu.dto.Menu;
import com.cafe.order.domain.menu.repo.JpaMenuRepository;
import com.cafe.order.domain.order.dto.CustomerOrderItemRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CartService {

    private final JpaMenuRepository menuRepository;

    public CartService(JpaMenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    /**
     * 장바구니에 항목을 추가하고 세션에 저장하는 로직
     */
    public void addItemToCart(Integer customerId, CustomerOrderItemRequest request, HttpSession session) {
        // 1. Request DTO를 이용하여 DB에서 메뉴 정보 조회
        Menu menu = menuRepository.findById(request.getMenuId())
            .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

        // 2. 옵션 가격을 포함하여 최종 가격 계산
        // 2-1. 기본 가격 및 수량
        Integer basePrice = menu.getPrice();
        Integer quantity = request.getQuantity();

        // 2-2. 옵션 가격 변동분 합산
        Integer optionPriceDelta =
            request.getCupType().getPriceDelta() +
            request.getShotOption().getPriceDelta();

        // 2-3. 개별 항목의 최종 가격 계산 (기본 가격 + 변동) * 수량
        Integer finalPrice = (basePrice + optionPriceDelta) * quantity;

        // 3. CustomerCartItem DTO 생성
        CustomerCartItem cartItem = new CustomerCartItem(
            request.getMenuId(),
            menu.getName(),
            request.getQuantity(),
            finalPrice,
            request.getTemperature(),
            request.getCupType(),
            request.getShotOption()
        );

        // 4. 세션에서 장바구니 리스트를 가져와 항목을 추가하고 다시 세션에 저장
        // 4-1. 세션에서 장바구니 리스트를 가져와 항목을 추가하고 없으면 새로 생성
        String cartSessionKey = "customer_cart_" + customerId;

        Object cartAttribute = session.getAttribute(cartSessionKey);
        List<CustomerCartItem> cartItems;

        if (cartAttribute == null) {
            cartItems = new ArrayList<>();
        } else {
            cartItems = (List<CustomerCartItem>) cartAttribute;
        }

        // 4-2. 장바구니에 새로운 항목 추가
        cartItems.add(cartItem);

        // 4-3. 변경된 리스트를 다시 세션에 저장
        session.setAttribute(cartSessionKey, cartItems);
    }

}
