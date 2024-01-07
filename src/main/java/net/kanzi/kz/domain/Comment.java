package net.kanzi.kz.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private String commenter;
    private String uid;

    @ManyToOne
    @JoinColumn(name="post_id")
    @JsonIgnore
    private Post post;




    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @Builder
    public Comment(String commenter, String message, String uid) {
        this.commenter = commenter;
        this.message = message;
        this.uid = uid;
    }

    public Comment(Post post, String message) {
        this.post = post;
        this.message = message;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}