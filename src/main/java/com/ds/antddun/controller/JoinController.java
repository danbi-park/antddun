package com.ds.antddun.controller;

import com.ds.antddun.dto.JobListDTO;
import com.ds.antddun.dto.MemberDTO;
import com.ds.antddun.service.JobListService;
import com.ds.antddun.service.MemberService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@Log4j2
@RequestMapping({"", "/"})
@SessionAttributes("JOIN_USER")
public class JoinController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private JobListService jobListService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping({"", "/"})
    public String index() {
        return "/index";
    }

    @GetMapping("/user")
    public @ResponseBody
    String user() {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody
    String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody
    String manager() {
        return "manager";
    }

    @ResponseBody
    @PostMapping("/idCheck")
    public int idCheck(String email) throws Exception {
        int result = memberService.idCheck(email);
        log.info("idDuplicateCheck >>>>>>>>>>>>>>>>>>>>>>> " + result);
        return result;
    }

    @ResponseBody
    @PostMapping("/phoneNumCheck")
    public int mobileCheck(String phoneNum) throws Exception {
        int result = memberService.mobileCheck(phoneNum);
        log.info("phoneNumDuplicateCheck >>>>>>>>>>>>>>>>>>>>>>> " + result);
        return result;
    }

    @ResponseBody
    @PostMapping("/recommendUserCheck")
    public boolean recommendUserCheck(@RequestBody String recommendUser) {
        boolean result = false;

        result = memberService.recommendUserCheck(recommendUser);
        log.info("recommendUserCheck >>>>>>>>>>>>>>> " + result);
        return result;
    }

    @GetMapping("/joinPage")
    public String joinPage(Model model, JobListDTO jobListDTO) {

        model.addAttribute("jobList", jobListService.getList());
        log.info("LIST>>>>>" + jobListService.getList());

        return "/member/join";
    }

    @GetMapping("/joinAgree")
    public String joinAgree() {
        return "/member/joinAgree";
    }

    @PostMapping(value = "/joinPage", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String join(@RequestBody MemberDTO memberDTO,
                       @RequestBody JobListDTO jobListDTO,
                       String username,
                       String phoneNum,
                       HttpSession httpSession) throws Exception {

        int idDuplicateCheck = memberService.idCheck(username);
        int mobileDuplicateCheck = memberService.mobileCheck(phoneNum);

        httpSession.setAttribute("JOIN_USER", memberService.welcomeMsg(memberDTO.getMno()));

        log.info("ID>>>>>>>>>>>>>>>>>>>>>>>>" + idDuplicateCheck);
        log.info("MOBILE>>>>>>>>>>>>>>>>>>>>>>>>" + mobileDuplicateCheck);

        if (idDuplicateCheck != 0 || mobileDuplicateCheck != 0) {
            return "redirect:/joinPage";
        } else {
            memberService.join(memberDTO, jobListDTO);
            log.info("MNOMNO>>>>>>>" + memberDTO.getMno());
        }

        return "/member/welcome";
    }

    // select가 필요 -> 어떤 식으로 mno를 받아오지???ㅏ

    @GetMapping(value="/joinWelcome", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String welcomeMsg(@ModelAttribute("JOIN_USER") MemberDTO memberDTO, Model model) {
//        Long result = memberDTO.getMno();
        log.info("MNO >>>>>>>>" + memberDTO.getMno());
        model.addAttribute("JOIN_USER", memberService.welcomeMsg(memberDTO.getMno()));
        return "/member/welcome";
    }
}
