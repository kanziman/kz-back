package net.kanzi.kz.repository.search;

import net.kanzi.kz.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchBoardRepository {

    Post search1();

    Page<Object[]> searchPage(String type, String[] tags, String keyword, Pageable pageable);

}
