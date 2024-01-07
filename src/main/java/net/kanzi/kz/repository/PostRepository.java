package net.kanzi.kz.repository;

import net.kanzi.kz.domain.Post;
import net.kanzi.kz.dto.post.PostRequestDto;
import net.kanzi.kz.repository.search.SearchBoardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> , SearchBoardRepository {

    @Query("select p,t from Post p LEFT JOIN Tag t on t.post = p WHERE p.id = :postId")
    Post getPostWithTags(@Param("postId") Long postId);

    @Query("select p,t from Post p LEFT JOIN Tag t on t.post = p")
    List<Object[]> getAll();

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.tags")
    List<Post> findAllWithTags(PostRequestDto params);
}

