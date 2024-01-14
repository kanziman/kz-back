package net.kanzi.kz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import net.kanzi.kz.domain.Comment;
import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.Role;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.dto.comment.AddCommentRequest;
import net.kanzi.kz.dto.post.AddPostRequest;
import net.kanzi.kz.dto.comment.CommentResponse;
import net.kanzi.kz.dto.post.PostResponse;
import net.kanzi.kz.repository.CommentRepository;
import net.kanzi.kz.repository.PostRepository;
import net.kanzi.kz.repository.TagRepository;
import net.kanzi.kz.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
class CommentServiceTest {
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    EntityManager em;

    User user;

    @BeforeEach
    void setSecurityContext() throws Exception {
        User user1 = createUser("uid1", "email1@com", Role.USER);
        user = userRepository.save(user1);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
    }
    @AfterEach
    void tearDown(){
        commentRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("로그인한 유저는 댓글을 작성할 수 있다.")
    public void createComment() {

        //given
        AddPostRequest request = AddPostRequest.builder()
                .title("t").content("c").category("cate")
                .build();
        PostResponse postResponse = postService.addPost(request);
        Post post = postRepository.findById(postResponse.getId()).get();

        AddCommentRequest comment = AddCommentRequest.builder()
                .message("message")
                .commenter("uid")
                .build();

        //when
        Long save = commentService.addComment(comment, post.getId());

        //then
        assertThat(save).isNotNull();
    }

    @Test
    @DisplayName("포스트에 있는 댓글들을 조회할 수 있다.")
    public void findAllComments() {

        //given
        AddPostRequest request = AddPostRequest.builder()
                .title("t").content("c").category("cate")
                .build();
        PostResponse postResponse = postService.addPost(request);
        Post post = postRepository.findById(postResponse.getId()).get();

        AddCommentRequest comment = AddCommentRequest.builder()
                .message("message")
                .commenter(user.getUid())
                .build();

        //when
        commentService.addComment(comment, post.getId());
        commentService.addComment(comment, post.getId());
        List<CommentResponse> commentByPostId = commentService.findCommentByPostId(post.getId());

        //then
        assertThat(commentByPostId).hasSize(2)
                .extracting("message", "commenter")
                .containsExactlyInAnyOrder(
                        tuple("message", user.getUid()),
                        tuple("message", user.getUid())
                );
    }

    @Test
    @DisplayName("작성자는 포스트에 있는 댓글을 삭제할 수 있다.")
    public void deleteComment() {

        //given
        AddPostRequest request = AddPostRequest.builder()
                .title("t").content("c").category("cate")
                .build();
        PostResponse postResponse = postService.addPost(request);
        Post post = postRepository.findById(postResponse.getId()).get();

        AddCommentRequest comment = AddCommentRequest.builder()
                .message("message")
                .commenter(user.getUid())
                .build();

        //when
        Long saved  = commentService.addComment(comment, post.getId());
        commentService.deleteComment(saved);

        //then
        assertThat(commentRepository.findById(saved)).isEmpty();
    }

    @Test
    @DisplayName("작성자는 포스트에 있는 댓글을 수정할 수 있다.")
    public void changeComment() {

        //given
        AddPostRequest request = AddPostRequest.builder()
                .title("t").content("c").category("cate")
                .build();
        PostResponse postResponse = postService.addPost(request);
        Post post = postRepository.findById(postResponse.getId()).get();

        AddCommentRequest comment = AddCommentRequest.builder()
                .message("message")
                .commenter(user.getUid())
                .build();

        //when
        Long saved  = commentService.addComment(comment, post.getId());

        AddCommentRequest newComment = AddCommentRequest.builder()
                .message("new message")
                .commenter(user.getUid())
                .build();
        commentService.updateComment(newComment, saved);

        //then
        Comment updated = commentRepository.findById(saved).get();
        assertThat(updated).isNotNull();
        assertThat(updated.getMessage()).isEqualTo("new message");
    }

    private User createUser(String uid, String email, Role role) {
        return User.builder()
                .uid(uid)
                .email(email)
                .roleType(role)
                .build();
    }


}
