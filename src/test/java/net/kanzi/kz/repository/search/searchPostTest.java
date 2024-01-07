package net.kanzi.kz.repository.search;

import net.kanzi.kz.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class searchPostTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    public void testSearch(){
        postRepository.search1();
    }

}