package net.kanzi.kz.dto.comment;

import lombok.*;
import net.kanzi.kz.domain.Comment;
import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.User;

@NoArgsConstructor
@ToString
@Data
public class AddCommentRequest {

    private String message;
    private String commenter;

    @Builder
    private AddCommentRequest(String message, String commenter) {
        this.message = message;
        this.commenter = commenter;
    }
    public Comment toEntity() {
        return Comment.builder()
                .commenter(commenter)
                .message(message)
                .build();
    }

}
