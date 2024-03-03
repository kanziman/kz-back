package net.kanzi.kz.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import net.kanzi.kz.domain.Comment;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.util.Constants;

import java.time.LocalDateTime;

@Data
public class CommentResponse {

    private Long id;

    private String message;
    private String commenter;
    private String photoURL;
    private String nickName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Builder
    private CommentResponse(Long id, String message, String commenter, String photoURL, String nickName, LocalDateTime createdAt) {
        this.id = id;
        this.message = message;
        this.commenter = commenter;
        this.photoURL = photoURL;
        this.nickName = nickName;
        this.createdAt = createdAt;
    }

    @Builder
    private CommentResponse(Comment comment, User user) {
        this.id = comment.getId();

        this.commenter = comment.getCommenter();
        this.message = comment.getMessage();
        this.createdAt = comment.getCreatedAt();
        this.nickName = user == null? "" : user.getNickname();
        this.photoURL = user == null? "" : Constants.PHOTO_URL + comment.getCommenter();
    }


}
