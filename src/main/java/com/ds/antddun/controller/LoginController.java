package com.ds.antddun.controller;

import com.ds.antddun.config.auth.PrincipalDetailsService;
import com.ds.antddun.repository.MemberRepository;
import com.ds.antddun.service.MemberService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Log4j2
@SessionAttributes("member")
public class LoginController {
    @Autowired
    private MemberService service;

    @Autowired
    private MemberService memberService;


    @Autowired
    private PrincipalDetailsService principalDetailsService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/user")
    public void user() {

    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/login")
    public String login(String error, String logout, Model model, HttpServletRequest request
            ,HttpSession session){

        String referer = request.getHeader("Referer");
        request.getSession().setAttribute("redirectURI",referer);

        if (error != null) {
            model.addAttribute("error", "Login Error Check Your Account");
        } else if (logout != null) {
            model.addAttribute("logout", "Logout!");
        } else {
            model.addAttribute("session", session.getId());

            log.info("session>>>>>>>>>>>>"+session.getId());
        }

        return "/member/login";
    }

    @GetMapping("/findIdForm")
    public String findId(){
        return "/member/findIdForm";
    }

    @ResponseBody
    @PostMapping("/findUsername")
    public String getPhoneNum(String phoneNum) throws Exception {
        String username = memberService.findByPhoneNum(phoneNum);
        log.info("findUsername>>>>>>>>>>>>>>>>>>>>>>> " + username);
        if (username == null) {
            return "해당 번호로 가입된 이메일이 없습니다.";
        }
        return username;
    }

}