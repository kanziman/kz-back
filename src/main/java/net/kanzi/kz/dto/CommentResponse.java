package net.kanzi.kz.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.kanzi.kz.domain.Comment;
import net.kanzi.kz.domain.User;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class CommentResponse {

    private Long id;

    private String message;
    private String commenter;
    private String photoURL;
    private String nickName;
    private LocalDateTime createdAt;

    public CommentResponse(Long id, String message, String commenter) {
        this.id = id;
        this.message = message;
        this.commenter = commenter;
    }

    public CommentResponse(Comment comment, User user) {
        this.id = comment.getId();
        this.commenter = comment.getUid();
        this.message = comment.getMessage();
        this.createdAt = comment.getCreatedAt();
        this.nickName = user == null? "" : user.getNickname();
        this.photoURL = user == null? "" : "https://api.dicebear.com/7.x/thumbs/svg?seed=" + comment.getUid();
    }

}
