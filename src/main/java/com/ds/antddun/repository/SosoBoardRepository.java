package com.ds.antddun.repository;

import com.ds.antddun.entity.SosoJobBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SosoBoardRepository extends JpaRepository<SosoJobBoard, Long> {

    @Query("SELECT sj, sc " +
            " FROM SosoJobBoard sj, SosoCategory sc " +
            " WHERE sj.category=sc.cateNo " +
            " AND sc.sosoCateName=:category ")
    List<SosoJobBoard> getListByCategory(String category);

    @Query("SELECT sj, sc " +
            " FROM SosoJobBoard sj, SosoCategory sc " +
            " WHERE sj.category=sc.cateNo " +
            " AND sc.cateNo=:categoryNo ")
    List<SosoJobBoard> getListByCategoryNo(int categoryNo);

//    @Query(value="SELECT * FROM soso_job_board sj, soso_category sc WHERE sj.category_cate_no = sc.cate_no AND sj.category_cate_no=:categoryNo ",
//    countQuery = "SELECT count(*) FROM soso_job_board sj, soso_category sc WHERE sj.category_cate_no = sc.cate_no AND sj.category_cate_no=:categoryNo ",
//    nativeQuery = true)
    @Query(value = "select * from soso_job_board where category_cate_no=:categoryNo ", nativeQuery = true)
    Page<SosoJobBoard> findAllByCategory(int categoryNo, Pageable pageable);

    @Query("SELECT sj, sc, m " +
            " FROM SosoJobBoard sj, SosoCategory sc, Member m " +
            " WHERE sj.category.cateNo=sc.cateNo " +
            " AND sj.member.mno=m.mno " +
            " AND sj.sosoNo=:sosoNo ")
    Optional<SosoJobBoard> readBySosoNo(Long sosoNo);

}
