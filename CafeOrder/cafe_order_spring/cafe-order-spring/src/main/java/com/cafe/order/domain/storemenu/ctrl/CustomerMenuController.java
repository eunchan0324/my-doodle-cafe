package com.cafe.order.domain.storemenu.ctrl;

import com.cafe.order.domain.menu.dto.Category;
import com.cafe.order.domain.store.dto.Store;
import com.cafe.order.domain.store.service.StoreService;
import com.cafe.order.domain.storemenu.dto.CustomerMenuResponse;
import com.cafe.order.domain.storemenu.service.StoreMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RequestMapping("/customer/menus")
@Controller
public class CustomerMenuController {

    private final StoreMenuService storeMenuService;
    private final StoreService storeService;

    public CustomerMenuController(StoreMenuService storeMenuService, StoreService storeService) {
        this.storeMenuService = storeMenuService;
        this.storeService = storeService;
    }

    /**
     * READ : 지점의 전체 메뉴 + 판매 가능 여부(재고 상태) 조회
     */
    @GetMapping
    public String storeMenuList(Model model) {
        // TODO : 로그인 기능 이후 수정
        Integer storeId = 1; // 임시 강남점
        Store store = storeService.findById(storeId);

        if (store == null) {
            throw new IllegalArgumentException("존재하지 않는 지점입니다: " + storeId);
        }

        List<CustomerMenuResponse> rawMenus = storeMenuService.findAllMenusWithAvailability(storeId);

        // 카테고리 별 그룹핑
        Map<Category, List<CustomerMenuResponse>> groupedMenus = rawMenus.stream()
            .collect(Collectors.groupingBy(CustomerMenuResponse::getCategory));

        model.addAttribute("store", store);
        model.addAttribute("groupedMenus", groupedMenus);
        model.addAttribute("allCategories", Categfory.values());

        return "customer/menus/list";
    }


}
