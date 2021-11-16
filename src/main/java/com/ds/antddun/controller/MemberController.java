package com.ds.antddun.controller;

import com.ds.antddun.config.auth.PrincipalDetails;
import com.ds.antddun.dto.MemberDTO;
import com.ds.antddun.service.JobListService;
import com.ds.antddun.service.MemberService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@Log4j2
public class MemberController {
    //추가
    private MemberService memberService;

    @Autowired
    private JobListService jobListService;


//    @GetMapping("/member/mypage")
//    public String wallet() {
//        return "/member/mypage";
//    }
//    //    @PutMapping("member/updateMember")
//    //    public

    @GetMapping("")
    public String main (Model model, MemberDTO memberDTO, @AuthenticationPrincipal PrincipalDetails principal) {
        if(principal != null) {
            model.addAttribute("member", principal.getMember());
        }
        return "index";
    }

    @GetMapping("/member/mypage")
    public String userinfo (Model model, MemberDTO memberDTO, @AuthenticationPrincipal PrincipalDetails principal) {
        if(principal != null) {
            model.addAttribute("member", principal.getMember());
            model.addAttribute("jobList", jobListService.getList());
        }
        return "member/mypage";
    }

    @PostMapping("/member/mypage")
    public String wishListSave() {
        // 동일한 name 속성을 가진 데이터는 배열 형태로 Controller로 넘어온다. split(",")을 이용해 잘라서 사용.'
        return "";
    }

    @GetMapping("/member/messenger")
    public String messenger (Model model, MemberDTO memberDTO, @AuthenticationPrincipal PrincipalDetails principal) {
        if(principal != null) {
            model.addAttribute("member", principal.getMember());
            model.addAttribute("jobList", jobListService.getList());
        }
        return "member/messenger";
    }
}