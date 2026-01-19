package com.cafe.order.domain.storemenu.controller.view;

import com.cafe.order.domain.menu.entity.Menu;
import com.cafe.order.domain.menu.service.MenuService;
import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.storemenu.entity.StoreMenu;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/seller/menus")
@RequiredArgsConstructor
public class SellerMenuController {

    private final StoreMenuService storeMenuService;
    private final MenuService menuService;

    /**
     * READ : 판매 메뉴 관리 페이지
     * - 전체 메뉴 목록 표시
     * - 현재 판매중인 메뉴는 체크박스 선택됨
     */
    @GetMapping
    public String menuManage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        // 보안 체크 (비로그인, 가게가 없는 유저)
        if (userDetails == null || userDetails.getStore() == null) {
            return "redirect:/login";
        }


        // 로그인 정보로 store 찾기
        Store store = userDetails.getStore();
        Integer storeId = store.getId();

        // 전체 메뉴 목록 조회
        List<Menu> allMenus = menuService.findAll();

        // 현재 우리 지점에 판매중인 메뉴 ID 목록 조회
        List<StoreMenu> storeMenus = storeMenuService.findByStoreId(storeId);

        Set<UUID> sellingMenuIds = storeMenus.stream()
                .map(sm -> sm.getMenu().getId())
                .collect(Collectors.toSet());

        model.addAttribute("store", store);
        model.addAttribute("allMenus", allMenus);
        model.addAttribute("sellingMenuIds", sellingMenuIds);

        return "seller/menu/manage";
    }

    @Deprecated
    public String menuManageBasedRoof(Model model) {
        Integer storeId = 1;

        List<Menu> allMenus = menuService.findAll();

        List<StoreMenu> storeMenus = storeMenuService.findByStoreId(storeId);

        Set<UUID> sellingMenuIds = new HashSet<>();
        for (StoreMenu storeMenu : storeMenus) {
            sellingMenuIds.add(storeMenu.getMenu().getId());
        }

        model.addAttribute("allMenus", allMenus);
        model.addAttribute("sellingMenuIds", sellingMenuIds);
        model.addAttribute("storeId", storeId);

        return "seller/menu/manage";
    }

    /**
     * 적용하기 버튼 처리
     * - 체크된 메뉴들을 판매 메뉴로 일괄 업데이트
     * - 체크 해제된 메뉴는 판매 메뉴에서 제거
     */
    @PostMapping("/apply")
    public String apply(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "menuIds", required = false) List<UUID>menuIds) {

        // 보안 체크 (비로그인, 가게가 없는 유저)
        if (userDetails == null || userDetails.getStore() == null) {
            return "redirect:/login";
        }

        Store store = userDetails.getStore();
        Integer storeId = store.getId();

        // Service에서 일괄 업데이트 처리
        storeMenuService.updateStoreMenus(storeId, menuIds);

        return "redirect:/seller/menus";
    }
}
