package net.kanzi.kz.repository.search;


import net.kanzi.kz.domain.*;
import net.kanzi.kz.dto.post.PostRequestDto;
import org.springframework.data.domain.PageImpl;
import java.util.List;


public interface SearchPostRepository {

    User searchPost(Post p);
    /**
     * v1 - simple query with offset and limit
     * @param offset
     * @param limit
     * @return
     */
    public List<Post> findPostsTagsUsersPage(int offset, int limit);
    /**
     * v3- 반환 타입 PageImpl (사용중)
     * @param postRequestDto
     * @return
     */
    public PageImpl searchQuery(PostRequestDto postRequestDto);
    public PageImpl searchQueryPage(PostRequestDto postRequestDto);

    /**
     * v2 - 반환 타입 List<Post>
     * @param postRequestDto
     * @return List<Post>
     */
    public List<Post> searchQueryPosts(PostRequestDto postRequestDto) ;


    public List<Post> search(PostRequestDto postRequestDto);

}
