package net.kanzi.kz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.kanzi.kz.domain.*;
import net.kanzi.kz.dto.*;
import net.kanzi.kz.dto.post.PageResultDTO;
import net.kanzi.kz.dto.post.PostRequestDto;
import net.kanzi.kz.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Log4j2
public class PostService {

    private final BlogRepository blogRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final BookMarkRepository bookMarkRepository;

    @Transactional
    public PostResponse findById(long id, Principal principal) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        post.upReadCount();

        PostResponse postResponse = new PostResponse(post);
        if (principal != null){
            User user = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new IllegalArgumentException("not found : "));
            //user bookmark check
            List<Post> userBookMarksPosts = bookMarkRepository.getUserBookMarksPosts(user);
            postResponse.setUserBookMark(
                    userBookMarksPosts.stream()
                            .anyMatch(bookmark -> bookmark.getId().equals(post.getId()))
            );
            //user like check
            List<Post> userLikesPosts = likeRepository.getUserLikesPosts(user);
            postResponse.setUserLike(
                    userLikesPosts.stream()
                            .anyMatch(likePost -> likePost.getId().equals(post.getId()))
            );
        }

        return postResponse;
    }



    public List<CommentResponse> findCommentByPostId(long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + postId));
        List<CommentResponse> commentsWithCommenter = commentRepository.getCommentsWithCommenter(post);

        return commentsWithCommenter;
    }

    public Comment saveComment(AddCommentRequest request, String userName, Long postId) {
        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + userName));
        Comment entity = request.toEntity(userName, user.getUid());

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + postId));
        entity.setPost(post);

        return commentRepository.save(entity);
    }

    public Post save(AddPostRequest request, String userName) {
        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + userName));

        Post post = request.toEntity(user.getUid());

        post.addTags(request);
        return postRepository.save(post);
    }

    @Transactional
    public Post update(long id, AddPostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        authorizeArticleAuthor(post);

        // 기존 태그 삭제
        tagRepository.deleteTagsByPost(post);
        // update
        post.update(request);

        return post;
    }
    public void delete(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        authorizeArticleAuthor(post);
        postRepository.delete(post);
    }

    public void deleteComment(long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + commentId));

        authorizeCommentAuthor(comment);
        commentRepository.delete(comment);
    }

    public PageResultDTO<PostResponse, Object[]> findAllWithTags(PostRequestDto params, Principal principal) {
        System.out.println("params = " + params);
//        public List<PostResponse> findAllWithTags(PostRequestDto params) {
//        Page<Object[]> objects = postRepository.searchPage();


        Page<Object[]> result = postRepository.searchPage(
                params.getCategory(),
                params.getTags(),
                params.getKeyword(),
                params.getPageable(Sort.by(params.getSort()).descending())
        );

        Function<Object[], PostResponse> fn = (en -> entityToDTO((Post)en[0],(User)en[1], (Long) en[2]));


        return new PageResultDTO<>(result, fn);
    }

    private PostResponse entityToDTO(Post post, User postUser, long commentCount) {
        System.out.println("post = " + post);
        System.out.println("postUser = " + postUser);
        // convert to response dto
        PostResponse postResponse = new PostResponse(post, postUser, commentCount);

        // login user info
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email == null || "anonymousUser".equals(email)) return postResponse;
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("not found user : " + email));

        //user bookmark check
        List<Post> userBookMarksPosts = bookMarkRepository.getUserBookMarksPosts(user);
        postResponse.setUserBookMark(
                userBookMarksPosts.stream()
                        .anyMatch(bookmark -> bookmark.getId().equals(post.getId()))
        );

        //user like check
        List<Post> userLikesPosts = likeRepository.getUserLikesPosts(user);
        postResponse.setUserLike(
                userLikesPosts.stream()
                        .anyMatch(likePost -> likePost.getId().equals(post.getId()))
        );

        return postResponse;
    }

