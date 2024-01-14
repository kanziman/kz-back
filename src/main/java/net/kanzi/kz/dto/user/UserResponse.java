package net.kanzi.kz.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import net.kanzi.kz.domain.Article;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.util.Constants;

import java.util.List;

@Getter
@ToString
public class UserResponse {

    private final String uid;
    private final String name;
    private final String email;
    private final String nickName;
    private final String photoURL;
    private final List roles;
    private final String role;

    @Builder
    public UserResponse(User user) {
        this.uid = user.getUid();
        this.email = user.getEmail();
        this.name = user.getName();
        this.photoURL = Constants.PHOTO_URL + user.getUid();
        this.nickName = user.getNickname();
        this.role = user.getRole().getValue();
        this.roles = (List) user.getAuthorities();
    }
}
