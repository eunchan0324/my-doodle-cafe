package com.cafe.order.domain.storemenu.ctrl;

import com.cafe.order.domain.store.dto.Store;
import com.cafe.order.domain.store.service.StoreService;
import com.cafe.order.domain.storemenu.dto.MenuWithRecommendType;
import com.cafe.order.domain.storemenu.dto.StoreMenu;
import com.cafe.order.domain.storemenu.service.StoreMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/seller/menus/recommend")
public class SellerMenuRecommendController {

    private final StoreMenuService storeMenuService;
    private final StoreService storeService;

    public SellerMenuRecommendController(StoreMenuService storeMenuService, StoreService storeService) {
        this.storeMenuService = storeMenuService;
        this.storeService = storeService;
    }

    /**
     * READ : 판매 메뉴 추천 관리 페이지
     * - 판매 메뉴 목록 표시 + 현재 추천 타입 표시
     */
    @GetMapping
    public String menuRecommendManage(Model model) {
        // TODO : 실제로는 로그인한 판매자의 storeId 가져오기
        Integer storeId = 1; // 임시 강남점 (1)

        // store 찾기
        Store store = storeService.findById(storeId);

        // 판매중인 메뉴 목록 + 추천 타입 조히
        List<MenuWithRecommendType> sellingMenus =
                storeMenuService.findStoreMenusWithRecommendType(storeId);

        model.addAttribute("store", store);
        model.addAttribute("menus", sellingMenus);

        return "seller/menu/recommend";
    }

    /**
     * UPDATE : 추천 타입 일괄 적용
     */
    @PostMapping("/apply")
    public String recommendApply(@RequestParam Map<String, String> params) {
        // TODO : 실제로는 세션에서 storeId 가져오기
        Integer storeId = 1;

        // 추천 타입 업데이트
        storeMenuService.updateRecommendTypes(storeId, params);

        return "redirect:/seller/menus/recommend";
    }

}
