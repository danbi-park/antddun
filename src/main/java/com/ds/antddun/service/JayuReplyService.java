package com.ds.antddun.service;

import com.ds.antddun.dto.JayuReplyDTO;
import com.ds.antddun.entity.*;

import java.util.List;

public interface JayuReplyService {

    //댓글 등록
    Long register(JayuReplyDTO jayuReplyDTO);

    //댓글 목록
    List<JayuReply> getReplyByJayuNo(Long jayuNo);

    //댓글 수정
    void modify(JayuReplyDTO jayuReplyDTO);

    //댓글 삭제
    void replyRemove(Long jayuRno);

    //게시글 삭제
    void remove(Long jayuNo);

    default JayuReply dtoToEntity(JayuReplyDTO jayuReplyDTO) {

        JayuReply jayuReply = JayuReply.builder()
                .jayuRno(jayuReplyDTO.getJayuRno())
                .replyText(jayuReplyDTO.getReplyText())
                .member(Member.builder().mno(jayuReplyDTO.getMno()).build())
                .jayuBoard(JayuBoard.builder().jayuNo(jayuReplyDTO.getJayuNo()).build())
                .build();
        return jayuReply;
    }
}