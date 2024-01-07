package net.kanzi.kz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.kanzi.kz.domain.Post;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class AddPostRequest {
//    private String uid;
    private String title;
    private String content;
    private String category;
    private String[] tags;

    public Post toEntity(String author) {
        return Post.builder()
                .title(title)
                .content(content)
                .uid(author)
                .category(category)
                .build();
    }

}
