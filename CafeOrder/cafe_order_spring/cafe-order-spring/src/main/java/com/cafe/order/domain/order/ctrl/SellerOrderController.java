package com.cafe.order.domain.order.ctrl;

import com.cafe.order.domain.order.entity.Order;
import com.cafe.order.domain.order.dto.OrderStatus;
import com.cafe.order.domain.order.service.OrderService;
import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.store.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/seller")
public class SellerOrderController {

    private final OrderService orderService;
    private final StoreService storeService;

    public SellerOrderController(OrderService orderService, StoreService storeService) {
        this.orderService = orderService;
        this.storeService = storeService;
    }

    /**
     * 주문 관리 메뉴
     */
    // READ : 로그인된 판매자 지점 조회
    // todo : 임시 storeId 설정, 로그인 기능 이후 @RequestParam Integer storeId 으로 수정 필요
    @GetMapping("/orders")
    public String orderList(Model model) {
        Integer storeId = 1;

        // 주문 목록 조회 (COMPLETED 제외)
        List<Order> orders = orderService.findActiveOrderByStoreId(storeId);

        // Store 조회해서 이름 가져오기
        Store store = storeService.findById(storeId);

        model.addAttribute("orders", orders);
        model.addAttribute("storeName", store.getName());

        return "seller/order/list";
    }

    // READ : 상세 주문 조회
    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable UUID id, Model model) {
        Order order = orderService.findById(id);
        // order.getItems()로 OrderItem 리스트 자동 로딩 (Lazy Loading)

        model.addAttribute("order", order);
        return "seller/order/detail";
    }

    // UPDATE : 주문 상태 변경
    @PostMapping("/orders/{id}/status")
    public String updateStatus(@PathVariable UUID id, @RequestParam String status) {
        // String -> OrderStatus Enum 변환
        OrderStatus newStatus = OrderStatus.valueOf(status);

        // 삳태 업데이트
        orderService.updateStatus(id, newStatus);

        return "redirect:/seller/orders";

    }


    /**
     * 매출 관리 메뉴
     */
    // todo : 임시 storeId 설정, 로그인 기능 이후 @RequestParam Integer storeId 으로 수정 필요
    @GetMapping("/sales")
    public String salesDashboard(Model model) {
        Integer storeId = 1; // 임시 (강남점)

        // 완료된 주문 목록 조회 (COMPLETE)
        List<Order> completedOrders = orderService.findCompleteOrdersByStoreId(storeId);

        // 총 매출 계산
        int totalSales = orderService.getTotalSales(storeId);

        // 주문 건수
        int totalOrders = completedOrders.size();

        // Store 조회
        Store store = storeService.findById(storeId);

        model.addAttribute("storeName", store.getName());
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("orders", completedOrders);

        return "seller/sales/dashboard";


    }

}
