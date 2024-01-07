package net.kanzi.kz.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.kanzi.kz.advice.DataWrapper;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.dto.AddUserRequest;
import net.kanzi.kz.dto.PostResponse;
import net.kanzi.kz.dto.UserResponse;
import net.kanzi.kz.service.PostService;
import net.kanzi.kz.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class UserApiController {

    private final UserService userService;
    private final PostService postService;

    @PostMapping("/api/users")
    @DataWrapper
    public UserResponse updateUser(@RequestBody AddUserRequest dto) {
        System.out.println("dto = " + dto);
        return userService.save(dto);
    }

    @GetMapping("/api/users")
    @DataWrapper
    public UserResponse getUser() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String email = authentication.getName();

        User user = userService.findByEmail(email);

        UserResponse userResponse = new UserResponse(user);

        return userResponse;
    }

    @GetMapping("/api/users/{uid}/bookMarks")
    public ResponseEntity<List<PostResponse>> getPostBookMark(@PathVariable String uid) {
        System.out.println("request = " + uid);
        List<PostResponse> userBookMarksPosts = postService.getUserBookMarksPosts(uid);
        return ResponseEntity.ok()
                .body(userBookMarksPosts);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

}
