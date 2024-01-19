package net.kanzi.kz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.kanzi.kz.domain.*;
import net.kanzi.kz.dto.*;
import net.kanzi.kz.dto.comment.AddCommentRequest;
import net.kanzi.kz.dto.comment.CommentResponse;
import net.kanzi.kz.dto.post.AddPostRequest;
import net.kanzi.kz.dto.post.PageResultDTO;
import net.kanzi.kz.dto.post.PostRequestDto;
import net.kanzi.kz.dto.post.PostResponse;
import net.kanzi.kz.repository.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Log4j2
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final BookMarkRepository bookMarkRepository;

    public List<PostResponse> getAllPostByUser(User user) {
        List<Post> posts = postRepository.findAllByUser(user);
        return posts.stream()
                .map(post->PostResponse.of(post))
                .collect(Collectors.toList());
    }

    // v2
    public List<PostResponse> getPostsTagsPage(PostRequestDto postRequestDto) {
        List<Post> posts = postRepository.searchQueryPosts(postRequestDto);
        return posts.stream()
                .map(post -> PostResponse.of(post))
                .collect(Collectors.toList());
    }

    /**
     * GET POSTS v3 사용중
     * @param postRequestDto
     * @return
     */
    public PageResultDTO getPostsPage(PostRequestDto postRequestDto) {
        PageImpl result = postRepository.searchQueryPage(postRequestDto);
        Function<Post, PostResponse> fn = (en -> PostResponse.of(en));
        return  new PageResultDTO<>(result, fn);
    }


    /**
     * CREATE POST
     *
     * @param userName
     * @return id
     */

    public PostResponse addPost(AddPostRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + email));

        // dto -> post
        Post post = request.toEntity();
        // post + user = create
        Post postCreate = Post.create(post, user);
        // save
        Post save = postRepository.save(postCreate);

        return PostResponse.of(save);
    }

    /**
     * GET POST ONE
     * @param id
     */
    public PostResponse findById(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        post.upReadCount();

        return PostResponse.of(post);
    }

    /**
     * DELETE POST
     * @param id
     */

    public void delete(long id, String email) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        authorizeArticleAuthor(post, email);
        postRepository.delete(post);
    }

    /**
     * UPDATE POST
     *
     * @param id
     * @param request
     * @return
     */
    @Transactional(readOnly = false)
    public PostResponse update(long id, AddPostRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        authorizeArticleAuthor(post, email);

        // 기존 태그 삭제
//        tagRepository.deleteTagsByPost(post);
        tagRepository.deleteAllInBatch(post.getTags());

        // update
        post.update(request);

        PostResponse postResponse = PostResponse.of(post);
        return postResponse;
    }



    /**
     * LIKES
     */
    @Transactional(readOnly = false)
    public void addLike(long postId, String uid) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("not found user : " + uid));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("not found post : " + postId));

        Optional<Likes> hasLike = likeRepository.getLike(post, user);

        Likes like = new Likes(post, user);
        if (hasLike.isPresent()){
            log.debug("like delete");
            Likes likes = hasLike.get();
            likeRepository.delete(likes);
            post.updateLikeCount(-1);
        } else {
            log.debug("like insert");
            likeRepository.save(like);
            post.updateLikeCount(1);
        }

    }
    public boolean hasLike(long postId, String uid) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("not found user : " + uid));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("not found post : " + postId));

        Optional<Likes> hasLike = likeRepository.getLike(post, user);
        return hasLike.isPresent();
    }

    /**
     * BOOKMARK
     */
    @Transactional(readOnly = false)
    public void addBookMark(long postId, String uid) {

        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("not found user : " + uid));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("not found post : " + postId));

        Optional<BookMark> hasBookMark = bookMarkRepository.getBookMark(post, user);

        BookMark bookMark = new BookMark(post, user);
        if (hasBookMark.isPresent()){
            BookMark mark = hasBookMark.get();
            bookMarkRepository.delete(mark);
            post.updateBookMarkCount(-1);
        } else {
            bookMarkRepository.save(bookMark);
            post.updateBookMarkCount(1);
        }

    }
    public boolean hasBookMark(long postId, String uid) {

        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("not found user : " + uid));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("not found post : " + postId));

        Optional<BookMark> hasBookMark = bookMarkRepository.getBookMark(post, user);
        return hasBookMark.isPresent();
    }



    public List<TagResponse> getTopTags(){
        List<TagResponse> topTags = tagRepository.getTopTags();
        return topTags;
    }


    // 게시글을 작성한 유저인지 확인
    private void authorizeArticleAuthor(Post post, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("not found user : " + email));

        if (!post.getUid().equals(user.getUid())) {
            throw new IllegalArgumentException("not authorized");
        }
    }


}
