package com.ds.antddun.service;

import com.ds.antddun.dto.JayuBoardDTO;
import com.ds.antddun.dto.PageRequestDTO;
import com.ds.antddun.dto.PageResultDTO;
import com.ds.antddun.entity.*;
import com.ds.antddun.repository.JayuBoardRepository;
import com.ds.antddun.repository.JayuCateRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class JayuBoardServiceImpl implements JayuBoardService{

    private final JayuBoardRepository jayuBoardRepository;

    private final JayuCateRepository jayuCateRepository;

    private final JayuLikesService jayuLikesService;

    private final JayuReplyService jayuReplyService;

    @Override
    public Long register(JayuBoardDTO jayuBoardDTO, Member member){
        JayuCategory cateName = jayuCateRepository.findById(jayuBoardDTO.getJayuCateNo()).get();

        JayuBoard jayuBoard = dtoToEntity(jayuBoardDTO);
        jayuBoard.setMember(member);
        jayuBoard.setJayuCategory(cateName);
        jayuBoardRepository.save(jayuBoard);

        return jayuBoard.getJayuNo();
    }

    @Override
    public JayuBoardDTO read(Long jayuNo) {
        List<Object[]> result = jayuBoardRepository.getBoard(jayuNo);
        JayuBoard jayuBoard = (JayuBoard) result.get(0)[0];
        Long likesCnt = (Long) result.get(0)[1];
        Long replyCnt = (Long) result.get(0)[2];

        return entityToDTO(jayuBoard, likesCnt, replyCnt);
    }

    @Transactional
    @Override
    public void modify(JayuBoardDTO jayuBoardDTO){
        JayuCategory jayuCategory = jayuCateRepository.findById(jayuBoardDTO.getJayuCateNo()).get();
        Optional<JayuBoard> result = jayuBoardRepository.findById(jayuBoardDTO.getJayuNo());
        if (result.isPresent()) {
            JayuBoard jayuboard = result.get();
            jayuboard.setTitle(jayuBoardDTO.getTitle());
            jayuboard.setContent(jayuBoardDTO.getContent());
            jayuboard.setJayuCategory(jayuCategory);
            jayuBoardRepository.save(jayuboard);
        }
    }


    @Transactional
    @Override
    public void remove(JayuBoardDTO jayuBoardDTO, Long mno) {
        Optional<JayuBoard> result = jayuBoardRepository.findById(jayuBoardDTO.getJayuNo());

        if (result.isPresent()) {
            JayuBoard jayuBoard = result.get();
            jayuBoard.setJayuCategory(null);
        }

        jayuLikesService.deleteLikes(jayuBoardDTO.getJayuNo(),mno);
        jayuReplyService.remove(jayuBoardDTO.getJayuNo());

        jayuBoardRepository.deleteById(jayuBoardDTO.getJayuNo());
    }

    //게시물 목록
    @Override
    public PageResultDTO<JayuBoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO){
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("regDate").descending());
        //검색조건 처리
        BooleanBuilder booleanBuilder = getSearch(pageRequestDTO);
        Page<Object[]> result = jayuBoardRepository.getList(booleanBuilder, pageable);

        Function<Object[], JayuBoardDTO> fn = (arr -> entityToDTO(
                (JayuBoard) arr[0],
                (Long) arr[1],
                (Long) arr[2]
        ));
        return new PageResultDTO<>(result, fn);
    }

    private BooleanBuilder getSearch(PageRequestDTO pageRequestDTO) {
        String keyword = pageRequestDTO.getKeyword();
        log.info("keyword>>>>>"+keyword);
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QJayuBoard qJayuBoard = QJayuBoard.jayuBoard;
        //jayuNo > 0 조건만 생성
        BooleanExpression expression = qJayuBoard.jayuNo.gt(0L); // gno>0 조건만 생성
        booleanBuilder.and(expression);

        //검색조건이 없는 경우
        if (keyword == null || keyword.trim().length() == 0) {
            return booleanBuilder;
        }

        //검색조건 작성
        BooleanBuilder conditionBuilder = new BooleanBuilder();
        conditionBuilder.or(qJayuBoard.title.contains(keyword));

        booleanBuilder.and(conditionBuilder);
        return booleanBuilder;
    }

    //카테고리 별 게시물 목록
    @Override
    public PageResultDTO<JayuBoardDTO, Object[]> getListByCate(int jayuCateNo, PageRequestDTO pageRequestDTO){
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("regDate").descending());
        log.info(">>>>>jayuCateNo"+jayuCateNo);
        Page<Object[]> result = jayuBoardRepository.getListByCate(jayuCateNo, pageable);
        log.info("getListByCateresult" + result);
        Function<Object[], JayuBoardDTO> fn = (arr -> entityToDTO(
                (JayuBoard) arr[0],
                (Long) arr[1],
                (Long) arr[2]
        ));

        return new PageResultDTO<>(result, fn);
    }
}