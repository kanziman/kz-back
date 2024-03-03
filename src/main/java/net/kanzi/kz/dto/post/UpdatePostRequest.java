package net.kanzi.kz.dto.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.Tag;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@NoArgsConstructor
@Getter
public class UpdatePostRequest {
    private Long id;
    @NotBlank(message = "글쓴이는 필수입니다.")
    private String writer;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @NotBlank(message = "카테고리는 필수입니다.")
    private String category;

    private String[] tags;


    @Builder
    private UpdatePostRequest(Long id, String writer, String title, String content, String category, String[] tags) {
        this.id = id;
        this.writer = writer;
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
                .id(id)
                .title(title)
                .content(content)
                .category(category)
                .tags(tagSet)
                .build();
    }


}
