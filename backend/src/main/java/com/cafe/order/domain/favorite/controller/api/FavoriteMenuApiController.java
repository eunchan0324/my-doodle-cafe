package com.cafe.order.domain.favorite.controller.api;

import com.cafe.order.domain.favorite.dto.FavoriteMenuResponse;
import com.cafe.order.domain.favorite.service.FavoriteMenuService;
import com.cafe.order.global.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class FavoriteMenuApiController {

    private final FavoriteMenuService favoriteMenuService;

    /**
     * 찜 목록 조회
     */
    @GetMapping
    public ResponseEntity<?> getFavorites(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Integer userId = userDetails.getId();

        List<FavoriteMenuResponse> favorites = favoriteMenuService.favoriteMenuList(userId);

        return ResponseEntity.ok(favorites);
    }
}
