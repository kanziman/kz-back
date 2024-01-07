package net.kanzi.kz.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.Tag;
import net.kanzi.kz.domain.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString(exclude = "tags")
@Setter
public class PostResponse {

    private Long id;
    private final String title;
    private final String content;
    private String category;
    private String uid;
    private List<String> tags;
    private LocalDateTime createdAt;
    private int readCount;
    private int likeCount;
    private int bookmarkCount;
    // user
    private String photoURL;
    private String nickName;
    // post count
    private boolean isUserBookMark;
    private boolean isUserLike;
    // comment count
    private long commentCount;
    @Builder
    public PostResponse(Long id, String title, String content, String category,String uid, List<String> tags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.uid = uid;
        this.tags = tags;
    }

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.uid = post.getUid();
        this.tags = post.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
        this.createdAt = post.getCreatedAt();
        this.readCount = post.getReadCount();
        this.likeCount = post.getLikeCount();
        this.bookmarkCount = post.getBookMarkCount();
    }

    public PostResponse(Post post, User member, long commentCount) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.uid = post.getUid();
        this.tags = post.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
        this.createdAt = post.getCreatedAt();
        this.readCount = post.getReadCount();
        this.likeCount = post.getLikeCount();
        this.bookmarkCount = post.getBookMarkCount();
        this.commentCount = commentCount;


        this.nickName = member == null? "" : member.getNickname();
        this.photoURL = member == null? "" : "https://api.dicebear.com/7.x/thumbs/svg?seed=" + member.getUid();
    }


//    public PostResponse(Post post) {
//        this.title = post.getTitle();
//        this.content = post.getContent();
//    }
}
