package net.kanzi.kz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.kanzi.kz.domain.Tag;

@Getter
@ToString
public class TagResponse {

    private final String name;
    private final Long tagCount;
    public TagResponse(String name, Long tagCount) {
        this.name = name;
        this.tagCount = tagCount;
    }

}
