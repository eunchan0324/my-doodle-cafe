package com.cafe.order.domain.cart.ctrl;

import com.cafe.order.domain.cart.dto.CustomerCartItem;
import com.cafe.order.domain.cart.service.CartService;
import com.cafe.order.domain.order.dto.CustomerOrderItemRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customer/cart")
public class CustomerCartController {

    private final CartService cartService;

    public CustomerCartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * POST : 메뉴 상세 페이지에서 넘어온 항목을 장바구니에 추가 처리
     */
    @PostMapping("/add")
    public String addToCart(
        // View Form에서 정의된 DTO를 자동으로 바인딩
        @ModelAttribute CustomerOrderItemRequest request,

        // 세션에 접근하기 위한 객체 (장바구니 저장소)
        HttpSession session) {

        // 1. 임시 customerId 설정 (TODO: 로그인 구현 시 변경)
        Integer customerId = 1;

        // 2. Service 호출 : 가격 계산 및 세션에 장바구니 항목 저장
        cartService.addItemToCart(customerId, request, session);

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
    public String getCartList(Model model, HttpSession session) {
        // 임시 ID (todo: 로그인 후 변경)
        Integer customerId = 1;

        List<CustomerCartItem> cartItems = cartService.getCartItems(customerId, session);

        // 총 금액 계산
        Integer totalPrice = cartService.calculateTotalPrice(cartItems);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);

        return "customer/cart/list";
    }


    // 테스트 컨트롤러
    @GetMapping("/check-session")
    @ResponseBody
    public List<CustomerCartItem> checkCartSession(HttpSession session) {
        // ... (아까 논의했던 세션 확인 로직) ...

        Integer customerId = 1;
        String cartSessionKey = "customer_cart_" + customerId;

        Object cartAttribute = session.getAttribute(cartSessionKey);

        if (cartAttribute instanceof List) {
            return (List<CustomerCartItem>) cartAttribute;
        }
        return List.of();
    }

}
