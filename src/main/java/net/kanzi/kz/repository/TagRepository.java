package net.kanzi.kz.repository;

import net.kanzi.kz.domain.Tag;
import net.kanzi.kz.dto.TagResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long>  {


    @Query("SELECT NEW net.kanzi.kz.dto.TagResponse(t.name, COUNT(t)) FROM Tag t GROUP BY t.name order by COUNT(t) desc")
    List<TagResponse> getTopTags();

    @Modifying
    void deleteAllInBatch();

}

