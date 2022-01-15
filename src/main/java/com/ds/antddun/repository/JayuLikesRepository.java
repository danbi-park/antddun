package com.ds.antddun.repository;

import com.ds.antddun.entity.JayuLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JayuLikesRepository extends JpaRepository<JayuLikes, Long>{
    //jayuNo와 mno를 인자로 받아서 해당 게시물에 해당 회원이 좋아요를 등록한 적이 있는지 체크
    @Query("SELECT jl FROM JayuLikes jl WHERE jl.jayuBoard.jayuNo=:jayuNo AND jl.member.mno=:mno ")
    JayuLikes checkLikeByBnoAndMno(Long jayuNo, Long mno);

    //좋아요
    @Modifying
    @Query(value = "INSERT INTO JAYU_LIKES (JAYU_NO, MNO) VALUES(:jayuNo, :mno) ", nativeQuery = true)
    void addLikes(Long jayuNo, Long mno);


    //좋아요 삭제
    @Modifying
    @Query(value = "DELETE FROM JAYU_LIKES WHERE JAYU_NO =:jayuNo AND MNO =:mno ", nativeQuery = true)
    void deleteLikes(Long jayuNo, Long mno);

    //좋아요 개수 세기
    @Query("SELECT COUNT(jl.jayuBoard.jayuNo) FROM JayuLikes jl WHERE jl.jayuBoard.jayuNo =:jayuNo ")
    int countLikes(Long jayuNo);


}
