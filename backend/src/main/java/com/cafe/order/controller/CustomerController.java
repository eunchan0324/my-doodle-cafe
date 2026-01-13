package com.cafe.order.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    // 구매자 대시보드 (메인)
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {

        // 1. 세션에서 매장 정보 확인
        Integer currentStoreId = (Integer) session.getAttribute("currentStoreId");

        // 2. 매장을 선택하지 않았다면 -> 매장 선택 페이지로 리다이렉트
        if (currentStoreId == null) {
            return "redirect:/customer/stores/select";
        }

        // 3. 매장이 선택되어 있다면 -> 대시보드 정상 출력
        return "customer/dashboard";
    }
}
