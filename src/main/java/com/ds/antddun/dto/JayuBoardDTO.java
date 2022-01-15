package com.ds.antddun.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JayuBoardDTO {

    private Long jayuNo, mno;
    private String title, content, writer, jayuCateName;
    private int jayuCateNo, viewCnt, likesCnt, replyCnt ;

    private LocalDateTime regDate, modDate;
}
