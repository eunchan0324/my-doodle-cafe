package com.cafe.order.domain.storemenu.ctrl;

import com.cafe.order.domain.storemenu.dto.CustomerRecommendMenuDto;
import com.cafe.order.domain.storemenu.service.StoreMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/customer/menus/recommend")
public class CustomerMenuRecommendController {

    private final StoreMenuService storeMenuService;

    public CustomerMenuRecommendController(StoreMenuService storeMenuService) {
        this.storeMenuService = storeMenuService;
    }

    @GetMapping
    public String RecommendList(Model model) {
        // todo : 임시 강남점
        Integer storeId = 1;

        List<CustomerRecommendMenuDto> recommendMenus = storeMenuService.findRecommendMenus(storeId);

        model.addAttribute("recommendMenus", recommendMenus);

        return "customer/menus/recommendList";

    }

}
