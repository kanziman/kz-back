package net.kanzi.kz.dto;

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


