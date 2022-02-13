package com.ds.antddun.repository.search;

import com.ds.antddun.entity.JayuBoard;
import com.ds.antddun.entity.QJayuBoard;
import com.ds.antddun.entity.QJayuLikes;
import com.ds.antddun.entity.QJayuReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

public class SearchJayuBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchJayuBoardRepository {

    public SearchJayuBoardRepositoryImpl(){super((JayuBoard.class));}

    @Override
    public Page<Object[]> search(int cate, String type, String keyword, Pageable pageable) {
        QJayuBoard qJayuBoard = QJayuBoard.jayuBoard;
        QJayuReply qJayuReply = QJayuReply.jayuReply;
        QJayuLikes qJayuLikes = QJayuLikes.jayuLikes;

        JPQLQuery<JayuBoard> jpqlQuery = from(qJayuBoard).where(qJayuBoard.peong.eq(false));
        jpqlQuery.leftJoin(qJayuReply).on(qJayuReply.jayuBoard.jayuNo.eq(qJayuBoard.jayuNo));
        jpqlQuery.leftJoin(qJayuLikes).on(qJayuLikes.jayuBoard.jayuNo.eq(qJayuBoard.jayuNo));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(qJayuBoard, qJayuLikes.countDistinct(), qJayuReply.countDistinct());

        BooleanBuilder booleanBuilder = new BooleanBuilder(); //검색을 실행하는 객체
        BooleanExpression expression = qJayuBoard.jayuNo.gt(0L); //검색의 조건을 처리하는 객체

        booleanBuilder.and(expression);

        if(cate != 0){
            booleanBuilder.and(qJayuBoard.jayuCategory.jayuCateNo.eq(cate));
        }

        if(type != null){
            String[] typeArr = type.split("");
            //검색 조건 작성
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t:typeArr){
                switch (t) {
                    case "t": conditionBuilder.or(qJayuBoard.title.contains(keyword));
                        break;
                    case "c": conditionBuilder.or(qJayuBoard.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);
        //order by
        Sort sort = pageable.getSort();
        sort.stream().forEach(order ->{
            Order direction = order.isAscending()?Order.ASC: Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(JayuBoard.class, "jayuBoard");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });
        tuple.groupBy(qJayuBoard.jayuNo);
        //page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        Long count = tuple.fetchCount();

        return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()),
                pageable, count);
    }

}
