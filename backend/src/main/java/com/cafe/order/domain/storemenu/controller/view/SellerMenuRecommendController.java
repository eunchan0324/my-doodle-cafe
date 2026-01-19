package com.cafe.order.domain.storemenu.controller.view;

import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.storemenu.dto.MenuWithRecommendType;
import com.cafe.order.domain.storemenu.service.StoreMenuService;
import com.cafe.order.global.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seller/menus/recommend")
@RequiredArgsConstructor
public class SellerMenuRecommendController {

    private final StoreMenuService storeMenuService;

    /**
     * READ : 판매 메뉴 추천 관리 페이지
     * - 판매 메뉴 목록 표시 + 현재 추천 타입 표시
     */
    @GetMapping
    public String menuRecommendManage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        // 보안 체크 (비로그인, 가게가 없는 유저)
        if (userDetails == null || userDetails.getStore() == null) {
            return "redirect:/login";
        }

        // 세션으로 Store 찾기
        Store store = userDetails.getStore();
        Integer storeId = store.getId();

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
    public String recommendApply(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Map<String, String> params) {

        // 보안 체크 (비로그인, 가게가 없는 유저)
        if (userDetails == null || userDetails.getStore() == null) {
            return "redirect:/login";
        }

        // 세션으로 Store 찾기
        Store store = userDetails.getStore();
        Integer storeId = store.getId();

        // 추천 타입 업데이트
        storeMenuService.updateRecommendTypes(storeId, params);

        return "redirect:/seller/menus/recommend";
    }
}
