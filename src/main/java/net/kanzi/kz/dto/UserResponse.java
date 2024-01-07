package net.kanzi.kz.dto;

import lombok.Getter;
import lombok.ToString;
import net.kanzi.kz.domain.Article;
import net.kanzi.kz.domain.User;

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




    public UserResponse(User user) {
        this.uid = user.getUid();
        this.email = user.getEmail();
        this.name = user.getName();
        this.photoURL = "https://api.dicebear.com/7.x/thumbs/svg?seed=" + user.getUid();
        this.nickName = user.getNickname();
        this.roles = (List) user.getAuthorities();
    }
}
