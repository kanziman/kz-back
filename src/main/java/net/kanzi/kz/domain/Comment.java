package net.kanzi.kz.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private String commenter;


    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name="post_id", nullable = false)
    @JsonIgnore
    private Post post;


    @Builder
    public Comment(String commenter, String message, Long userId, Post post) {
        this.commenter = commenter;
        this.message = message;
        this.userId = userId;
        this.post = post;
    }

    public void change(String message){
        this.message = message;
    }


    public void setPost(Post post) {
        this.post = post;
    }
    public void setUserId(Long id) {
        this.userId = id;
    }
}