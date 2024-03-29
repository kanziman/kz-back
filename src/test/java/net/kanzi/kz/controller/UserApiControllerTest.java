package net.kanzi.kz.controller;

import net.kanzi.kz.ControllerTestSupport;
import net.kanzi.kz.dto.user.UpdateUserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserApiControllerTest extends ControllerTestSupport {
    @DisplayName("유저 업데이트 할 때 아이디값은 필수값이다.")
    @Test
    void createPostWithoutTitle() throws Exception {
        //given
        UpdateUserRequest request = UpdateUserRequest.builder()
                .nickName("nick")
                .email("e@mail.com")
                .build();

        // when // then
        mockMvc.perform(
                        put("/api/users")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("아이디값은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @DisplayName("유저 업데이트 할 때 닉네임은 필수값이다.")
    @Test
    void updateUserWithoutNickname() throws Exception {
        //given
        UpdateUserRequest request = UpdateUserRequest.builder()
                .email("e@mail.com")
                .uid("uid")
                .build();

        // when // then
        mockMvc.perform(
                        put("/api/users")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("별명은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }


}