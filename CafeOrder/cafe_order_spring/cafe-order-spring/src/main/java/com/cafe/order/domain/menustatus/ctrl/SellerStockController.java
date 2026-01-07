package com.cafe.order.domain.menustatus.ctrl;

import com.cafe.order.domain.menustatus.entity.MenuStatus;
import com.cafe.order.domain.storemenu.entity.SalesStatus;
import com.cafe.order.domain.menustatus.service.SellerStockService;
import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.store.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/seller/stock")
@Controller
public class SellerStockController {

    private final SellerStockService sellerStockService;
    private final StoreService storeService;

    public SellerStockController(SellerStockService sellerStockService, StoreService storeService) {
        this.sellerStockService = sellerStockService;
        this.storeService = storeService;
    }

    /**
     * READ : 재고/판매 상태 조회
     */
    @GetMapping
    public String stockManage(Model model) {
        // TODO : 실제로는 로그인한 판매자의 storeId 가져오기
        Integer storeId = 1; // 임시 강남점 (1)
        Store store = storeService.findById(storeId);

        List<MenuStatus> menuStatusList = sellerStockService.findByStoreId(storeId);

        model.addAttribute("store", store);
        model.addAttribute("menuStatuses", menuStatusList);

        return "seller/menustatus/manage";

    }

    /**
     * UPDATE : 재고/판매 상태 일괄 수정
     */
    @PostMapping("/{menuId}/update")
    public String updateMenuStatus(@PathVariable UUID menuId,
                              @RequestParam int stock, @RequestParam SalesStatus status) {
        // TODO : 실제로는 로그인한 판매자의 storeId 가져오기
        Integer storeId = 1; // 임시 강남점 (1)
        sellerStockService.updateMenuStatus(storeId, menuId, stock, status);
        return "redirect:/seller/stock";
    }

}
