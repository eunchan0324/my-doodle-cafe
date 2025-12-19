package com.cafe.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seller")
public class SellerController {

    // 판매자 대시보드 (메인)
    @GetMapping("/dashboard")
    public String dashboard() {
        return "seller/dashboard";
    }

}
