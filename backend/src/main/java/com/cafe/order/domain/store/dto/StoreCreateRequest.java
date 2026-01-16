package com.cafe.order.domain.store.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 가게 생성 및 수정 요청을 받을 때 사용하는 DTO입니다.
 * <p>
 * 역할:
 * 1. 클라이언트(프론트엔드)가 보낸 JSON 데이터를 이 객체에 담습니다.
 * 2. 입력값의 유효성 검사(Validation)를 수행합니다.
 * </p>
 */
@Getter
@NoArgsConstructor // 역직렬화(JSON -> Java 객체)를 위해 기본 생성자가 필수입니다.
public class StoreCreateRequest {

    /**
     * 가게 이름
     * <p>
     * @NotBlank: null, 빈 문자열(""), 공백(" ")을 모두 허용하지 않습니다.
     * message: 검증 실패 시 클라이언트에게 보낼 에러 메시지입니다.
     * </p>
     */
    @NotBlank(message = "가게 이름은 필수입니다.")
    private String name;

    // 테스트 코드 등에서 편하게 객체를 만들기 위한 생성자
    public StoreCreateRequest(String name) {
        this.name = name;
    }
}
