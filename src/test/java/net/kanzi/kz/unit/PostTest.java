package net.kanzi.kz.unit;

import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.Role;
import net.kanzi.kz.domain.Tag;
import net.kanzi.kz.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    @DisplayName("제목, 내용, 카테고리 태그로 포스트를 생성할 수 있다.")
    void postTest() {
        Post post = createPost("t", "c", "cate", new String[]{});

        assertThat(post.getTitle()).isNotNull();
        assertThat(post)
                .extracting("title", "content", "category", "tags")
                .contains("t", "c", "cate", new HashSet<>());
    }

    @Test
    @DisplayName("포스트에 tags 를 추가 할 수 있다.")
    void postAddTagsTest() {
        String[] strings = {"tag1","tag2"};
        Post post = createPost("t", "c", "cate", strings);

        assertThat(post.getTags())
                .hasSize(2)
                .extracting(tag -> tag.getName())
                .containsExactlyInAnyOrder("tag1","tag2");

    }

    @Test
    @DisplayName("포스트에 user 를 추가 할 수 있다.")
    void postAddUserTest() {
        Post post = createPost("t", "c", "cate", new String[]{});
        User user = User.builder()
                .email("user@gmail.com")
                .password("test")
                .uid(UUID.randomUUID().toString())
                .roleType(Role.USER)
                .build();

        post.addUser(user);
        // Assert that the post has the correct user
        assertThat(post.getUser())
                .isNotNull()
                .extracting(User::getEmail)
                .isEqualTo("user@gmail.com");
    }



    private Post createPost(String title, String content, String category, String[] strings){
        Set<Tag> tagSet = new HashSet<>();

        Arrays.stream(strings)
                .map(Tag::new)
                .forEach(t -> tagSet.add(t));

        return Post.builder()
                .title(title)
                .content(content)
                .category(category)
                .tags(tagSet)
                .build();
    }


}