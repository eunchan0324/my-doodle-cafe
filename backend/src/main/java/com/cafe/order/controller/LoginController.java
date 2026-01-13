package com.cafe.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // 모든 유저가 들어오는 단 하나의 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
