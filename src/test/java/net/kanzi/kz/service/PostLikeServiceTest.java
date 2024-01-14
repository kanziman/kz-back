package net.kanzi.kz.service;

import jakarta.persistence.EntityManager;
import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.Role;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.dto.post.AddPostRequest;
import net.kanzi.kz.dto.post.PostResponse;
import net.kanzi.kz.repository.LikeRepository;
import net.kanzi.kz.repository.PostRepository;
import net.kanzi.kz.repository.TagRepository;
import net.kanzi.kz.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class PostLikeServiceTest {
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostService postService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LikeRepository likeRepository;

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
        likeRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }


    @Test
    @DisplayName("유저가 좋아요 한 글인지 확인할 수 있다.")
    public void hasLike() {

        //given
        AddPostRequest request = AddPostRequest.builder()
                .title("t").content("c").category("cate")
                .build();

        PostResponse postResponse = postService.addPost(request);

        //when
        postService.addLike(postResponse.getId(), user.getUid());
        boolean hasLike = postService.hasLike(postResponse.getId(), user.getUid());

        //then
        assertThat(hasLike).isTrue();

    }


    @DisplayName("좋아요 누르면 포스트 좋아요 수가 1 증가하고, 한번 더 누르면 1 감소한다.(취소)")
    @TestFactory
    Collection<DynamicTest> likeAndCancel() throws Exception {
        //given
        AddPostRequest request = AddPostRequest.builder()
                .title("t").content("c").category("cate")
                .build();

        PostResponse postResponse = postService.addPost(request);

        //when
        return List.of(
            DynamicTest.dynamicTest("좋아요를 누르면 카운트가 1 증가한다.", ()->{
                //when
                postService.addLike(postResponse.getId(), user.getUid());
                //then
                Post post = postRepository.findById(postResponse.getId()).get();
                assertThat(post).isNotNull()
                        .extracting("likeCount").isEqualTo(1);
            }),
            DynamicTest.dynamicTest("좋아요 한 글에 다시 좋아요를 누르면, 1 감소한다.", ()->{
                //when
                postService.addLike(postResponse.getId(), user.getUid());

                //then
                Post post = postRepository.findById(postResponse.getId()).get();
                assertThat(post).isNotNull()
                        .extracting("likeCount").isEqualTo(0);
            })
        );
    }

    private User createUser(String uid, String email, Role role) {
        return User.builder()
                .uid(uid)
                .email(email)
                .roleType(role)
                .build();
    }


}