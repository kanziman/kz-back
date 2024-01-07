package net.kanzi.kz.repository;

import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.Tag;
import net.kanzi.kz.dto.TagCountProjection;
import net.kanzi.kz.dto.TagResponse;
import net.kanzi.kz.dto.post.PostRequestDto;
import net.kanzi.kz.repository.search.SearchBoardRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long>  {

    @Modifying
    @Query("DELETE FROM Tag t WHERE t.post = :post")
    int deleteTagsByPost(@Param("post") Post post);

    @Query("SELECT t.name, COUNT(t) AS tagCount FROM Tag t GROUP BY t.name order by tagCount LIMIT 10")
    List<Object[]> getTagsWithCount();


    @Query("SELECT NEW net.kanzi.kz.dto.TagResponse(t.name, COUNT(t)) FROM Tag t GROUP BY t.name order by COUNT(t) LIMIT 10")
    List<TagResponse> getTopTags(Pageable page);
}

