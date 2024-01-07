package net.kanzi.kz.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.log4j.Log4j2;
import net.kanzi.kz.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Post.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Post search1() {

        log.info("search1........................");

        QPost post = QPost.post;
        QTag tag = QTag.tag;
//        JPQLQuery<Post> query = from(post);
//        query.select(post).where(post.id.eq(32L));

        List<Post> list = jpaQueryFactory.selectFrom(post)
                .leftJoin(post.tags, tag)
                .fetchJoin()
                .fetch();
        for (Post p : list) {
            System.out.println("p = " + p.getTags());
        }

        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String[] tags, String keyword, Pageable pageable) {
        log.info("search page");
        System.out.println("type = " + type);
        System.out.println("tags = " + Arrays.toString(tags));
        System.out.println("Pageable = " + pageable);
        QPost post = QPost.post;
        QTag tag = QTag.tag;
        QUser user = QUser.user;
        QComment comment = QComment.comment;

        JPQLQuery<Post> jpqlQuery = from(post);
        jpqlQuery.leftJoin(user).on(post.uid.eq(user.uid));
        jpqlQuery.leftJoin(comment).on(comment.post.eq(post));
        jpqlQuery.leftJoin(tag).on(tag.post.eq(post));

        //SELECT b, w, count(r) FROM Board b
        //LEFT JOIN b.writer w LEFT JOIN Reply r ON r.board = b
        JPQLQuery<Tuple> tuple = jpqlQuery.select(post, user, comment.count());

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

        tuple.where(booleanBuilder);
        tuple.groupBy(post);

        Objects.requireNonNull(this.getQuerydsl()).applyPagination(pageable, tuple);

        List<Tuple> result = tuple.fetch();

        log.info(result);

        long count = tuple.fetchCount();

        log.info("COUNT: " +count);

        return new PageImpl<Object[]>(
                result.stream().map(Tuple::toArray).collect(Collectors.toList()),
                pageable,
                count);
    }

}
