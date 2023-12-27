package net.kanzi.kz.oauth;


import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Log4j2
@Getter
@Setter
@ToString
@NoArgsConstructor
public class OAuth2Dto implements OAuth2User {

    private Map<String, Object> attributes;
    private String name;
    private String email;
    private String providerType;

    @Builder
    public OAuth2Dto(String name, String email, Map<String, Object> attributes) {
        this.name = name;
        this.email = email;
        this.attributes = attributes;
    }

    public static OAuth2Dto of(ProviderType providerType, Map<String, Object> attributes) {
        if(ProviderType.NAVER.equals(providerType)) {
            return ofNaver(attributes);
        }
        if(ProviderType.KAKAO.equals(providerType)) {
            return ofKakao(attributes);
        }
        return ofGoogle(attributes);
    }

    private static OAuth2Dto ofGoogle(Map<String, Object> attributes) {

        return OAuth2Dto.builder()
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .attributes(attributes)
                .build();
    }

    private static OAuth2Dto ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Dto.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .attributes(response)
                .build();
    }
    private static OAuth2Dto ofKakao(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object>  profile = (Map<String, Object>) response.get("profile");

        return OAuth2Dto.builder()
                .name((String) profile.get("nickname"))
                .email((String) response.get("email"))
                .attributes(response)
                .build();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }
}
