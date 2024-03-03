package net.kanzi.kz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional(readOnly = false)
    public void update(String uid, String nickName) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
        user.update(nickName);
    }

    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
    public User findByUid(String uid) {
        return userRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
    public User findByEmail(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        System.out.println("byEmail = " + byEmail);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("userdetail service laodUserByUsername" + username);
        return null;
    }
}
