package net.kanzi.kz.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.kanzi.kz.advice.DataWrapper;
import net.kanzi.kz.config.jwt.TokenProvider;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.dto.UserResponse;
import net.kanzi.kz.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class UserApiController {

    private final UserService userService;

    @PostMapping("/api/user")
    @DataWrapper
    public UserResponse getUser(HttpServletRequest request) {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        UserResponse userResponse = new UserResponse(user);

        return userResponse;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

}
