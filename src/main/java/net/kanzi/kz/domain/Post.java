package net.kanzi.kz.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import net.kanzi.kz.dto.AddPostRequest;
import net.kanzi.kz.dto.PostResponse;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EntityListeners(AuditingEntityListener.class)
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
@Entity
@Table
@ToString(exclude = "tags")
@DynamicInsert
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "uid", nullable = false)
    private String uid;

    @Column(name = "category", nullable = false)
    private String category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Tag> tags;

    @Column(name = "read_count")
    private Integer readCount;
    @Column(name = "like_count")
    private Integer likeCount;
    @Column(name = "book_mark_count")
    private Integer bookMarkCount;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Post(String title, String content, String uid, String category, Set<Tag> tags, Integer readCount, Integer likeCount, Integer bookMarkCount) {
        this.title = title;
        this.content = content;
        this.uid = uid;
        this.category = category;
        this.tags = tags;
        this.readCount = readCount;
        this.likeCount = likeCount;
        this.bookMarkCount = bookMarkCount;
    }

    @Builder
    public Post(String uid, String title, String content, String category) {
        this.uid = uid;
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setTags(Set<Tag> tagEntities) {
        this.tags = tagEntities;
    }

    public void upReadCount() {
        this.readCount = this.readCount+1;
    }
    public void updateLikeCount(int upOrDown) {
        this.likeCount = this.likeCount + upOrDown;
    }
    public void updateBookMarkCount(int upOrDown) {
        this.bookMarkCount = this.bookMarkCount + upOrDown;
    }
    //==연간관계 매서드==//
    public void update(AddPostRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.category = request.getCategory();
        addTags(request);
    }
    public void addTags(AddPostRequest request) {
        if (request.getTags() != null ){
            String[] tags = request.getTags();
            for (String tagName : tags) {
                addTag(new Tag(tagName));
            }
        }
    }
    private void addTag(Tag tag) {
        if (this.tags == null) {
            this.tags = new HashSet<>();
        }
        this.tags.add(tag);
        tag.setPost(this);
    }

    // convert

    public static PostResponse toDTO(Post post){
        PostResponse postResponse = new PostResponse(post);
        return postResponse;
    }

}
