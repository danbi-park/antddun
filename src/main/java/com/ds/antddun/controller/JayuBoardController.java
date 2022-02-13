package com.ds.antddun.controller;

import com.ds.antddun.config.auth.PrincipalDetails;
import com.ds.antddun.dto.JayuBoardDTO;
import com.ds.antddun.dto.PageRequestDTO;
import com.ds.antddun.dto.PageResultDTO;
import com.ds.antddun.entity.MemberWishList;
import com.ds.antddun.repository.JayuBoardRepository;
import com.ds.antddun.service.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Log4j2
public class JayuBoardController {

    @Autowired
    private JayuBoardService jayuBoardService;

    @Autowired
    private JayuBoardRepository jayuBoardRepository;

    @Autowired
    private WishListService wishListService;

    @Autowired
    private JayuCateService jayuCateService;

    @Autowired
    private JayuReplyService jayuReplyService;

    @Autowired
    private JayuLikesService jayuLikesService;

    //게시글 작성
    @GetMapping("/member/jayu/register")
    public String register(Model model, @AuthenticationPrincipal PrincipalDetails principal){

        if (principal == null) {return "redirect:/login";}

        //카테고리
        model.addAttribute("cateList", jayuCateService.getCateList());

        //위시리스트
        List<MemberWishList> wishLists = wishListService.getListByMno(principal.getMember().getMno());
        model.addAttribute("wishList", wishListService.getListByMno(principal.getMember().getMno()));
        if (wishLists.size() != 0) {
            model.addAttribute("wishListIndex", wishLists.get(0));
        }

        return "jayu/register";
    }

    //게시글 등록
    @PostMapping("/member/jayu/register")
    public String register(JayuBoardDTO jayuBoardDTO, RedirectAttributes redirectAttributes, @AuthenticationPrincipal PrincipalDetails principal){
        Long jayuNo = jayuBoardService.register(jayuBoardDTO, principal.getMember());
        redirectAttributes.addFlashAttribute("jayuNo", jayuNo);

        return "redirect:/member/jayu/read?jayuNo="+jayuNo;
    }

    //게시글 조회
    @GetMapping("/member/jayu/read")
    public String read(Long jayuNo, Model model, @AuthenticationPrincipal PrincipalDetails principal) {
        if (principal == null) {return "redirect:/login";}

        //조회수 추가
        jayuBoardRepository.updateViewCnt(jayuNo);

        //게시판 정보
        model.addAttribute("jayuBoardDTO", jayuBoardService.read(jayuNo));

        //좋아요 체크 유무
        model.addAttribute("lieksCheck", jayuLikesService.checkLikes(jayuNo, principal.getMember().getMno()));

        //수정 삭제 버튼 보이기
        Long actualWriter = jayuBoardRepository.getById(jayuNo).getMember().getMno();
        if (principal.getMember().getMno() == actualWriter) {
            model.addAttribute("checkUser", true);
        }

        //댓글
        model.addAttribute("replyList",jayuReplyService.getReplyByJayuNo(jayuNo));
        model.addAttribute("replier", principal.getMember().getMno());

        //위시리스트
        List<MemberWishList> wishLists = wishListService.getListByMno(principal.getMember().getMno());
        model.addAttribute("wishList", wishListService.getListByMno(principal.getMember().getMno()));
        if (wishLists.size() != 0) {
            model.addAttribute("wishListIndex", wishLists.get(0));
        }

        return "/jayu/read";
    }

    //게시글 목록
    @GetMapping("/jayu/list")
    public String list(Model model, PageRequestDTO pageRequestDTO, @AuthenticationPrincipal PrincipalDetails principal) {
        //summernote 태그 제거, 게시판 정보
        PageResultDTO<JayuBoardDTO, Object[]> jayuList = jayuBoardService.getList(pageRequestDTO);
        List<JayuBoardDTO> list = jayuList.getDtoList();
        for (int i = 0; i < list.size(); i++) {
            JayuBoardDTO tmp = (JayuBoardDTO) list.get(i);
            tmp.setContent(tmp.getContent().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", ""));
            list.set(i, tmp);
        }
        jayuList.setDtoList((List<JayuBoardDTO>) list);

        model.addAttribute("jayuList",jayuList);

        //카테고리
        model.addAttribute("cateList",jayuCateService.getCateList());

        //위시리스트
        if (principal != null) {
            List<MemberWishList> wishLists = wishListService.getListByMno(principal.getMember().getMno());
            model.addAttribute("wishList", wishListService.getListByMno(principal.getMember().getMno()));
            if (wishLists.size() != 0) {
                model.addAttribute("wishListIndex", wishLists.get(0));
            }
        }

        return "/jayu/list";
    }

    //펑예 목록
    @GetMapping("/jayu/peong")
    public String peong(Model model, PageRequestDTO pageRequestDTO, @AuthenticationPrincipal PrincipalDetails principal) {
        //summernote 태그 제거, 게시판 정보
        PageResultDTO<JayuBoardDTO, Object[]> jayuList = jayuBoardService.getPeongList(pageRequestDTO);
        List<JayuBoardDTO> list = jayuList.getDtoList();
        for (int i = 0; i < list.size(); i++) {
            JayuBoardDTO tmp = (JayuBoardDTO) list.get(i);
            tmp.setContent(tmp.getContent().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", ""));
            list.set(i, tmp);
        }
        jayuList.setDtoList((List<JayuBoardDTO>) list);

        model.addAttribute("jayuList",jayuList);

        //카테고리
        model.addAttribute("cateList",jayuCateService.getCateList());

        //위시리스트
        if (principal != null) {
            List<MemberWishList> wishLists = wishListService.getListByMno(principal.getMember().getMno());
            model.addAttribute("wishList", wishListService.getListByMno(principal.getMember().getMno()));
            if (wishLists.size() != 0) {
                model.addAttribute("wishListIndex", wishLists.get(0));
            }
        }

        return "/jayu/peong";
    }

    //게시글 수정 페이지
    @GetMapping("/member/jayu/modify")
    public String modify(Long jayuNo, Model model){
        //카테고리
        model.addAttribute("cateList",jayuCateService.getCateList());
        //게시판 정보
        model.addAttribute("jayuBoardDTO", jayuBoardService.read(jayuNo));

        return "/jayu/modify";
    }

    //게시글 수정
    @PostMapping("/member/jayu/modify")
    public String modify(JayuBoardDTO jayuBoardDTO, RedirectAttributes redirectAttributes,
                         @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO){
        Long jayuNo = jayuBoardDTO.getJayuNo();
        jayuBoardService.modify(jayuBoardDTO);
        redirectAttributes.addAttribute("jayuNo", jayuNo);
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());

        return "redirect:/member/jayu/read?jayuNo="+jayuNo;
    }

    //게시글 삭제
    @GetMapping("/member/jayu/remove")
    public String remove(JayuBoardDTO jayuBoardDTO, @AuthenticationPrincipal PrincipalDetails principal){
        Long mno = principal.getMember().getMno();

        jayuBoardService.remove(jayuBoardDTO, mno);

        return "redirect:/jayu/list";
    }
}