package com.cafe.order.domain.store.controller.api;

import com.cafe.order.domain.store.dto.StoreCreateRequest;
import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.store.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreApiController.class) // Controller만 테스트하겠다는 선언
class StoreApiControllerTest {

    @Autowired
    private MockMvc mockMvc; // 가짜 요청을 보내는 도구

    @Autowired
    private ObjectMapper objectMapper; // 객체 -> JSON 변환 도구

    @MockitoBean // 실제 Service 대신 가짜(Mock) Service를 주입
    private StoreService storeService;

    @Test
    @DisplayName("가게 목록 조회 성공 테스트")
    @WithMockUser // Spring Security가 있으면 인증된 사용자로 가정해야 테스트가 통과됨
    void getAllStores() throws Exception {
        // given: 가짜 데이터 준비
        Store store1 = new Store("강남점");
        // 테스트용이라 ID를 강제로 넣어줍니다 (실제로는 DB가 넣어줌)
        // Reflection 등을 써야하지만 간단히 Mock 동작을 정의
        given(storeService.findAll()).willReturn(List.of(store1));

        // when: GET 요청 보내기
        mockMvc.perform(get("/api/v1/stores"))
                .andDo(print()) // 결과를 콘솔에 출력 (디버깅용)
                .andExpect(status().isOk()) // 상태 코드가 200 OK인지 확인
                .andExpect(jsonPath("$[0].name").value("강남점")); // JSON 데이터 검증
    }

    @Test
    @DisplayName("가게 생성 성공 테스트")
    @WithMockUser(roles = "ADMIN") // 관리자 권한으로 가정
    void createStore() throws Exception {
        // given
        StoreCreateRequest request = new StoreCreateRequest("판교점");
        Store savedStore = new Store("판교점");

        // service.create()가 호출되면 savedStore를 리턴하도록 설정
        given(storeService.create(any(String.class))).willReturn(savedStore);

        // when: POST 요청 보내기
        mockMvc.perform(post("/api/v1/stores")
                        .with(csrf()) // POST 요청은 CSRF 토큰이 필요함 (Security 설정 때문)
                        .contentType(MediaType.APPLICATION_JSON) // 보내는 데이터가 JSON임을 명시
                        .content(objectMapper.writeValueAsString(request))) // 객체를 JSON 문자열로 변환
                .andDo(print())
                .andExpect(status().isCreated()) // 201 Created 확인
                .andExpect(jsonPath("$.name").value("판교점")); // 응답 데이터 확인
        
        // then: 실제로 서비스의 create 메서드가 호출되었는지 검증
        verify(storeService).create("판교점");
    }
}
