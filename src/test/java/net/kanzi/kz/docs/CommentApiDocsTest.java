package net.kanzi.kz.docs;

import net.kanzi.kz.apicontroller.CommentApiController;
import net.kanzi.kz.dto.comment.AddCommentRequest;
import net.kanzi.kz.dto.comment.CommentResponse;
import net.kanzi.kz.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentApiDocsTest extends RestDocsSupport{

    private final CommentService commentService = mock(CommentService.class);

    @Override
    protected Object initController() {
        return new CommentApiController(commentService);
    }
    @DisplayName("글에 속하는 댓글을 만들수 있다.")
    @Test
    public void create() throws Exception {
        //given
        AddCommentRequest comment = AddCommentRequest.builder()
                .message("댓글을 입력합니다.")
                .commenter("3a727380-bca1-4aa1-b65d-8f887f7202ee")
                .build();

        given(commentService.addComment(any(AddCommentRequest.class), any(Long.class)))
                .willReturn(1L);

        // when // then
        mockMvc.perform(
                        post("/api/posts/{postId}/comments", 1L)
                                .content(objectMapper.writeValueAsString(comment))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("commenter").type(JsonFieldType.STRING)
                                        .description("내용")
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
                                fieldWithPath("data").type(JsonFieldType.NUMBER)
                                        .description("생성된 코멘트 ID")
                        )
                ));

    }

    @DisplayName("글 목록 조회가 가능하다.")
    @Test
    public void findAll() throws Exception {
        CommentResponse commentResponse = CommentResponse
                .builder()
                .id(1L)
                .message("댓글을 입력합니다.")
                .commenter("3a727380-bca1-4aa1-b65d-8f887f7202ee")
                .nickName("dolphin")
                .photoURL("bear.com")
                .createdAt(LocalDateTime.now())
                .build();
        List<CommentResponse> commentResponses = List.of(commentResponse);


        given(commentService.findCommentByPostId((any(Long.class))))
                .willReturn(commentResponses);
        // when // then
        mockMvc.perform(
                        get("/api/posts/{id}/comments", 1L)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment-findList",
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
                                fieldWithPath("data").type(JsonFieldType.ARRAY)
                                        .description("응답 데이터"),

                                fieldWithPath("data.[].id").type(JsonFieldType.NUMBER)
                                        .description("댓글 ID"),
                                fieldWithPath("data.[].message").type(JsonFieldType.STRING)
                                        .description("댓글 내용"),
                                fieldWithPath("data.[].commenter").type(JsonFieldType.STRING)
                                        .description("작성자 ID"),
                                fieldWithPath("data.[].photoURL").type(JsonFieldType.STRING)
                                        .description("작성자 사진"),
                                fieldWithPath("data.[].nickName").type(JsonFieldType.STRING)
                                        .description("작성자 별명"),
                                fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING)
                                        .description("댓글 생성일시")

                        )
                ));

    }
}


