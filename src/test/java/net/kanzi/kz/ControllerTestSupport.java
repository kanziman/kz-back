package net.kanzi.kz;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.kanzi.kz.apiController.CommentApiController;
import net.kanzi.kz.apiController.PostApiController;
import net.kanzi.kz.apiController.UserApiController;
import net.kanzi.kz.repository.UserRepository;
import net.kanzi.kz.service.CommentService;
import net.kanzi.kz.service.PostService;
import net.kanzi.kz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
    PostApiController.class,
    CommentApiController.class,
        UserApiController.class
})
@AutoConfigureMockMvc(addFilters = false)
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected PostService postService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected UserRepository userRepository;

}
