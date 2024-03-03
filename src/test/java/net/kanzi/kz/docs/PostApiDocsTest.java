package net.kanzi.kz.docs;

import net.kanzi.kz.apicontroller.PostApiController;
import net.kanzi.kz.dto.post.AddPostRequest;
import net.kanzi.kz.dto.post.PageResultDTO;
import net.kanzi.kz.dto.post.PostResponse;
import net.kanzi.kz.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostApiDocsTest extends RestDocsSupport{

    private final PostService postService = mock(PostService.class);

    @Override
    protected Object initController() {
        return new PostApiController(postService);
    }

    @DisplayName("글 목록 조회가 가능하다.")
    @Test
    public void findAll() throws Exception {
        PostResponse postResponse = PostResponse.builder()
                .id(1L)
                .title("제목을 기재합니다.")
                .uid("3a727380-bca1-4aa1-b65d-8f887f7202ee")
                .content("내용을 기재합니다.")
                .category("NOTICE")
                .tags(List.of("삼성", "현대"))
                .nickName("돌고래1231")
                .photoURL("bear.com")
                .createdAt(LocalDateTime.now())
                .build();

        PageResultDTO<Object, Object> objectPageResultDTO = PageResultDTO.builder()
                .dtoList(List.of(postResponse))
                .totalPage(2)
                .totalElements(10L)
                .page(1)
                .build();

        given(postService.getPostsPage(any()))
                .willReturn(
                        objectPageResultDTO
                );
        // when // then
        mockMvc.perform(
                get("/api/posts")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "createdAt")
                        .param("category", "notice")
                        .param("keyword", "samsung")
                        .param("tags", "samsung","hyundai")
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andDo(document("post-findList",
                queryParameters (
                        parameterWithName("page").description("조회 페이지"),
                        parameterWithName("size").description("페이지 당 개수"),
                        parameterWithName("sort").description("정렬"),
                        parameterWithName("category").description("카테고리"),
                        parameterWithName("keyword").description("검색어"),
                        parameterWithName("tags").description("태그")
                ),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                .description("코드"),
                        fieldWithPath("status").type(JsonFieldType.STRING)
                                .description("상태"),
                        fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("메시지"),
                        fieldWithPath("timeStamp").type(JsonFieldType.STRING)
                                .description("메시지"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                .description("응답 데이터"),

                        fieldWithPath("data.dtoList[].id").type(JsonFieldType.NUMBER)
                                .description("글 ID"),
                        fieldWithPath("data.dtoList[].title").type(JsonFieldType.STRING)
                                .description("글 제목"),
                        fieldWithPath("data.dtoList[].content").type(JsonFieldType.STRING)
                                .description("글 내용"),
                        fieldWithPath("data.dtoList[].category").type(JsonFieldType.STRING)
                                .description("글 분류"),
                        fieldWithPath("data.dtoList[].tags").type(JsonFieldType.ARRAY)
                                .description("글 태그"),

                        fieldWithPath("data.dtoList[].uid").type(JsonFieldType.STRING)
                                .description("유저 아이디"),
                        fieldWithPath("data.dtoList[].photoURL").type(JsonFieldType.STRING)
                                .description("유저 사진"),
                        fieldWithPath("data.dtoList[].nickName").type(JsonFieldType.STRING)
                                .description("유저 별명"),

                        fieldWithPath("data.dtoList[].readCount").type(JsonFieldType.NUMBER)
                                .description("글 조회수"),
                        fieldWithPath("data.dtoList[].likeCount").type(JsonFieldType.NUMBER)
                                .description("글 좋아요 수"),
                        fieldWithPath("data.dtoList[].bookmarkCount").type(JsonFieldType.NUMBER)
                                .description("글 북마크 수"),
                        fieldWithPath("data.dtoList[].commentCount").type(JsonFieldType.NUMBER)
                                .description("글 댓글 수"),
                        fieldWithPath("data.dtoList[].createdAt").type(JsonFieldType.STRING)
                                .description("글 작성일"),


                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("총 페이지"),
                        fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                                .description("총 숫자"),
                        fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                                .description("조회 사이즈"),
                        fieldWithPath("data.page").type(JsonFieldType.NUMBER)
                                .description("현재 페이지")
                )
        ));

    }
    @DisplayName("글 단건 조회가 가능하다.")
    @Test
    public void findOne() throws Exception {
        given(postService.findById(any(Long.class)))
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
        // when // then
        mockMvc.perform(
                        get("/api/posts/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-findOne",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("timeStamp").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("글 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING)
                                        .description("글 제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("글 내용"),
                                fieldWithPath("data.category").type(JsonFieldType.STRING)
                                        .description("글 분류"),
                                fieldWithPath("data.tags").type(JsonFieldType.ARRAY)
                                        .description("글 태그"),

                                fieldWithPath("data.uid").type(JsonFieldType.STRING)
                                        .description("유저 아이디"),
                                fieldWithPath("data.photoURL").type(JsonFieldType.STRING)
                                        .description("유저 사진주소"),
                                fieldWithPath("data.nickName").type(JsonFieldType.STRING)
                                        .description("유저 별명"),

                                fieldWithPath("data.readCount").type(JsonFieldType.NUMBER)
                                        .description("글 조회수"),
                                fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER)
                                        .description("글 좋아요 수"),
                                fieldWithPath("data.bookmarkCount").type(JsonFieldType.NUMBER)
                                        .description("글 북마크 수"),
                                fieldWithPath("data.commentCount").type(JsonFieldType.NUMBER)
                                        .description("글 댓글 수"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING)
                                        .description("글 작성일")
                        )
                ));
    }

    @DisplayName("글을 생성할 수 있다.")
    @Test
    public void create() throws Exception {
        //given
        AddPostRequest request = AddPostRequest.builder()
                .title("제목을 기재합니다.")
                .content("내용을 기재합니다.")
                .category("COMMUNITY")
                .build();

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


        // when // then
        mockMvc.perform(
                        post("/api/posts")
                                .principal(principal)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("category").type(JsonFieldType.STRING)
                                        .description("카테고리"),
                                fieldWithPath("tags").type(JsonFieldType.ARRAY)
                                        .optional()
                                        .description("태그")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("timeStamp").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),

                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("글 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING)
                                        .description("글 제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("글 내용"),
                                fieldWithPath("data.category").type(JsonFieldType.STRING)
                                        .description("글 분류"),
                                fieldWithPath("data.tags").type(JsonFieldType.ARRAY)
                                        .description("글 태그"),

                                fieldWithPath("data.uid").type(JsonFieldType.STRING)
                                        .description("유저 아이디"),
                                fieldWithPath("data.photoURL").type(JsonFieldType.STRING)
                                        .description("유저 사진주소"),
                                fieldWithPath("data.nickName").type(JsonFieldType.STRING)
                                        .description("유저 별명"),

                                fieldWithPath("data.readCount").type(JsonFieldType.NUMBER)
                                        .description("글 조회수"),
                                fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER)
                                        .description("글 좋아요 수"),
                                fieldWithPath("data.bookmarkCount").type(JsonFieldType.NUMBER)
                                        .description("글 북마크 수"),
                                fieldWithPath("data.commentCount").type(JsonFieldType.NUMBER)
                                        .description("글 댓글 수"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING)
                                        .description("글 작성일")
                        )
                ));

    }
}


