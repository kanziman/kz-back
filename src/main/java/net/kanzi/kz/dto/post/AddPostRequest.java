package net.kanzi.kz.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.Tag;

import java.util.*;

@NoArgsConstructor
@Getter
@ToString
public class AddPostRequest {
//    private String uid;
    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @NotBlank(message = "카테고리는 필수입니다.")
    private String category;

    private String[] tags;


    @Builder
    private AddPostRequest(String title, String content, String category, String[] tags) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.tags = tags;
    }

    public Post toEntity() {
        Set<Tag> tagSet = new HashSet<>();
        Optional.ofNullable(tags)
                .ifPresent(tags -> Arrays.stream(tags)
                            .map(Tag::new)
                            .forEach(tagSet::add));
        return Post.builder()
                .title(title)
                .content(content)
                .category(category)
                .tags(tagSet)
                .build();
    }


}
