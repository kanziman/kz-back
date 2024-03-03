package net.kanzi.kz.dto.comment;

import lombok.Getter;

@Getter
public class UpdateCommentRequest {
    Long commentId;
    String message;
    String commenter;

    public UpdateCommentRequest(Long commentId, String message, String commenter) {
        this.commentId = commentId;
        this.message = message;
        this.commenter = commenter;
    }
}
