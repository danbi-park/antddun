package com.ds.antddun.repository;


import com.ds.antddun.entity.JayuBoard;
import com.ds.antddun.repository.search.SearchJayuBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JayuBoardRepository extends JpaRepository<JayuBoard, Long>,
        SearchJayuBoardRepository{

    //게시글 내용 출력
    @Query("SELECT jayu, COUNT(distinct likes.jayuLno), COUNT(distinct reply.jayuRno) " +
            "FROM JayuBoard jayu " +
            "LEFT OUTER JOIN JayuLikes likes " +
            "ON likes.jayuBoard.jayuNo = jayu.jayuNo " +
            "LEFT OUTER JOIN JayuReply reply " +
            "ON reply.jayuBoard.jayuNo = jayu.jayuNo " +
            "WHERE jayu.jayuNo=:jayuNo "+
            "group by jayu.jayuNo " )
    List<Object[]> getBoard(Long jayuNo);

    //조회수
    @Transactional
    @Modifying
    @Query("update JayuBoard set viewCnt = viewCnt + 1 where jayuNo=:jayuNo")
    void updateViewCnt(Long jayuNo);

    //펑예 목록
    @Query("SELECT jayu, COUNT(distinct likes.jayuLno), COUNT(distinct reply.jayuRno) " +
            "FROM JayuBoard jayu " +
            "LEFT OUTER JOIN JayuLikes likes " +
            "ON likes.jayuBoard.jayuNo = jayu.jayuNo " +
            "LEFT OUTER JOIN JayuReply reply " +
            "ON reply.jayuBoard.jayuNo = jayu.jayuNo " +
            "WHERE jayu.peong=true " +
            "AND FUNCTION('DATEDIFF', 'HOUR', jayu.regDate, current_date) < 1 "+
            "group by jayu.jayuNo " )
    Page<Object[]> getPeongList(Pageable pageable);
}
