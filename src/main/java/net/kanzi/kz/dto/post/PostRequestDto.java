package net.kanzi.kz.dto.post;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@ToString
public class PostRequestDto
{
    private int page;
    private int size;
    private String category ;
    private int limit;
    private String sort;
    private String[] tags;
    private String keyword;

    public PostRequestDto() {
        this.page = 1;
        this.size = 10;
        this.sort = "createdAt";
        this.category = "All";
        this.keyword = "";
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page -1, size, sort);
    }
}


