package net.kanzi.kz.apicontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.dto.user.UpdateUserRequest;
import net.kanzi.kz.dto.user.UserResponse;
import net.kanzi.kz.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @GetMapping("/health_check")
    public ResponseEntity<Void> check() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    public ResponseEntity<Void> status() {
        return ResponseEntity.ok().build();
    }


    @PutMapping("/api/users")
    public ApiResponse<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest dto) {
        userService.update(dto.getUid(), dto.getNickName());
        User user = userService.findByUid(dto.getUid());
        UserResponse userResponse = UserResponse.of(user);

        return ApiResponse.of(HttpStatus.OK, userResponse);

    }

    @GetMapping("/api/users/{uid}")
    public ApiResponse<UserResponse> getUser(@PathVariable String uid) {
        User user = userService.findByUid(uid);
        UserResponse userResponse = UserResponse.of(user);
        return ApiResponse.of(HttpStatus.OK, userResponse);
    }






}
