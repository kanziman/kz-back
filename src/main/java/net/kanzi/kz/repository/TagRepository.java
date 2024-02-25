package net.kanzi.kz.repository;

import net.kanzi.kz.domain.Post;
import net.kanzi.kz.domain.Tag;
import net.kanzi.kz.dto.TagResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long>  {


    @Query("SELECT NEW net.kanzi.kz.dto.TagResponse(t.name, COUNT(t)) FROM Tag t GROUP BY t.name order by COUNT(t) desc")
    List<TagResponse> getTopTags();

    @Query("select t from Tag t where t.post = :post")
    List<Tag> findByPostId(Post post);

    @Modifying
    void deleteAllInBatch();

}

