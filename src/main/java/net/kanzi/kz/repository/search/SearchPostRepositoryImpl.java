package net.kanzi.kz.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import net.kanzi.kz.domain.*;
import net.kanzi.kz.dto.post.PostRequestDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static net.kanzi.kz.domain.QBookMark.bookMark;
import static net.kanzi.kz.domain.QLikes.likes;
import static net.kanzi.kz.domain.QPost.post;
import static net.kanzi.kz.domain.QTag.tag;
import static net.kanzi.kz.domain.QUser.user;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class SearchPostRepositoryImpl extends QuerydslRepositorySupport  implements SearchPostRepository{
    private final EntityManager em;
    private final JPAQueryFactory factory;

    public SearchPostRepositoryImpl(EntityManager em, JPAQueryFactory factory) {
        super(Post.class);
        this.em = em;
        this.factory = factory;
    }

    @Transactional
    public User searchPost(Post p){
        JPAQuery<User> query = factory.select(user)
                .from(post)
                .join(post.user, user)
                .leftJoin(post.tags, tag)
                .leftJoin(bookMark).on(bookMark.post.id.eq(post.id))
                .leftJoin(likes).on(likes.post.id.eq(post.id))
                .fetchJoin()
                .where(post.id.eq(p.getId()));

        User u = query.fetchOne();
        return u;
    }
    /**
     * v1 - simple query with offset and limit
     * @param offset
     * @param limit
     * @return
     */
    public List<Post> findPostsTagsUsersPage(int offset, int limit) {
        return em.createQuery(
                        "select p from Post p" +
                                " left join fetch p.user u", Post.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
    /**
     * v3- 반환 타입 PageImpl (사용중)
     * @param postRequestDto
     * @return
     */

    private BooleanExpression categoryEq(String category){
        if (!hasText(category) || "ALL".equals(category.toUpperCase())){
            return null;
        }
        return post.category.eq(category);
    }
    private BooleanExpression keywordLike(String keyword){
        if (!hasText(keyword)) {
            return null;
        }
        return post.content.contains(keyword)
                .or(post.title.contains(keyword));
    }
    private BooleanExpression tagsContain(String[] tags) {
        if (tags == null || tags.length == 0) {
            return null;
        }
        BooleanExpression result = null;
        for (String t : tags) {
            BooleanExpression tagEquals = tag.name.eq(t);
            result = result == null ? tagEquals : result.or(tagEquals);
        }
        return result;
    }

    public PageImpl searchQuery(PostRequestDto postRequestDto) {

        // Convert Sort to OrderSpecifiers

        Pageable pageable = postRequestDto.getPageable(Sort.by(postRequestDto.getSort()).descending());
        OrderSpecifier<?>[] orderSpecifiers = getOrderSpecifiers(pageable);

        JPAQuery<Post> query = factory.selectDistinct(post)
                .from(post)
                .leftJoin(post.user, user)
                .fetchJoin()
                .leftJoin(post.tags, tag)
                .where(categoryEq(postRequestDto.getCategory()),
                        tagsContain(postRequestDto.getTags()),
                        keywordLike(postRequestDto.getKeyword())
                        )
                ;
//        long count = query.fetch().stream().count();

        // Apply order specifiers
        List<Post> fetch = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifiers)
                .fetch();


        Long countQuery = factory.select(post.id.countDistinct())
                .from(post)
                .leftJoin(post.tags, tag)
                .where(categoryEq(postRequestDto.getCategory()),
                        tagsContain(postRequestDto.getTags()),
                        keywordLike(postRequestDto.getKeyword())
                ).fetchOne();


//        System.out.println("pageable = " + fetch.size());
//        System.out.println("pageable = " + pageable.getOffset());
//        System.out.println("pageable = " + pageable.getPageSize());
        System.out.println(">>> countQuery = " + countQuery);

        return new PageImpl<>(
                fetch,
                pageable,
                countQuery);
    }

    public PageImpl searchQueryPage(PostRequestDto postRequestDto) {

        // Convert Sort to OrderSpecifiers
        Pageable pageable = postRequestDto.getPageable(Sort.by(postRequestDto.getSort()).descending());

        JPAQuery<Post> query = factory.selectDistinct(post)
                .from(post)
                .leftJoin(post.user, user)
                .fetchJoin()
                .leftJoin(post.tags, tag)
                .where(categoryEq(postRequestDto.getCategory()),
                        tagsContain(postRequestDto.getTags()),
                        keywordLike(postRequestDto.getKeyword())
                        )
                ;
        // Apply order specifiers
//        OrderSpecifier<?>[] orderSpecifiers = getOrderSpecifiers(pageable);
//        List<Post> fetch = query
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(orderSpecifiers)
//                .fetch();

        JPQLQuery<Post> postJPQLQuery = getQuerydsl().applyPagination(pageable, query);
        List<Post> fetch = postJPQLQuery.fetch();
//        Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, select);

        JPAQuery<Long> countQuery = factory.select(post.id.countDistinct())
                .from(post)
                .leftJoin(post.tags, tag)
                .where(categoryEq(postRequestDto.getCategory()),
                        tagsContain(postRequestDto.getTags()),
                        keywordLike(postRequestDto.getKeyword())
                );
        return (PageImpl) PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);

    }

    /**
     * v2 - 반환 타입 List<Post>
     * @param postRequestDto
     * @return List<Post>
     */
    public List<Post> searchQueryPosts(PostRequestDto postRequestDto) {
        JPAQuery<Post> query = factory.select(post)
                .from(post)
                .leftJoin(post.user, user)
                .leftJoin(post.tags, tag)
                .where(categoryEq(postRequestDto.getCategory()),
                        tagsContain(postRequestDto.getTags()),
                        keywordLike(postRequestDto.getKeyword())
                )
                ;

        Pageable pageable = postRequestDto.getPageable(Sort.by(postRequestDto.getSort()).descending());
        System.out.println(pageable.getOffset());
        // Convert Sort to OrderSpecifiers
        OrderSpecifier<?>[] orderSpecifiers = getOrderSpecifiers(pageable);

        // Apply order specifiers
        List<Post> fetch = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifiers)
                .fetch();

        return fetch;
    }

    // ORDER BY
    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        PathBuilder postPath = new PathBuilder(Post.class, "post");
        OrderSpecifier<?>[] orderSpecifiers = pageable.getSort()
                .stream()
                .map(order -> new OrderSpecifier(order.isAscending() ? Order.ASC : Order.DESC,
                        postPath.get(order.getProperty())))
                .toArray(OrderSpecifier[]::new);
        return orderSpecifiers;
    }


    public List<Post> search(PostRequestDto postRequestDto) {
        String type = postRequestDto.getCategory();
        String[] tags = postRequestDto.getTags();
        String keyword = postRequestDto.getKeyword();

        QPost post = QPost.post;
        QTag tag = QTag.tag;
        QUser user = QUser.user;
        QComment comment = QComment.comment;

        JPQLQuery<Post> jpqlQuery = from(post);
        jpqlQuery.leftJoin(user).on(post.uid.eq(user.uid));
//        jpqlQuery.leftJoin(comment).on(comment.post.eq(post));
        jpqlQuery.leftJoin(tag).on(tag.post.eq(post));

        JPQLQuery<Post> select = jpqlQuery.select(post);

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = post.id.gt(0L);
        booleanBuilder.and(expression);

        // 검색 조건을 작성하기
        if(type != null && !type.equals("All")){
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            conditionBuilder.or(post.category.contains(type));
            booleanBuilder.and(conditionBuilder);
        }
        if(tags != null && tags.length > 0){
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            for (String t:tags) {
                conditionBuilder.or(tag.name.eq(t));
            }
            booleanBuilder.and(conditionBuilder);
        }
        if(!"".equals(keyword)){
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            conditionBuilder
                    .or(post.content.contains(keyword))
                    .or(post.title.contains(keyword));
            booleanBuilder.and(conditionBuilder);
        }
        select.where(booleanBuilder);

        Pageable pageable = postRequestDto.getPageable(Sort.by(postRequestDto.getSort()).descending());
//
//        System.out.println("pageable = " + pageable.getOffset());
//        System.out.println("pageable = " + pageable.getPageSize());
//        System.out.println("pageable = " + pageable.getSort());
//        Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, select);

        // Convert Sort to OrderSpecifiers
        OrderSpecifier<?>[] orderSpecifiers = getOrderSpecifiers(pageable);

        // Apply order specifiers

        List<Post> fetch = select.offset(postRequestDto.getOffset())
                .limit(postRequestDto.getSize())
                .orderBy(orderSpecifiers)
                .fetch();

        return fetch;
    }

}
