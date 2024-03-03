package net.kanzi.kz.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.util.Constants;

import java.util.List;

@Getter
@Setter
@ToString
public class UserResponse {

    private String uid;
    private String name;
    private String email;
    private String nickName;
    private String photoURL;
    private List roles;
    private String role;

    @Builder
    public UserResponse(String uid, String name, String email, String nickName, String photoURL, List roles, String role) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.nickName = nickName;
        this.photoURL = photoURL;
        this.roles = roles;
        this.role = role;
    }

    public static UserResponse of(User user){
        return UserResponse.builder()
                .uid(user.getUid())
                .name(user.getName())
                .email(user.getEmail())
                .nickName(user.getNickname())
                .photoURL(Constants.PHOTO_URL + user.getUid())
                .role(user.getRole().getValue())
                .roles((List) user.getAuthorities(user.getRole().getValue())).build();
    }
}
