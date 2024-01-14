package net.kanzi.kz.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import net.kanzi.kz.dto.post.AddPostRequest;
import org.hibernate.annotations.DynamicInsert;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table
@ToString(exclude = {"tags", "comments"})
@DynamicInsert
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "uid")
    private String uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "category", nullable = false)
    private String category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();


    @Column(name = "read_count")
    private int readCount;
    @Column(name = "like_count")
    private int likeCount;
    @Column(name = "book_mark_count")
    private int bookMarkCount;

//    @CreatedDate
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//    @LastModifiedDate
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;

    @Builder
    private Post(String title, String content, String uid, String category, Set<Tag> tags, User user) {
        this.title = title;
        this.content = content;
        this.uid = uid;
        this.category = category;
        this.tags = tags;
        this.user = user;
    }

    public static Post create(Post post, User user) {
        return Post.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .tags(post.getTags())
                .user(user)
                .uid(user.getUid())
                .build();
    }


    public void upReadCount() {
        this.readCount = this.readCount+1;
    }
    public void updateLikeCount(int upOrDown) {
        if (Math.abs(upOrDown) != 1) {
            throw new IllegalArgumentException("좋아요는 1씩 증가/감소만 가능합니다.");
        }
        this.likeCount = this.likeCount + upOrDown;
    }
    public void updateBookMarkCount(int upOrDown) {
        if (Math.abs(upOrDown) != 1) {
            throw new IllegalArgumentException("북마크는 1씩 증가/감소만 가능합니다.");
        }
        this.bookMarkCount = this.bookMarkCount + upOrDown;
    }

    //==연간관계 매서드==//
    public void update(AddPostRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.category = request.getCategory();

        if (request.getTags() != null ){
            Arrays.stream(request.getTags())
                    .map(Tag::new)
                    .forEach(t -> this.addTag(t));
        }
//        addTags(request);
    }

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.setPost(this);
    }

    public void addUser(User user){
        this.user = user;
        this.uid = user.getUid();
    }


}
