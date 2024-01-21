package net.kanzi.kz.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.util.Constants;

import java.util.List;

@Getter
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
    public UserResponse(User user) {
        this.uid = user.getUid();
        this.email = user.getEmail();
        this.name = user.getName();
        this.photoURL = Constants.PHOTO_URL + user.getUid();
        this.nickName = user.getNickname();
        this.role = user.getRole().getValue();
        this.roles = (List) user.getAuthorities(role);
    }
}
