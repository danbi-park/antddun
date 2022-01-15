package com.ds.antddun.controller;

import com.ds.antddun.config.auth.PrincipalDetails;
import com.ds.antddun.dto.PageRequestDTO;
import com.ds.antddun.dto.PageResultDTO;
import com.ds.antddun.dto.QnaBoardDTO;
import com.ds.antddun.entity.JobList;
import com.ds.antddun.entity.MemberWishList;
import com.ds.antddun.repository.QnaBoardRepository;
import com.ds.antddun.service.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Log4j2
public class QnaBoardController {

    @Autowired
    private JobListService jobListService;

    @Autowired
    private QnaBoardRepository qnaBoardRepository;

    @Autowired
    private QnaService qnaService;

    @Autowired
    private QnaLikesService qnaLikeService;

    @Autowired
    private WishListService wishListService;

    @Autowired
    private QnaReplyService qnaReplyService;

    @Autowired
    private DdunService ddunService;

    //리스트 출력
    @GetMapping("/qna/list")
    public String allList(Model model, PageRequestDTO requestDTO,
                          @AuthenticationPrincipal PrincipalDetails principal) {
        PageResultDTO<QnaBoardDTO, Object[]> getListAll = qnaService.getListAll(requestDTO);
        model.addAttribute("boardList",getListAll);

        List<JobList> list = jobListService.getList();
        model.addAttribute("jobList", list);

        return "/qna/list";
    }

    //게시물 작성 양식
    @GetMapping("/member/qna/register")
    public String register(@AuthenticationPrincipal PrincipalDetails principal, Model model) {

        if (principal == null) {
            return "redirect:/login";
        }

        model.addAttribute("jobList", jobListService.getList());
        model.addAttribute("totalDdun", ddunService.totalAmountByMno(principal.getMember().getMno()));

        return "/qna/register";
    }


    //글 등록
    @PostMapping("/member/qna/register")
    public String register(QnaBoardDTO qnaBoardDTO, RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal PrincipalDetails principal) {
        Long qnaNo = qnaService.register(qnaBoardDTO, principal.getMember());
        redirectAttributes.addFlashAttribute("qnaNo", qnaNo);
        return "redirect:/member/qna/read?qnaNo=" + qnaNo;
    }


    //게시판 조회
    @GetMapping("/member/qna/read")
    public String read(Long qnaNo, @AuthenticationPrincipal PrincipalDetails principal, Model model) {
        if (principal == null) { return "redirect:/login";}
        qnaBoardRepository.updateViewCnt(qnaNo);

        //게시판 정보
        model.addAttribute("boardList", qnaService.getBoard(qnaNo));
        model.addAttribute("replyList", qnaReplyService.getListByQnaNo(qnaNo));
        model.addAttribute("preMem", principal.getMember().getMno()); //현재 멤버의 mno

        //권한 따라 수정 삭제 버튼 보이기
        Long actualWriter = qnaBoardRepository.getById(qnaNo).getMember().getMno();
        if (principal.getMember().getMno() == actualWriter) {
            model.addAttribute("checkUser", true);
        }

        //좋아요 체크 유무
        model.addAttribute("qnaCheck", qnaLikeService.checkLikes(qnaNo, principal.getMember().getMno()));

        return "/qna/read";
    }


    //게시물 수정 폼
    @GetMapping("/member/qna/modify")
    public String modifyForm(@RequestParam("qnaNo") Long qnaNo, Model model){
        model.addAttribute("jobList", jobListService.getList());

        QnaBoardDTO boardList = qnaService.getBoard(qnaNo);
        model.addAttribute("boardList", boardList);

        model.addAttribute("totalDdun", ddunService.totalAmountByMno(boardList.getMno()));
        return "/qna/modify";
    }

    //게시물 수정
    @PostMapping("/member/qna/modify")
    public String modifyBoard(QnaBoardDTO qnaBoardDTO, @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                              @AuthenticationPrincipal PrincipalDetails principal) {
        qnaService.modify(qnaBoardDTO, principal.getMember());

        Long qnaNo = qnaBoardDTO.getQnaNo();
        int page = requestDTO.getPage();

        return "redirect:/member/qna/read?qnaNo=" + qnaNo + "&page=" + page ;
    }


    //게시물 삭제
    @GetMapping("/member/qna/remove")
    public String removeBoard(QnaBoardDTO qnaBoardDTO, @AuthenticationPrincipal PrincipalDetails principal){
        Long qnaNo = qnaBoardDTO.getQnaNo();
        Long mno = principal.getMember().getMno();

        qnaService.deleteAll(qnaNo, mno);

        return "redirect:/qna/list";
    }



}