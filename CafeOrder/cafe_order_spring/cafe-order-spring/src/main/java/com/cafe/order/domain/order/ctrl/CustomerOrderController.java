package com.cafe.order.domain.order.ctrl;

import com.cafe.order.domain.order.dto.CustomerOrderSummary;
import com.cafe.order.domain.order.entity.Order;
import com.cafe.order.domain.order.service.OrderService;
import com.cafe.order.domain.store.service.StoreService;
import com.cafe.order.domain.storemenu.service.StoreMenuService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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

//    @GetMapping("/new")
//    public String showOrderForm(Model model) {
//        // TODO : 로그인 기능 이후 수정
//        Integer storeId = 1; // 임시 강남점
//
//        Store store = storeService.findById(storeId);
//        if (store == null) {
//            throw new IllegalArgumentException("존재하지 않는 지점입니다: " + storeId);
//        }
//
//        List<CustomerMenuResponse> sellableMenus = storeMenuService.findSellableMenus(storeId);
//
//
//        model.addAttribute("store", store);
//        model.addAttribute("menus", sellableMenus);
//
//        return "customer/order/form";
//    }
//
//    @PostMapping("/new")
//    public String createOrder(@ModelAttribute CreateOrderRequest req,
//                              @RequestParam Integer storeId) {
//
//        UUID orderId = orderService.createOrder(req, storeId);
//
//        return "redirect:/customer/orders/success?orderId=" + orderId;
//    }
//
//    @GetMapping("/success")
//    public String orderSuccess(@RequestParam UUID orderId, Model model) {
//        Order order = orderService.findById(orderId);
//        Integer waitingNumber = order.getWaitingNumber();
//
//        model.addAttribute("waitingNumber", waitingNumber);
//        return "customer/order/success";
//    }

    @GetMapping("/check")
    public String orderCheck(Model model) {
        // TODO : 로그인 기능 이후 수정
        Integer storeId = 1; // 임시 강남점

        // TODO : 로그인 기능 이후 수정
        String customerId = "1"; // 임시 ID

        List<CustomerOrderSummary> customerOrderSummaries = orderService.findOrderSummaries(storeId, customerId);

        model.addAttribute("orderSummaries", customerOrderSummaries);

        return "customer/order/summary";
    }

    /**
     * 장바구니로부터 주문을 처리
     */
    @PostMapping
    public String createOrderFromCart(HttpSession session) {
        // 임시 id 설정 (todo : 로그인 구현 후 수정)
        String customerId = "1";
        Integer storeId = 1;

        UUID orderId = orderService.createOrderFromCart(customerId, storeId, session);

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
