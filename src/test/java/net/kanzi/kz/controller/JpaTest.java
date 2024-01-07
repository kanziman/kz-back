package net.kanzi.kz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.dto.AddPostRequest;
import net.kanzi.kz.dto.CommentResponse;
import net.kanzi.kz.dto.PostResponse;
import net.kanzi.kz.repository.CommentRepository;
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
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class JpaTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;
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
        userRepository.deleteByEmail("user@gmail.com");
        user = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .uid(UUID.randomUUID().toString())
                .build());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
    }

    @Test
    public void testSearch1(){
        postRepository.search1();
    }

    @Test
    public void testSearch2(){
        Pageable pageable =
                PageRequest.of(0,5,
                        Sort.by("id").descending()
                                .and(Sort.by("title").ascending()));

        postRepository.searchPage("t", new String[]{}, "more", pageable);
    }

    @Test
    @Transactional
    public void testUserBookMarks(){
        List<PostResponse> userBookMarksPosts = postService.getUserBookMarksPosts("cf308d78-fc19-4378-aff0-3406e48133da");

        for (PostResponse userBookMarksPost : userBookMarksPosts) {
            System.out.println("userBookMarksPost = " + userBookMarksPost);
        }

    }

    @Test
    @Transactional
    public void testUserComments(){
        Post post = postRepository.findById(13L)
                .orElseThrow(() -> new IllegalArgumentException("not found : "));

        List<CommentResponse> commentsWithCommenter = commentRepository.getCommentsWithCommenter(post);

        for (CommentResponse res : commentsWithCommenter) {
            System.out.println("commentsWithCommenter = " + res);
        }

    }
}
