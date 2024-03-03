package net.kanzi.kz.controller;

import net.kanzi.kz.ControllerTestSupport;
import net.kanzi.kz.dto.post.AddPostRequest;
import net.kanzi.kz.dto.post.PostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class PostApiControllerTest  extends ControllerTestSupport {


    @DisplayName("새글을 등록한다.")
    @Test
    void createPost() throws Exception {

        given(postService.addPost(any(AddPostRequest.class)))
                .willReturn(PostResponse.builder()
                        .id(1L)
                        .title("제목을 기재합니다.")
                        .uid("3a727380-bca1-4aa1-b65d-8f887f7202ee")
                        .content("내용을 기재합니다.")
                        .category("NOTICE")
                        .tags(List.of("삼성","현대"))
                        .nickName("돌고래1231")
                        .photoURL("bear.com")
                        .createdAt(LocalDateTime.now())
                        .build()
                );
        Principal principal = () -> "user@gmail.com";
        AddPostRequest request = AddPostRequest.builder()
                .uid("user-uuid")
                .title("t").content("c").category("cate")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/posts")
                                .principal(principal)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
    @DisplayName("새글을 등록할 때 제목은 필수값이다.")
    @Test
    void createPostWithoutTitle() throws Exception {
        //given
        AddPostRequest request = AddPostRequest.builder()
                .uid("user-uuid")
                .content("any-content")
                .category("cate")
                .build();
        // when // then
        mockMvc.perform(
                        post("/api/posts")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("제목은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @DisplayName("새글을 등록할 때 내용은 필수값이다.")
    @Test
    void createPostWithoutContent() throws Exception {
        //given
        AddPostRequest request = AddPostRequest.builder()
                .uid("user-uuid")
                .title("any-title")
                .category("cate")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/posts")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("내용은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @DisplayName("새글을 등록할 때 카테고리는 필수값이다.")
    @Test
    void createPostWithoutType() throws Exception {
        //given
        AddPostRequest request = AddPostRequest.builder()
                .uid("user-uuid")
                .title("any-title")
                .content("any-content")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/posts")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("카테고리는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }





}