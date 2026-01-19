package com.cafe.order.domain.storemenu.controller.view;

import com.cafe.order.domain.storemenu.dto.CustomerRecommendMenuDto;
import com.cafe.order.domain.storemenu.service.StoreMenuService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/customer/menus/recommend")
@RequiredArgsConstructor
public class CustomerMenuRecommendController {

    private final StoreMenuService storeMenuService;

    @GetMapping
    public String recommendList(HttpSession session, Model model) {
        // 세션에서 지점 ID 가져오기 + 지점 정보 확인
        Integer storeId = (Integer) session.getAttribute("currentStoreId");

        if (storeId == null) {
            return "redirect:/customer/stores/select";
        }

        List<CustomerRecommendMenuDto> recommendMenus = storeMenuService.findRecommendMenus(storeId);

        model.addAttribute("recommendMenus", recommendMenus);

        return "customer/menus/recommendList";
    }
}
