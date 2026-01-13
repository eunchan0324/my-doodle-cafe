package com.cafe.order.controller;

import com.cafe.order.global.security.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seller")
public class SellerController {

    // 판매자 대시보드 (메인)
    @GetMapping("/dashboard")
    public String dashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        if (userDetails != null && userDetails.getStore() != null) {
            model.addAttribute("storeName", userDetails.getStore().getName());
        }

        return "seller/dashboard";
    }

}
