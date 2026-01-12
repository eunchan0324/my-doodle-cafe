package com.cafe.order.domain.order.ctrl;

import com.cafe.order.domain.order.dto.CustomerOrderSummary;
import com.cafe.order.domain.order.entity.Order;
import com.cafe.order.domain.order.service.OrderService;
import com.cafe.order.domain.store.service.StoreService;
import com.cafe.order.domain.storemenu.service.StoreMenuService;
import com.cafe.order.global.security.dto.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/customer/orders")
@Controller
@RequiredArgsConstructor
public class CustomerOrderController {

    private final OrderService orderService;

    @GetMapping("/check")
    public String orderCheck(
            @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        // 비로그인 처리
        if (userDetails == null) {
            return "redirect:/login";
        }

        // TODO : 로그인 기능 이후 수정
        Integer storeId = 1; // 임시 강남점

        Integer userId = userDetails.getId();

        List<CustomerOrderSummary> customerOrderSummaries = orderService.findOrderSummaries(storeId, userId);

        model.addAttribute("orderSummaries", customerOrderSummaries);

        return "customer/order/summary";
    }

    /**
     * 장바구니로부터 주문을 처리
     */
    @PostMapping
    public String createOrderFromCart(@AuthenticationPrincipal CustomUserDetails userDetails, HttpSession session) {
        // 로그인 방어 로직
        if (userDetails == null) {
            return "redirect:/login";
        }

        Integer userId = userDetails.getId();

        // todo : 지점 선택 기능 구현 후 수정
        Integer storeId = 1;

        UUID orderId = orderService.createOrderFromCart(userId, storeId, session);

        return "redirect:/customer/orders/" + orderId;
    }

    /**
     * 주문 완료 상세페이지
     */
    @GetMapping("{orderId}")
    public String orderSuccess(@PathVariable UUID orderId, Model model) {
        // 1. OrderId로 주문 정보 조회
        Order order = orderService.findById(orderId);

        // 2. 대기 번호와 주문 ID를 View에 전달
        model.addAttribute("waitingNumber", order.getWaitingNumber());
        model.addAttribute("orderId", order.getOrderId());

        return "customer/order/success";
    }
}
