package net.kanzi.kz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.dto.post.PostResponse;
import net.kanzi.kz.repository.BookMarkRepository;
import net.kanzi.kz.repository.LikeRepository;
import net.kanzi.kz.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final BookMarkRepository bookMarkRepository;

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

    public List<PostResponse> getUserBookMarks(String uid) {
        List<PostResponse> responses = new ArrayList<>();
        try {
            User user = userRepository.findByUid(uid)
                    .orElseThrow(() -> new UsernameNotFoundException("not found user : " + uid));
            //user bookmark (북마크 조회기 떄문에 북마크는 전부 true )
            List<Post> bookMarksPosts = bookMarkRepository.getUserBookMarksPosts(user);
            responses = bookMarksPosts.stream()
                    .map(b -> PostResponse.of(b))
                    .collect(Collectors.toList());
        } catch (UsernameNotFoundException e){
            log.info(e.getMessage());
        }

        return responses;
    }
    public List<PostResponse> getUserLikes(String uid) {
        List<PostResponse> responses = new ArrayList<>();
        try {
            User user = userRepository.findByUid(uid)
                    .orElseThrow(() -> new UsernameNotFoundException("not found user : " + uid));

            //user like check
            List<Post> userLikesPosts = likeRepository.getUserLikesPosts(user);
            responses = userLikesPosts.stream()
                    .map(p -> PostResponse.of(p))
                    .collect(Collectors.toList());
        } catch (UsernameNotFoundException e){
            log.info(e.getMessage());
        }
        return responses;
    }
}
