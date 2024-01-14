package net.kanzi.kz.service;

import jakarta.persistence.EntityManager;
import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.Role;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.dto.post.AddPostRequest;
import net.kanzi.kz.dto.post.PostResponse;
import net.kanzi.kz.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostBookmarkServiceTest {
    @Autowired
    BookMarkRepository bookMarkRepository;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

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
        bookMarkRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }


    @Test
    @DisplayName("유저가 북마크 한 글인지 확인할 수 있다.")
    public void hasBookmark() {

        //given
        AddPostRequest request = AddPostRequest.builder()
                .title("t").content("c").category("cate")
                .build();

        PostResponse postResponse = postService.addPost(request);

        //when
        postService.addBookMark(postResponse.getId(), user.getUid());
        boolean hasBookmark = postService.hasBookMark(postResponse.getId(), user.getUid());
        //then
        assertThat(hasBookmark).isTrue();

    }


    @DisplayName("북마크 하면 포스트 북마크 수가 1 증가하고, 한번 더 누르면 1 감소한다.(취소)")
    @TestFactory
    Collection<DynamicTest> bookmarkAndCancel() throws Exception {
        //given
        AddPostRequest request = AddPostRequest.builder()
                .title("t").content("c").category("cate")
                .build();

        PostResponse postResponse = postService.addPost(request);

        //when
        return List.of(
            DynamicTest.dynamicTest("북마크를 누르면 카운트가 1 증가한다.", ()->{
                //when
                postService.addBookMark(postResponse.getId(), user.getUid());
                //then
                Post post = postRepository.findById(postResponse.getId()).get();
                assertThat(post).isNotNull()
                        .extracting("bookMarkCount").isEqualTo(1);
            }),
            DynamicTest.dynamicTest("북마크 한 글에 다시 좋아요를 누르면, 1 감소한다.", ()->{
                //when
                postService.addBookMark(postResponse.getId(), user.getUid());

                //then
                Post post = postRepository.findById(postResponse.getId()).get();
                assertThat(post).isNotNull()
                        .extracting("bookMarkCount").isEqualTo(0);
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