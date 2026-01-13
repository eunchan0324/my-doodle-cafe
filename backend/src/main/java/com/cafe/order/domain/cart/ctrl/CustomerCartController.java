package com.cafe.order.domain.cart.ctrl;

import com.cafe.order.domain.cart.dto.CustomerCartItem;
import com.cafe.order.domain.cart.service.CartService;
import com.cafe.order.domain.order.dto.CustomerOrderItemRequest;
import com.cafe.order.global.security.dto.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer/cart")
public class CustomerCartController {

    private final CartService cartService;

    /**
     * POST : 메뉴 상세 페이지에서 넘어온 항목을 장바구니에 추가 처리
     */
    @PostMapping("/add")
    public String addToCart(
            @AuthenticationPrincipal CustomUserDetails userDetails, // 인증된 사용자 정보 주입
            @ModelAttribute CustomerOrderItemRequest request,
            HttpSession session) {

        // 로그인 안 된 상태 처리 (혹시 모를 방어 로직)
        if (userDetails == null) {
            return "redirect:/login";
        }

        // 1. 로그인 유저 ID 사용
        Integer userId = userDetails.getId();

        // 2. Service 호출 : 가격 계산 및 세션에 장바구니 항목 저장
        cartService.addItemToCart(userId, request, session);

        // 3. 중간 확인 페이지로 리다이렉트 (다른 메뉴 보기 / 장바구니 보기 선택지 제공)
        return "redirect:/customer/cart/added";
    }


    /**
     * READ : 중간 확인 페이지 매핑
     */
    @GetMapping("/added")
    public String cartAddedSuccess() {
        return "customer/cart/added-success";
    }


    /**
     * GET : 장바구니 목록 페이지
     */
    @GetMapping
    public String getCartList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model,
            HttpSession session) {

        // 로그인 안 된 상태 방어 로직
        if (userDetails == null) {
            return "redirect:/login";
        }

        // 실제 로그인 ID
        Integer userId = userDetails.getId();

        List<CustomerCartItem> cartItems = cartService.getCartItems(userId, session);

        // 총 금액 계산
        Integer totalPrice = cartService.calculateTotalPrice(cartItems);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);

        return "customer/cart/list";
    }
}
