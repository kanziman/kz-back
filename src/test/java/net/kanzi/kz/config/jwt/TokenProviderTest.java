package net.kanzi.kz.config.jwt;

import io.jsonwebtoken.Jwts;
import net.kanzi.kz.domain.Role;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;


    @DisplayName("유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    @Test
    void generateTokenWith() {
        // given
        User testUser = userRepository.save(User.builder()
                .uid(UUID.randomUUID().toString())
                .email("user@gmail.com")
                .password("test")
                .roleType(Role.ADMIN)
                .build());

        // when
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        // then
        String userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
//                .get("uid", String.class);
        String role = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
        assertThat(userId).isEqualTo(testUser.getUid());
    }

    @DisplayName("만료된 토큰인 경우에 유효성 검증에 실패한다.")
    @Test
    void validToken_invalidToken() {
        // given
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        // when
        boolean result = tokenProvider.validToken(token);

        // then
        assertThat(result).isFalse();
    }


    @DisplayName("유효한 토큰인 경우에 유효성 검증에 성공한다.")
    @Test
    void validToken_validToken() {
        // given
        String token = JwtFactory.withDefaultValues()
                .createToken(jwtProperties);

        // when
        boolean result = tokenProvider.validToken(token);

        // then
        assertThat(result).isTrue();
    }


    @DisplayName("토큰 기반으로 인증정보를 가져올 수 있다.")
    @Test
    void getAuthentication() {
        // given
        String userEmail = "user@email.com";
        String token = JwtFactory.builder()
                .claims(Map.of("uid", "uuid"))
                .claims(Map.of("email", userEmail))
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        // when
        Authentication authentication = tokenProvider.getAuthentication(token);
        System.out.println("authentication = " + authentication);
        System.out.println(((UserDetails) authentication.getPrincipal()).getUsername());

        // then
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    @DisplayName("토큰으로 유저 ID를 가져올 수 있다.")
    @Test
    void getUserId() {
        // given
        String userId = "1";
        String token = JwtFactory.builder()
                .claims(Map.of("uid", userId))
                .build()
                .createToken(jwtProperties);

        // when
        String userIdByToken = tokenProvider.getUserId(token);

        // then
        assertThat(userIdByToken).isEqualTo(userId);
    }
}
