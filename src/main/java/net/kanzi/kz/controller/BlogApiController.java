package net.kanzi.kz.controller;

import lombok.RequiredArgsConstructor;
import net.kanzi.kz.domain.Comment;
import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.Tag;
import net.kanzi.kz.dto.*;
import net.kanzi.kz.dto.post.PageResultDTO;
import net.kanzi.kz.dto.post.PostRequestDto;
import net.kanzi.kz.service.BlogService;
import net.kanzi.kz.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class BlogApiController {

    private final BlogService blogService;
    private final PostService postService;


    @PostMapping("/api/posts")
    public ResponseEntity<Post> addPost(@RequestBody AddPostRequest request, Principal principal) {
        Post save = postService.save(request, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(save);
    }
    @GetMapping("/api/posts")
    public ResponseEntity<PageResultDTO<PostResponse, Object[]>> findAllPosts(PostRequestDto params, Principal principal) {
        System.out.println("params = " + params);
        PageResultDTO<PostResponse, Object[]> posts = postService.findAllWithTags(params, principal);

//        List<PostResponse> fetch = postService.fetch(params);
//        PageResultDTO<PostResponse, Object[]> posts = new PageResultDTO<>();

        return ResponseEntity.ok()
                .body(posts);
    }
    @GetMapping("/api/posts/{id}")
    public ResponseEntity<PostResponse> findPost(@PathVariable long id, Principal principal) {

        PostResponse postResponse = postService.findById(id, principal);

        return ResponseEntity.ok()
                .body(postResponse);
    }

    @PutMapping("/api/posts/{id}")
    public ResponseEntity<Post> putPost(@PathVariable long id, @RequestBody AddPostRequest request) {
        System.out.println("id = " + id);
        System.out.println("request = " + request);
        Post update = postService.update(id, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(update);
    }

    @DeleteMapping("/api/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable long id) {
        postService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/api/posts/{id}/comments")
    public ResponseEntity<List<CommentResponse>> findPostComments(@PathVariable long id) {
        System.out.println("post id = " + id);
        List<CommentResponse> commentByPostId = postService.findCommentByPostId(id);

        return ResponseEntity.ok()
                .body(commentByPostId);
    }

    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable long postId, @RequestBody AddCommentRequest request, Principal principal) {
        System.out.println("postId = " + request);
        System.out.println("request = " + request);
        Comment comment = postService.saveComment(request, principal.getName(), postId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(comment);
    }

    @DeleteMapping("/api/posts/{id}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long commentId) {
        postService.deleteComment(commentId);

        return ResponseEntity.ok()
                .build();
    }



    // === LIKE
    @PostMapping("/api/posts/{postId}/like")
    public ResponseEntity<String> addPostLike( @PathVariable long postId, @RequestBody AddLikeRequest addLikeRequest) {
        postService.addLike(postId, addLikeRequest.getUid());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("saved");
    }
    @GetMapping("/api/posts/{postId}/like/{uid}")
    public ResponseEntity<Boolean> getPostLike(@PathVariable long postId, @PathVariable String uid) {
        boolean hasLike = postService.hasLike(postId, uid);
        return ResponseEntity.ok()
                .body(hasLike);
    }

    // == BOOK MARK
    @PostMapping("/api/posts/{postId}/bookMarks")
    public ResponseEntity<String> addPostBookMark( @PathVariable long postId, @RequestBody AddBookMarkRequest bookMarkRequest            ) {
        postService.addBookMark(postId, bookMarkRequest.getUid());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("saved");
    }
    @GetMapping("/api/posts/{postId}/bookMarks/{uid}")
    public ResponseEntity<Boolean> getPostBookMark(@PathVariable long postId, @PathVariable String uid) {
        System.out.println("get request = " + uid);
        System.out.println("get postId = " + postId);
        boolean hasBookMark = postService.hasBookMark(postId, uid);
        return ResponseEntity.ok()
                .body(hasBookMark);
    }

    // == TAG
    @GetMapping("/api/tags")
    public ResponseEntity<List<TagResponse>> getTags() {
        List<TagResponse> tags = postService.getTags();
        System.out.println("tags = " + tags);
        return ResponseEntity.ok()
                .body(tags);
    }
}

