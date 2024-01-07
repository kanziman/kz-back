package net.kanzi.kz.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@ToString
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "uid", nullable = false, unique = true)
    private String uid;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "providerType")
    private String providerType;

    @Builder
    public User(String email, String name, String password, String nickname, String uid, String providerType) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.nickname = nickname;
        this.uid = uid;
        this.providerType =providerType;
    }

    public User update(String nickname) {
        this.nickname = nickname;
        return this;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if ("hwanghkt@naver.com".equals(this.getUsername())) {
            return List.of(new SimpleGrantedAuthority("user"),new SimpleGrantedAuthority("admin"));
        }
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
