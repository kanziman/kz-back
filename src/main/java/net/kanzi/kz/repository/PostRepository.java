package net.kanzi.kz.repository;

import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.User;
import net.kanzi.kz.repository.search.SearchPostRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> , SearchPostRepository {

    @Query("select p,t from Post p LEFT JOIN Tag t on t.post = p WHERE p.id = :postId")
    Post getPostWithTags(@Param("postId") Long postId);

    List<Post> findAllByUser(User user);
}

