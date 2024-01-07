package net.kanzi.kz.dto;

import lombok.*;
import net.kanzi.kz.domain.Comment;
import net.kanzi.kz.domain.Post;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class AddCommentRequest {

    private String message;
    private String commenter;
    private String uid;
    private Long postId;

    public Comment toEntity(String user, String uid) {
        return Comment.builder()
                .commenter(user)
                .uid(uid)
                .message(message)
                .build();
    }

}
