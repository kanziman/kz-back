package net.kanzi.kz.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
public class UpdateUserRequest {
    private String email;
    @NotBlank(message = "아이디값은 필수입니다.")
    private String uid;
    @NotBlank(message = "별명은 필수입니다.")
    private String nickName;
    @Builder
    private UpdateUserRequest(String email, String uid, String nickName) {
        this.email = email;
        this.uid = uid;
        this.nickName = nickName;
    }
}


