package net.kanzi.kz.service;

import lombok.RequiredArgsConstructor;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.dto.AddUserRequest;
import net.kanzi.kz.dto.UserResponse;
import net.kanzi.kz.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserResponse save(AddUserRequest dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
        user.update(dto.getNickName());
        return new UserResponse(user);
    }

    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        System.out.println("byEmail = " + byEmail);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
