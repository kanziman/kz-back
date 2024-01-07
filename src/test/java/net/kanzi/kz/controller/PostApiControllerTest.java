package net.kanzi.kz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.kanzi.kz.domain.Article;
import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.Tag;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.dto.*;
import net.kanzi.kz.dto.post.PostRequestDto;
import net.kanzi.kz.repository.BlogRepository;
import net.kanzi.kz.repository.PostRepository;
import net.kanzi.kz.repository.TagRepository;
import net.kanzi.kz.repository.UserRepository;
import net.kanzi.kz.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TagRepository tagRepository;

    User user;

    @BeforeEach
    public void mockMvcSetup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .uid(UUID.randomUUID().toString())
                .build());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
    }


    @Test
    public void findTagsWithCount() throws Exception {

        List<TagResponse> topTags = tagRepository.getTopTags(PageRequest.of(0, 10));
        topTags.forEach(o-> System.out.println("o.getName() = " + o.getName()));

    }
    @Test
    public void findAllWithTags(PostRequestDto request) throws Exception {

        List<Post> posts = postRepository.findAllWithTags(request);

        for (Post post : posts) {
            Set<Tag> tags = post.getTags(); // tags already fetched
        }

        List<PostResponse> collect = posts.stream()
                .map(Post::toDTO).collect(Collectors.toList());

        for (PostResponse response : collect) {
            System.out.println("response = " + response);
        }

    }

    public PostResponse toDTO(Post post){
        PostResponse build = PostResponse.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .uid(post.getUid())
                .category(post.getCategory())
                .tags(post.getTags().stream()
                        .map(Tag::getName)
                        .collect(Collectors.toList()))
                .build();
        return build;
    }

    @DisplayName("addArticle: post and tag 조회에 성공한다.")
    @Test
    public void getPostWithTag() throws Exception {

        // given
        final String url = "/api/posts";
        final String title = "title";
        final String content = "content";
        final String category = "notice";
        final String[] tags = {"태그1","태그2"};
        AddPostRequest request = new AddPostRequest(title, content, category, tags);

        Post post = request.toEntity("userName");
        if ( request.getTags() != null ){
            post.addTags(request);
        }
        Post save = postRepository.save(post);
        System.out.println("save = " + save);

        Post selcted = postRepository.getPostWithTags(save.getId());
        System.out.println("selcted = " + selcted);

    }

    @DisplayName("addArticle: post and tag 추가에 성공한다.")
    @Test
    @Transactional
    public void getPost() throws Exception {

        // given
        final String url = "/api/posts";
        final String title = "title";
        final String content = "content";
        final String category = "notice";
        final String[] tags = {"태그1","태그2"};
        final AddPostRequest userRequest = new AddPostRequest(title, content, category, tags);

        final String requestBody = objectMapper.writeValueAsString(userRequest);
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));
        System.out.println("result = " + result);
        // then
        result.andExpect(status().isCreated());
    }
    @DisplayName("addArticle: post 추가에 성공한다.")
    @Test
    public void addPost() throws Exception {

        // given
        final String url = "/api/posts";
        final String title = "title";
        final String content = "content";
        final String category = "notice";
        final String[] tags = null;
        final AddPostRequest userRequest = new AddPostRequest(title, content, category, tags);

        final String requestBody = objectMapper.writeValueAsString(userRequest);
        System.out.println("requestBody = " + requestBody);
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        List<Post> posts = postRepository.findAll();

        assertThat(posts.get(0).getTitle()).isEqualTo(title);
        assertThat(posts.get(0).getContent()).isEqualTo(content);
    }


    @Test
    @Transactional
    public void testDeleteTag(){
        Post post = postRepository.getPostWithTags(44L);
        System.out.println("post = " + post);
        tagRepository.deleteTagsByPost(post);
    }

}