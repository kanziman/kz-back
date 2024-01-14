package net.kanzi.kz.config;

import lombok.RequiredArgsConstructor;
import net.kanzi.kz.config.jwt.TokenProvider;
import net.kanzi.kz.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import net.kanzi.kz.config.oauth.OAuth2SuccessHandler;
import net.kanzi.kz.config.oauth.OAuth2UserCustomService;
import net.kanzi.kz.repository.RefreshTokenRepository;
import net.kanzi.kz.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    public final Environment env;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
//                .requestMatchers(toH2Console())
                .requestMatchers("/img/**", "/css/**", "/static/js/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable())
                .logout((logout) ->
                        logout.deleteCookies("remove")
                                .invalidateHttpSession(false)
                                .logoutSuccessUrl("/")
                ).addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

        ;

        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionConcurrency((sessionConcurrency) ->
                                sessionConcurrency.maximumSessions(1)
                                        .expiredUrl("/")
                        )
        );

        http.authorizeRequests()
                .requestMatchers("/api/posts/**").permitAll()
                .requestMatchers("/api/users/**").permitAll()
                .requestMatchers("/api/tags").permitAll()
                .requestMatchers("/api/proxy").permitAll()
                .requestMatchers("/api/token").permitAll()
                .requestMatchers("/api/ticker").permitAll()
                .requestMatchers("/api/tickers").permitAll()
                .requestMatchers("/api/market").permitAll()
                .requestMatchers("/api/health_check").permitAll()
                .requestMatchers("/api/**").authenticated()
                .requestMatchers("/login", "/signup", "/user").permitAll()
                .anyRequest().permitAll();

        http.oauth2Login(oauth2 -> oauth2
                .successHandler(oAuth2SuccessHandler())
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                        .userService(oAuth2UserCustomService)
                )
        );

//        http.oauth2Login()
//                .loginPage("/login")
//                .authorizationEndpoint()
//                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
//                .and()
//                .successHandler(oAuth2SuccessHandler())
//                .userInfoEndpoint()
//                .userService(oAuth2UserCustomService);

//        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.logout()
//                .logoutSuccessUrl("/login");
//        http.exceptionHandling()
//                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
//                        new AntPathRequestMatcher("/api/**"));


        return http.build();
    }


    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                userService
        );
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
