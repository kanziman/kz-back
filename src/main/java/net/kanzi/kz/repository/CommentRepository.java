package net.kanzi.kz.repository;

import net.kanzi.kz.domain.Comment;
import net.kanzi.kz.domain.Post;
import net.kanzi.kz.dto.comment.CommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>  {

    @Query("select c from  Comment c WHERE c.post = :post")
    List<Comment> getComments(@Param("post") Post post);


    @Query("SELECT NEW net.kanzi.kz.dto.comment.CommentResponse(c, u) FROM Comment c LEFT JOIN User u on c.commenter = u.uid WHERE c.post = :post order by c.createdAt desc ")
    List<CommentResponse> getCommentsWithCommenter(@Param("post") Post post);
}

