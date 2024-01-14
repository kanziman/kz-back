package net.kanzi.kz.api;

import lombok.RequiredArgsConstructor;
import net.kanzi.kz.dto.*;
import net.kanzi.kz.dto.comment.AddCommentRequest;
import net.kanzi.kz.dto.comment.CommentResponse;
import net.kanzi.kz.service.CommentService;
import net.kanzi.kz.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class CommentApiController {

//    private final PostService postService;
    private final CommentService commentService;


    @GetMapping("/api/posts/{id}/comments")
    public ApiResponse<List<CommentResponse>> findPostComments(@PathVariable long id) {
        List<CommentResponse> comments = commentService.findCommentByPostId(id);
        return ApiResponse.of(HttpStatus.OK, comments) ;
    }

    @PostMapping("/api/posts/{postId}/comments")
    public ApiResponse<Long> addComment(@PathVariable long postId, @RequestBody AddCommentRequest request) {
        Long comment = commentService.addComment(request, postId);
        return ApiResponse.of(HttpStatus.CREATED, comment) ;
    }
    @PatchMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Long> patchComment(@PathVariable long commentId, @RequestBody AddCommentRequest request) {
        Long id = commentService.updateComment(request, commentId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(id);

    }

    @DeleteMapping("/api/posts/{id}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok()
                .build();
    }


}

