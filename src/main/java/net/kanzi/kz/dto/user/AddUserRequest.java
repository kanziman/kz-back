package net.kanzi.kz.dto.user;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class AddUserRequest {
    private String email;
    private String password;
    private String nickName;
}


