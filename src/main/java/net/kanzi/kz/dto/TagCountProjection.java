package net.kanzi.kz.dto;

import lombok.AllArgsConstructor;
import lombok.ToString;
public interface TagCountProjection {

    String getName();
    Long getTagCount();

}
