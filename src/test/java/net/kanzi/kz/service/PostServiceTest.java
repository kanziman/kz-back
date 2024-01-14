package net.kanzi.kz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.Role;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.dto.post.AddPostRequest;
import net.kanzi.kz.dto.post.PostResponse;
import net.kanzi.kz.dto.post.PageResultDTO;
import net.kanzi.kz.dto.post.PostRequestDto;
import net.kanzi.kz.repository.PostRepository;
import net.kanzi.kz.repository.TagRepository;
import net.kanzi.kz.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class PostServiceTest {
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostService postService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    EntityManager em;

    User user;

    @BeforeEach
    void setSecurityContext()  {
        User user1 = createUser("uid1", "email1@com", Role.USER);
        user = userRepository.save(user1);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
    }
    @AfterEach
    void tearDown(){
        tagRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("로그인한 유저는 글을 작성할 수 있다.")
    public void createPost() {

        //given
        AddPostRequest request = AddPostRequest.builder()
                .title("t").content("c").category("cate")
                .tags(new String[]{"tag1", "tag2"})
                .build();

        //when
        PostResponse postResponse = postService.addPost(request);

        //then
        assertThat(postResponse.getId()).isNotNull();
        assertThat(postResponse)
                .extracting("uid")
                .isEqualTo(user.getUid());
        assertThat(postResponse)
                .extracting("title", "content", "category")
                .contains("t", "c", "cate");
    }

    private User createUser(String uid, String email, Role role) {
        return User.builder()
                .uid(uid)
                .email(email)
                .roleType(role)
                .build();
    }

    @DisplayName("글 목록 조회에 성공한다.")
    @Transactional
    @Test
    public void getPosts() throws Exception {

        //given
        String[] tags = new String[]{"tag1", "tag2"};
        AddPostRequest request = AddPostRequest.builder()
                .title("t").content("c").category("cate")
                .tags(tags)
                .build();
        postService.addPost(request);

        PostRequestDto postRequestDto = new PostRequestDto();

        // when
        PageImpl result = postRepository.searchQuery(postRequestDto);

        Function<Post, PostResponse> fn = (en -> PostResponse.of(en));
        PageResultDTO pageResultDTO = new PageResultDTO<>(result, fn);

        PostResponse postResponse = (PostResponse) pageResultDTO.getDtoList().get(0);
        assertThat(postResponse.getTags())
                .hasSize(2)
                .containsExactlyInAnyOrder("tag1", "tag2");
        assertThat(postResponse)
                .extracting("title", "content", "category")
                .contains("t", "c", "cate");
    }

    @DisplayName("페이지에 따른 목록 조회에 성공한다.")
    @Test
    public void getPostsPage() throws Exception {
        // given
        AddPostRequest request = AddPostRequest.builder()
                .title("t").content("c").category("cate")
                .build();
        for (int i = 0; i < 5; i++) {
            postService.addPost(request);
        }

        // when
        List<Post> postsTagsUsersPage = postRepository.findPostsTagsUsersPage(1, 5);

        // then
        assertThat(postsTagsUsersPage)
                .hasSize(4);
    }


    @DisplayName("한개 글을 조회할 수 있다.")
    @Test
    public void getPost() throws Exception {
        //given
        String[] tags = new String[]{"tag1", "tag2"};
        AddPostRequest request = AddPostRequest.builder()
                .title("t").content("c").category("cate")
                .tags(tags)
                .build();
        PostResponse postResponse = postService.addPost(request);

        // when
        PostResponse postOne = postService.findById(postResponse.getId());

        // then
        assertThat(postOne.getId()).isNotNull();
    }

    @DisplayName("작성자가 아닌 다른 사람은 글을 삭제할 수 없다.")
    @Test
    public void deletePost() throws Exception {
        // given
        AddPostRequest request = AddPostRequest.builder()
                .title("t").content("c").category("cate")
                .build();
        PostResponse postResponse = postService.addPost(request);

        User other = userRepository.save(createUser("uid2", "other@com", Role.USER));

        // when then
        assertThatThrownBy(()->postService.delete(postResponse.getId(), other.getEmail()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("not authorized");
    }

    @DisplayName("게시글 제목, 내용, 카테고리, 태그목록 수정에 성공한다.")
    @Test
    public void updatePost() throws Exception {
        // given
        String[] tags = new String[]{"tag1", "tag2"};
        AddPostRequest request = AddPostRequest.builder()
                .title("t").content("c").category("cate")
                .tags(tags)
                .build();
        PostResponse postResponse = postService.addPost(request);

        String[] newTags = new String[]{"tag3"};
        AddPostRequest newRequest = AddPostRequest.builder()
                .title("tt").content("cc").category("cate")
                .tags(newTags)
                .build();

        // when
        postService.update(postResponse.getId(), newRequest);

        // then
        PostResponse response = postService.findById(postResponse.getId());
        assertThat(response.getTags())
                .hasSize(1)
                .containsExactlyInAnyOrder("tag3");
        assertThat(response)
                .extracting("title", "content", "category")
                .contains("tt", "cc", "cate");

    }

}