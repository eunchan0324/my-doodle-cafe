package com.cafe.order.domain.storemenu.ctrl;

import com.cafe.order.domain.store.dto.Store;
import com.cafe.order.domain.store.service.StoreService;
import com.cafe.order.domain.storemenu.dto.CustomerMenuResponse;
import com.cafe.order.domain.storemenu.service.StoreMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/customer/menus")
@Controller
public class CustomerMenuController {

    private final StoreMenuService storeMenuService;
    private final StoreService storeService;

    public CustomerMenuController(StoreMenuService storeMenuService, StoreService storeService) {
        this.storeMenuService = storeMenuService;
        this.storeService = storeService;
    }

    @GetMapping
    public String storeMenuList(Model model) {
        // TODO : 로그인 기능 이후 수정
        Integer storeId = 1; // 임시 강남점
        Store store = storeService.findById(storeId);

        List<CustomerMenuResponse> menus = storeMenuService.findAllMenusWithAvailability(storeId);

        model.addAttribute("store", store);
        model.addAttribute("menus", menus);

        return "customer/menus/list";
    }


}
