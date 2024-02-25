package net.kanzi.kz.repository;

import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.Role;
import net.kanzi.kz.domain.Tag;
import net.kanzi.kz.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
class PostRepositoryTest {
    
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @DisplayName("User로 포스트를 조회할 수 있다.")
    @Test
    @Transactional
    public void findAllByUser() throws Exception {
        //given
        User user1 = createUser("uid1", "email1@com", Role.USER);
        userRepository.save(user1);
        User user2 = createUser("uid2", "email2@com", Role.USER);
        userRepository.save(user2);

        Post post1 = createPost("t1", "c1", "cate1", new String[]{}, user1);
        Post post2 = createPost("t2", "c2", "cate2", new String[]{}, user2);
        postRepository.saveAll(List.of(post1, post2));

        //when
        List<Post> posts = postRepository.findAllByUser(user1);
        //then
        Assertions.assertThat(posts).hasSize(1)
                .extracting("user")
                .containsExactly(
                        user1
                );
    }

    private User createUser(String uid, String email, Role role) {
        return User.builder()
                .uid(uid)
                .email(email)
                .roleType(role)
                .build();
    }


    private Post createPost(String title, String content, String category, String[] strings, User user){
        Set<Tag> tagSet = new HashSet<>();
        Arrays.stream(strings)
                .map(Tag::new)
                .forEach(t -> tagSet.add(t));

        return Post.builder()
                .uid("3a727380-bca1-4aa1-b65d-8f887f7202ee")
                .title(title)
                .content(content)
                .category(category)
                .tags(tagSet)
                .user(user)
                .build();
    }

}