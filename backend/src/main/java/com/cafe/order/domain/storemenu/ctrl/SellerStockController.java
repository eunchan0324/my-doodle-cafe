package com.cafe.order.domain.storemenu.ctrl;

import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.storemenu.entity.SalesStatus;
import com.cafe.order.domain.storemenu.entity.StoreMenu;
import com.cafe.order.domain.storemenu.service.StoreMenuService;
import com.cafe.order.global.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/seller/stock")
@Controller
@RequiredArgsConstructor
public class SellerStockController {

    private final StoreMenuService storeMenuService;

    /**
     * READ : 재고/판매 상태 조회
     */
    @GetMapping
    public String stockManage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        // 보안 체크 (비로그인, 가게가 없는 유저)
        if (userDetails == null || userDetails.getStore() == null) {
            return "redirect:/login";
        }

        // 세션으로 Store 찾기
        Store store = userDetails.getStore();
        Integer storeId = store.getId();

        List<StoreMenu> storeMenus = storeMenuService.findByStoreId(storeId);

        model.addAttribute("store", store);
        model.addAttribute("menuStatuses", storeMenus);

        return "seller/menustatus/manage";
    }

    /**
     * UPDATE : 재고/판매 상태 일괄 수정
     */
    @PostMapping("/{menuId}/update")
    public String updateMenuStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID menuId,
            @RequestParam int stock,
            @RequestParam SalesStatus status) {

        // 보안 체크 (비로그인, 가게가 없는 유저)
        if (userDetails == null || userDetails.getStore() == null) {
            return "redirect:/login";
        }

        // 세션으로 Store 찾기
        Store store = userDetails.getStore();
        Integer storeId = store.getId();

        // 재고, 판매 상태 수정
        storeMenuService.updateStockAndStatus(storeId, menuId, stock, status);

        return "redirect:/seller/stock";
    }
}
