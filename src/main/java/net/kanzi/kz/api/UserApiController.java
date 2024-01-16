package net.kanzi.kz.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.dto.post.PostResponse;
import net.kanzi.kz.dto.user.UpdateUserRequest;
import net.kanzi.kz.dto.user.UserResponse;
import net.kanzi.kz.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class UserApiController {

    private final UserService userService;

    @GetMapping("/health_check")
    public ResponseEntity<Void> check() {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/api/users")
    public ApiResponse<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest dto) {
        userService.update(dto.getUid(), dto.getNickName());
        UserResponse userResponse = new UserResponse(userService.findByUid(dto.getUid()));

        return ApiResponse.of(HttpStatus.OK, userResponse);

    }

    @GetMapping("/api/users/{uid}")
    public ApiResponse<UserResponse> getUser(@PathVariable String uid) {
        User user = userService.findByUid(uid);
        UserResponse userResponse = new UserResponse(user);
        return ApiResponse.of(HttpStatus.OK, userResponse);
    }

    @GetMapping("/api/users/{uid}/bookMarks")
    public ApiResponse<List<PostResponse>> getUserBookMark(@PathVariable String uid) {
        List<PostResponse> userBookMarksPosts = userService.getUserBookMarks(uid);

        return ApiResponse.of(HttpStatus.OK, userBookMarksPosts);
    }
    @GetMapping("/api/users/{uid}/likes")
    public ApiResponse<List<PostResponse>> getUserLikes(@PathVariable String uid) {
        List<PostResponse> userLikes = userService.getUserLikes(uid);
        return ApiResponse.of(HttpStatus.OK, userLikes);
    }




}
