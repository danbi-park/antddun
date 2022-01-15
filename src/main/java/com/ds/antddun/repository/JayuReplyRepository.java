package com.ds.antddun.repository;

import com.ds.antddun.entity.JayuReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JayuReplyRepository extends JpaRepository<JayuReply, Long> {

    //댓글 리스트
    @Query("SELECT reply FROM JayuReply reply WHERE reply.jayuBoard.jayuNo=:jayuNo ")
    List<JayuReply> getReplyListByJayuNo(@Param("jayuNo") Long jayuNo);

    @Modifying
    @Query("DELETE FROM JayuReply reply WHERE reply.jayuBoard.jayuNo=:jayuNo ")
    void remove(Long jayuNo);
}