//    public List<PostResponse> fetch(PostRequestDto params){
//        //
//        List<Post> posts = postRepository.findAllWithTags(params);
//        List<PostResponse> collect = posts.stream()
//                .map(o -> toDTO(o)).collect(Collectors.toList());
//        for (PostResponse post: collect) {
//            System.out.println("post = " + post);
//        }
//        return collect;
//    }
//    public PostResponse toDTO(Post post){
//        PostResponse postResponse = new PostResponse(post);
//        PostResponse build = PostResponse.builder()
//                .id(post.getId())
//                .title(post.getTitle())
//                .content(post.getContent())
//                .uid(post.getUid())
//                .category(post.getCategory())
//                .tags(post.getTags().stream()
//                        .map(Tag::getName)
//                        .collect(Collectors.toList()))
//                .build();
//        return postResponse;
//    }

    /**
     * LIKE
     * @param postId
     * @param uid
     * @param name
     */
    @Transactional
    public void addLike(long postId, String uid) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("not found user : " + uid));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("not found post : " + postId));

        Optional<Likes> hasLike = likeRepository.getLike(post, user);

        Likes like = new Likes(post, user);
        if (hasLike.isPresent()){
            // delete
            System.out.println("like delete");
            Likes likes = hasLike.get();
            likeRepository.delete(likes);
            post.updateLikeCount(-1);
        } else {
            // insert
            System.out.println("like insert");
            likeRepository.save(like);
            post.updateLikeCount(1);
        }

    }
    public boolean hasLike(long postId, String uid) {
        System.out.println("hasLike = " + postId);
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("not found user : " + uid));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("not found post : " + postId));

        Optional<Likes> hasLike = likeRepository.getLike(post, user);
        return hasLike.isPresent();
    }

    /**
     * BOOKMARK
     * @param postId
     * @param uid
     * @param name
     */
    @Transactional
    public void addBookMark(long postId, String uid) {

        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("not found user : " + uid));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("not found post : " + postId));

        Optional<BookMark> hasBookMark = bookMarkRepository.getBookMark(post, user);

        BookMark bookMark = new BookMark(post, user);
        if (hasBookMark.isPresent()){
            // delete
            BookMark mark = hasBookMark.get();
            bookMarkRepository.delete(mark);
            post.updateBookMarkCount(-1);
        } else {
            // insert
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

    public List<PostResponse> getUserBookMarksPosts(String uid) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("not found user : " + uid));
        //user bookmark (북마크 조회기 떄문에 북마크는 전부 true )
        List<Post> bookMarksPosts = bookMarkRepository.getUserBookMarksPosts(user);
        //user like check
        List<Post> userLikesPosts = likeRepository.getUserLikesPosts(user);

        List<PostResponse> responses = bookMarksPosts.stream()
                .map(post -> {
                    PostResponse response = new PostResponse(post);
                    response.setUserBookMark(true);
                    response.setUserLike(isUserLikedPost(userLikesPosts, response));
                    return response;
                })
                .collect(Collectors.toList());

        return responses;
    }

    // user 가 like 한 post 인지 체크
    private boolean isUserLikedPost(List<Post> userLikesPosts, PostResponse postResponse) {
        return userLikesPosts.stream()
                .anyMatch(likePost -> likePost.getId().equals(postResponse.getId()));
    }

    public List<TagResponse> getTags(){
        List<TagResponse> topTags = tagRepository.getTopTags(PageRequest.of(0, 10));
        return topTags;
    }


    // 게시글을 작성한 유저인지 확인
    private void authorizeArticleAuthor(Post post) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("not found user : " + email));

        if (!post.getUid().equals(user.getUid())) {
            throw new IllegalArgumentException("not authorized");
        }
    }
    private void authorizeCommentAuthor(Comment comment) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("not found user : " + email));

        if (!comment.getUid().equals(user.getUid())) {
            throw new IllegalArgumentException("not authorized");
        }
    }


}
