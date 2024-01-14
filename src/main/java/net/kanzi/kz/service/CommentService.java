package net.kanzi.kz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.kanzi.kz.domain.Comment;
import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.dto.comment.AddCommentRequest;
import net.kanzi.kz.dto.comment.CommentResponse;
import net.kanzi.kz.repository.CommentRepository;
import net.kanzi.kz.repository.PostRepository;
import net.kanzi.kz.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional(readOnly = true)
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    /**
     * COMMENTS LIST
     */

    public List<CommentResponse> findCommentByPostId(long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + postId));
        List<CommentResponse> commentsWithCommenter = commentRepository.getCommentsWithCommenter(post);

        return commentsWithCommenter;
    }

    /**
     * SAVE COMMENTS
     */
    public Long addComment(AddCommentRequest request, Long postId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + email));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + postId));

        Comment entity = request.toEntity();
        entity.setPost(post);
        entity.setUserId(user.getId());

        Comment save = commentRepository.save(entity);
        return save.getId();
    }

    /**
     * UPDATE COMMENTS
     */
    @Transactional(readOnly = false)
    public Long updateComment(AddCommentRequest request, Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + commentId));

        authorizeCommentAuthor(comment);

        comment.change(request.getMessage());

        return comment.getId();
    }

    /**
     * DELETE COMMENT
     */
    @Transactional(readOnly = false)
    public void deleteComment(long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + commentId));

        authorizeCommentAuthor(comment);
        commentRepository.delete(comment);
    }

    private void authorizeCommentAuthor(Comment comment) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("not found user : " + email));

        if (!comment.getCommenter().equals(user.getUid())) {
            throw new IllegalArgumentException("not authorized");
        }
    }
}
