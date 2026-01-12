package com.cafe.order.domain.store.ctrl;

import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.store.service.StoreService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/customer/stores")
@RequiredArgsConstructor
public class StoreSelectionController {

    private final StoreService storeService;

    /**
     * READ : 매장 선택 페이지
     */
    @GetMapping("/select")
    public String selectStorePage(Model model) {
        // 모든 매장 목록을 가져와서 뷰에 전달
        List<Store> stores = storeService.findAll();
        model.addAttribute("stores", stores);

        return "customer/store/select";
    }

    /**
     * 매장 선택 처리 (세션 저장)
     */
    @PostMapping("/select")
    public String selectStoreProcess(@RequestParam Integer storeId, HttpSession session) {
        // 세션에 '현재 선택된 매장 ID' 저장
        session.setAttribute("currentStoreId", storeId);

        // 매장 이름 저장
        Store store = storeService.findById(storeId);
        session.setAttribute("currentStoreName", store.getName());

        // 대시보드로 복귀
        return "redirect:/customer/dashboard";
    }
}
