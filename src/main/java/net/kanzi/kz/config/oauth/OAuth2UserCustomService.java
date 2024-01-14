package net.kanzi.kz.config.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kanzi.kz.domain.Role;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.oauth.OAuth2Dto;
import net.kanzi.kz.oauth.ProviderType;
import net.kanzi.kz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest); // ❶ 요청을 바탕으로 유저 정보를 담은 객체 반환
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        User savedUser = saveOrUpdate(user,registrationId);
//        saveOrUpdate(user);

        return user;
    }

    // ❷ 유저가 있으면 업데이트, 없으면 유저 생성
    private User saveOrUpdate(OAuth2User oAuth2User, String registrationId) {
        log.info("save or update :" + oAuth2User.getName());
        ProviderType providerType = ProviderType.valueOf(registrationId.toUpperCase());

        Map<String, Object> attributes = oAuth2User.getAttributes();
        attributes.forEach(
                (k,v) -> System.out.println(k + " = " + v)
        );

        OAuth2Dto oAuth2Dto = OAuth2Dto.of(providerType, oAuth2User.getAttributes());
//           throw new OAuth2AuthenticationException("유효하지 않습니다. 다른 방식으로 로그인 해주세요.");
        String email = oAuth2Dto.getEmail();
        String name = oAuth2Dto.getName();

        User user = userRepository.findByEmail(email).orElse(null);

        // New user
        if (user == null) {
            user = User.builder()
                    .email(email)
                    .name(name)
                    .roleType(Role.USER)
                    .password(new BCryptPasswordEncoder().encode("0000"))
                    .nickname(genNickName())
                    .uid(UUID.randomUUID().toString())
                    .providerType(providerType.name())
                    .build();

        }
        return userRepository.save(user);
    }

    private String genNickName() {
        String name = jdbcTemplate.queryForObject(
                    "SELECT name FROM Animal ORDER BY RAND() LIMIT 1",
                        String.class);

        // 0부터 9999 사이의 숫자 (10,000개, 4자리)
        int number = new Random().nextInt(10000);
        String fourDigitNum = String.format("%04d", number);

        String finalName = name + fourDigitNum;
        return finalName;
    }

}

