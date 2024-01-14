package net.kanzi.kz.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@ToString(exclude = {"post"})
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name="post_id")
    @JsonIgnore
    private Post post;

    public Tag(String tagName) {
        this.name = tagName;
    }

    public Tag(Post post, String tag) {
        this.post = post;
        this.name = tag;
    }

    public void setPost(Post post) {
        this.post = post;
        post.getTags().add(this);
    }
}