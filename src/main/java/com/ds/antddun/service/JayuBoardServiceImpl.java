package com.ds.antddun.service;

import com.ds.antddun.dto.JayuBoardDTO;
import com.ds.antddun.dto.PageRequestDTO;
import com.ds.antddun.dto.PageResultDTO;
import com.ds.antddun.entity.JayuBoard;
import com.ds.antddun.entity.JayuCategory;
import com.ds.antddun.entity.Member;
import com.ds.antddun.repository.JayuBoardRepository;
import com.ds.antddun.repository.JayuCateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
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
            jayuboard.setPeong(jayuBoardDTO.isPeong());
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
    public PageResultDTO<JayuBoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        Page<Object[]> result = jayuBoardRepository.search(
                pageRequestDTO.getCate(),
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("regDate").descending())
        );

        Function<Object[], JayuBoardDTO> fn = (arr -> entityToDTO(
                (JayuBoard) arr[0],
                (Long) arr[1],
                (Long) arr[2]
        ));

        return new PageResultDTO<>(result, fn);
    }

    //펑예 목록
    @Override
    public PageResultDTO<JayuBoardDTO, Object[]> getPeongList(PageRequestDTO pageRequestDTO) {

        Page<Object[]> result = jayuBoardRepository.getPeongList(
                pageRequestDTO.getPageable(Sort.by("regDate").descending())
        );

        Function<Object[], JayuBoardDTO> fn = (arr -> entityToDTO(
                (JayuBoard) arr[0],
                (Long) arr[1],
                (Long) arr[2]
        ));

        return new PageResultDTO<>(result, fn);
    }
}