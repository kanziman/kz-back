package net.kanzi.kz.unit;

import net.kanzi.kz.dto.post.AddPostRequest;

public class PostRequest {
    public static AddPostRequest creatAddPostRequest() {
        AddPostRequest request = AddPostRequest.builder()
                .uid("user-uuid")
                .title("t").content("c").category("cate")
                .build();
        return request;
    }

}
