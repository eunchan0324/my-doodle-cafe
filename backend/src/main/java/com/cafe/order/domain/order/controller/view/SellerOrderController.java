package com.cafe.order.domain.order.controller.view;

import com.cafe.order.domain.order.dto.OrderStatus;
import com.cafe.order.domain.order.entity.Order;
import com.cafe.order.domain.order.service.OrderService;
import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.global.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerOrderController {

    private final OrderService orderService;

    /**
     * [READ] 주문 관리 목록
     */
    @GetMapping("/orders")
    public String orderList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        // 보안 체크 (비로그인, 가게가 없는 유저)
        if (userDetails == null || userDetails.getStore() == null) {
            return "redirect:/login";
        }

        // 1. 로그인 유저 객체에서 Store 조회
        Store store = userDetails.getStore();
        Integer storeId = store.getId();

        // 2. 내 가게의 주문 목록 조회 (COMPLETED 제외)
        List<Order> orders = orderService.findActiveOrderByStoreId(storeId);

        model.addAttribute("orders", orders);
        model.addAttribute("storeName", store.getName());

        return "seller/order/list";
    }

    /**
     * [READ] 상세 주문 조회
     */
    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable UUID id, Model model) {
        Order order = orderService.findById(id);
        // order.getItems()로 OrderItem 리스트 자동 로딩 (Lazy Loading)

        model.addAttribute("order", order);
        return "seller/order/detail";
    }

    /**
     * [UPDATE] 주문 상태 변경
     */
    @PostMapping("/orders/{id}/status")
    public String updateStatus(@PathVariable UUID id, @RequestParam String status) {
        // String -> OrderStatus Enum 변환
        OrderStatus newStatus = OrderStatus.valueOf(status);

        // 삳태 업데이트
        orderService.updateStatus(id, newStatus);

        return "redirect:/seller/orders";
    }


    /**
     * [READ] 매출 관리 대시보드
     */
    @GetMapping("/sales")
    public String salesDashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        // 보안 체크
        if (userDetails == null || userDetails.getStore() == null) {
            return "redirect:/login";
        }

        // Store 조회
        Store store = userDetails.getStore();
        Integer storeId = store.getId();

        // 완료된 주문 목록 조회 (COMPLETE)
        List<Order> completedOrders = orderService.findCompleteOrdersByStoreId(storeId);

        // 총 매출 계산
        int totalSales = orderService.getTotalSales(storeId);

        // 주문 건수
        int totalOrders = completedOrders.size();

        model.addAttribute("storeName", store.getName());
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("orders", completedOrders);

        return "seller/sales/dashboard";
    }
}
