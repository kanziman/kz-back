package net.kanzi.kz.repository;

import net.kanzi.kz.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long>  {


    @Query("select l from Likes l JOIN Post p on l.post = p JOIN User u on l.user = :user " +
            "WHERE p = :post ")
    Likes getLike(@Param("post") Post post, @Param("user") User user);

    @Query("select p from Likes l JOIN Post p on l.post = p JOIN User u on l.user = :user ")
    List<Post> getUserLikesPosts(@Param("user") User user);

}

