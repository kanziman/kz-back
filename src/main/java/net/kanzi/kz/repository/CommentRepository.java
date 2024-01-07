package net.kanzi.kz.repository;

import net.kanzi.kz.domain.Comment;
import net.kanzi.kz.domain.Post;
import net.kanzi.kz.dto.CommentResponse;
import net.kanzi.kz.dto.TagResponse;
import net.kanzi.kz.dto.post.PostRequestDto;
import net.kanzi.kz.repository.search.SearchBoardRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>  {

    @Query("select c from  Comment c WHERE c.post = :post")
    List<Comment> getComments(@Param("post") Post post);


    @Query("SELECT NEW net.kanzi.kz.dto.CommentResponse(c, u) FROM Comment c LEFT JOIN User u on c.uid = u.uid WHERE c.post = :post")
    List<CommentResponse> getCommentsWithCommenter(@Param("post") Post post);
}

