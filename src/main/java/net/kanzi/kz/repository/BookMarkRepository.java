package net.kanzi.kz.repository;

import net.kanzi.kz.domain.BookMark;
import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookMarkRepository extends JpaRepository<BookMark, Long>  {


    @Query("select l from BookMark l JOIN Post p on l.post = p JOIN User u on l.user = :user " +
            "WHERE p = :post ")
    BookMark getBookMark(@Param("post") Post post, @Param("user") User user);

    @Query("select p from BookMark l JOIN Post p on l.post = p JOIN User u on l.user = :user ")
    List<Post> getUserBookMarksPosts(@Param("user") User user);

}

