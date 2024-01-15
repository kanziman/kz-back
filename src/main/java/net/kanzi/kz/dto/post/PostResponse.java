package net.kanzi.kz.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.Tag;
import net.kanzi.kz.util.Constants;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString(exclude = "tags")
@Setter
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private String category;
    private String uid;
    private List<String> tags;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private int readCount;
    private int likeCount;
    private int bookmarkCount;
    // user
    private String photoURL;
    private String nickName;
    // comment count
    private long commentCount;

    @Builder
    public PostResponse(Long id, String title, String content, String category, String uid, List<String> tags, LocalDateTime createdAt, int readCount, int likeCount, int bookmarkCount, String photoURL, String nickName, long commentCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.uid = uid;
        this.tags = tags;
        this.createdAt = createdAt;
        this.readCount = readCount;
        this.likeCount = likeCount;
        this.bookmarkCount = bookmarkCount;
        this.photoURL = photoURL;
        this.nickName = nickName;
        this.commentCount = commentCount;
    }

    public static PostResponse of(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .uid(post.getUid())
                .tags(post.getTags().stream()
                                .map(Tag::getName)
                                .collect(Collectors.toList())
                )
                .readCount(post.getReadCount())
                .likeCount(post.getLikeCount())
                .bookmarkCount(post.getBookMarkCount())
                .nickName(post.getUser().getNickname())
                .photoURL(Constants.PHOTO_URL + post.getUser().getUid())
                .commentCount(post.getComments().size())
                .build();
    }
}
