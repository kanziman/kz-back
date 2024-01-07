package net.kanzi.kz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.kanzi.kz.domain.Post;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class AddBookMarkRequest {
    private String uid;
}
