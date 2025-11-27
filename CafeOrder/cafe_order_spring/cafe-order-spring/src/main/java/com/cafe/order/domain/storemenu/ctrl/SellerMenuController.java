package com.cafe.order.domain.storemenu.ctrl;

import com.cafe.order.domain.menu.dto.Menu;
import com.cafe.order.domain.menu.service.MenuService;
import com.cafe.order.domain.store.dto.Store;
import com.cafe.order.domain.store.service.StoreService;
import com.cafe.order.domain.storemenu.dto.StoreMenu;
import com.cafe.order.domain.storemenu.service.StoreMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("seller/menus")
public class SellerMenuController {

    private final StoreMenuService storeMenuService;
    private final MenuService menuService;
    private final StoreService storeService;

    public SellerMenuController(StoreMenuService storeMenuService, MenuService menuService, StoreService storeService) {
        this.storeMenuService = storeMenuService;
        this.menuService = menuService;
        this.storeService = storeService;
    }

    /**
     * READ : 판매 메뉴 관리 페이지
     * - 전체 메뉴 목록 표시
     * - 현재 판매중인 메뉴는 체크박스 선택됨
     */
    @GetMapping
    public String menuManage(Model model) {
        // TODO : 실제로는 로그인한 판매자의 storeId 가져오기
        Integer storeId = 1; // 임시로 강남점(1)

        // store 찾기
        Store store = storeService.findById(storeId);

        // 전체 메뉴 목록
        List<Menu> allMenus = menuService.findAll();

        // 현재 판매중인 메뉴 ID 목록
        List<StoreMenu> storeMenus = storeMenuService.findByStoreId(storeId);

        Set<UUID> sellingMenuIds = storeMenus.stream()
                .map(StoreMenu::getMenuId)
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
            sellingMenuIds.add(storeMenu.getMenuId());
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
    public String apply(@RequestParam(value = "menuIds", required = false) List<UUID> menuIds) {
        // TODO : 실제로는 로그인한 판매자의 storeId 가져오기
        Integer storeId = 1; // 임시

        // Service에서 일괄 업데이트 처리
        storeMenuService.updateStoreMenus(storeId, menuIds);

        return "redirect:/seller/menus";
    }
}
