package com.cafe.order.domain.favorite.ctrl;

import com.cafe.order.domain.favorite.dto.FavoriteMenuResponse;
import com.cafe.order.domain.favorite.service.FavoriteMenuService;
import com.cafe.order.global.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/customer/favorites")
@RequiredArgsConstructor
@Controller
public class FavoriteMenuController {

    private final FavoriteMenuService favoriteMenuService;

    /**
     * READ : 구매자 찜 목록 조회
     */
    @GetMapping
    public String favoriteMenuList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        // 로그인 방어 로직
        if (userDetails == null) {
            return "redirect:/login";
        }

        Integer userId = userDetails.getId();

        List<FavoriteMenuResponse> favorites = favoriteMenuService.favoriteMenuList(userId);

        model.addAttribute("favorites", favorites);

        return "customer/favorites/list";
    }

    /**
     * POST : 찜 상태 변경
     */
    @PostMapping("/{menuId}/toggle")
    public String toggleFavorite(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID menuId,
            @RequestParam(defaultValue = "") String redirect) {

        // 로그인 방어 로직
        if (userDetails == null) {
            return "redirect:/login";
        }

        Integer userId = userDetails.getId();

        favoriteMenuService.toggleFavorite(userId, menuId);

        // 리다이렉트 분기 처리
        // 만약 리스트 페이지에서 요청했다면(redirect="list') 다시 찜 목록으로 돌아감
        if ("list".equals(redirect)) {
            return "redirect:/customer/favorites";
        }

        // 그 외 (상세 페이지 등)에서는 해당 메뉴 상세 페이지로 돌아감
        return "redirect:/customer/menus/" + menuId;
    }
}
