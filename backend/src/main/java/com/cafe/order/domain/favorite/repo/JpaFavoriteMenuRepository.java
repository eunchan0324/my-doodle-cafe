package com.cafe.order.domain.favorite.repo;

import com.cafe.order.domain.favorite.entity.FavoriteMenu;
import com.cafe.order.domain.favorite.entity.FavoriteMenuId;
import com.cafe.order.domain.user.entity.User; // User import 필요
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaFavoriteMenuRepository extends JpaRepository<FavoriteMenu, FavoriteMenuId> {

    /**
     * 특정 유저(PK)의 찜 목록 조회
     * - findByUser_Id (Integer) -> User 객체의 ID로 검색
     */
    List<FavoriteMenu> findByUser_Id(Integer userId);

    /**
     * 찜 존재 여부 확인
     * - existsByUser_IdAndMenu_Id
     * (User의 ID와 Menu의 ID를 조건으로 검사)
     */
    boolean existsByUser_IdAndMenu_Id(Integer userId, UUID menuId);

    Integer user(User user);
}