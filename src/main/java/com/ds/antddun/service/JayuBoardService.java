package com.ds.antddun.service;

import com.ds.antddun.dto.JayuBoardDTO;
import com.ds.antddun.dto.PageRequestDTO;
import com.ds.antddun.dto.PageResultDTO;
import com.ds.antddun.entity.JayuBoard;
import com.ds.antddun.entity.JayuCategory;
import com.ds.antddun.entity.Member;
public interface JayuBoardService {

    Long register(JayuBoardDTO jayuBoardDTO, Member member);

    JayuBoardDTO read(Long jayuNo);

    void modify(JayuBoardDTO jayuBoardDTO);

    void remove(JayuBoardDTO jayuBoardDTO, Long mno);

    //목록 조회
    PageResultDTO<JayuBoardDTO,Object[]> getList(PageRequestDTO pageRequestDTO);

    //카테고리 목록 조회
    PageResultDTO<JayuBoardDTO, Object[]> getListByCate(int jayuCateNo,PageRequestDTO pageRequestDTO);

    default JayuBoardDTO entityToDTO(JayuBoard jayuBoard, Long likesCnt, Long replyCnt) {
        JayuBoardDTO jayuBoardDTO = JayuBoardDTO.builder()
                .jayuNo(jayuBoard.getJayuNo())
                .mno(jayuBoard.getMember().getMno())
                .jayuCateNo(jayuBoard.getJayuCategory().getJayuCateNo())
                .jayuCateName(jayuBoard.getJayuCategory().getJayuCateName())
                .title(jayuBoard.getTitle())
                .content(jayuBoard.getContent())
                .writer(jayuBoard.getMember().getJob().getJob() + " " +jayuBoard.getMember().getExperience()+"년차 " + jayuBoard.getMember().getLastName() +"개미")
                .viewCnt(jayuBoard.getViewCnt())
                .regDate(jayuBoard.getRegDate())
                .modDate(jayuBoard.getModDate())
                .build();
        jayuBoardDTO.setLikesCnt(likesCnt.intValue());
        jayuBoardDTO.setReplyCnt(replyCnt.intValue());

        return jayuBoardDTO;
    }
    default JayuBoard dtoToEntity(JayuBoardDTO jayuBoardDTO) {
        JayuCategory jayuCategory = JayuCategory.builder().jayuCateNo(jayuBoardDTO.getJayuCateNo()).build();

        JayuBoard jayuBoard = JayuBoard.builder()
                .jayuNo(jayuBoardDTO.getJayuNo())
                .jayuCategory(jayuCategory)
                .title(jayuBoardDTO.getTitle())
                .content(jayuBoardDTO.getContent())
                .viewCnt(jayuBoardDTO.getViewCnt())
                .build();

        return jayuBoard;
    }
}
