package net.kanzi.kz.dto;

import lombok.Getter;
import lombok.ToString;
import net.kanzi.kz.domain.Article;
import net.kanzi.kz.domain.User;

import java.util.List;

@Getter
@ToString
public class UserResponse {

    private final String uuid;
    private final String email;
    private final List roles;




    public UserResponse(User user) {
        this.uuid = user.getUuid();
        this.email = user.getEmail();
        this.roles = (List) user.getAuthorities();
    }
}
