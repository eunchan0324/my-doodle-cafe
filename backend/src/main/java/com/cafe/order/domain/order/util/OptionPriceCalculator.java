package com.cafe.order.domain.order.util;

import com.cafe.order.domain.menu.dto.Category;
import com.cafe.order.domain.menu.dto.CupType;
import com.cafe.order.domain.menu.dto.ShotOption;
import com.cafe.order.domain.menu.dto.Temperature;

public class OptionPriceCalculator {

    public static int calculate(
            Category category,
            int basePrice,
            Temperature temp,
            CupType cup,
            ShotOption shot

    ) {
        int result = basePrice;

        // 1) ShotOption (커피만)
        if (category == Category.COFFEE && shot != null) {
            result += shot.getPriceDelta();
        }

        // 2) CupType (커피,음료만 / 디저트는 제외)
        if (category != Category.DESSERT) {
            result += cup.getPriceDelta();
        }

        // 3) Temperature : 현재는 가격 변화 없음

        return result;
    }
}
