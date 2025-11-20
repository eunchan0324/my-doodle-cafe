package com.cafe.order.domain.order.ctrl;

import com.cafe.order.domain.order.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // READ : 전체 매출 조회
    @GetMapping
    public String list(Model model) {
        model.addAttribute("sales", orderService.getSalesByStore());
        return "sales/list";
    }





}
