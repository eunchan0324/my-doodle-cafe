package com.cafe.order.domain.order.ctrl;

import com.cafe.order.domain.order.dto.CreateOrderRequest;
import com.cafe.order.domain.order.dto.CustomerOrderSummary;
import com.cafe.order.domain.order.dto.Order;
import com.cafe.order.domain.order.service.OrderService;
import com.cafe.order.domain.store.dto.Store;
import com.cafe.order.domain.store.service.StoreService;
import com.cafe.order.domain.storemenu.dto.CustomerMenuResponse;
import com.cafe.order.domain.storemenu.dto.MenuWithAvailability;
import com.cafe.order.domain.storemenu.service.StoreMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/customer/orders")
@Controller
public class CustomerOrderController {

    private final OrderService orderService;
    private final StoreService storeService;
    private final StoreMenuService storeMenuService;

    public CustomerOrderController(OrderService orderService, StoreService storeService, StoreMenuService storeMenuService) {
        this.orderService = orderService;
        this.storeService = storeService;
        this.storeMenuService = storeMenuService;
    }

    @GetMapping("/new")
    public String showOrderForm(Model model) {
        // TODO : 로그인 기능 이후 수정
        Integer storeId = 1; // 임시 강남점

        Store store = storeService.findById(storeId);
        if (store == null) {
            throw new IllegalArgumentException("존재하지 않는 지점입니다: " + storeId);
        }

        List<CustomerMenuResponse> sellableMenus = storeMenuService.findSellableMenus(storeId);


        model.addAttribute("store", store);
        model.addAttribute("menus", sellableMenus);

        return "customer/order/form";
    }

    @PostMapping("/new")
    public String createOrder(@ModelAttribute CreateOrderRequest req,
                              @RequestParam Integer storeId) {

        UUID orderId = orderService.createOrder(req, storeId);

        return "redirect:/customer/orders/success?orderId=" + orderId;
    }

    @GetMapping("/success")
    public String orderSuccess(@RequestParam UUID orderId, Model model) {
        Order order = orderService.findById(orderId);
        Integer waitingNumber = order.getWaitingNumber();

        model.addAttribute("waitingNumber", waitingNumber);
        return "customer/order/success";
    }

    @GetMapping("/check")
    public String orderCheck(Model model) {
        // TODO : 로그인 기능 이후 수정
        Integer storeId = 1; // 임시 강남점

        // TODO : 로그인 기능 이후 수정
        String customerId = "customer1"; // 임시 ID

        List<CustomerOrderSummary> customerOrderSummaries = orderService.findOrderSummaries(storeId, customerId);

        model.addAttribute("orderSummaries", customerOrderSummaries);

        return "customer/order/summary";
    }

}
