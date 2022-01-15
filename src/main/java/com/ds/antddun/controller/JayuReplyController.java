package com.ds.antddun.controller;


import com.ds.antddun.config.auth.PrincipalDetails;
import com.ds.antddun.dto.JayuReplyDTO;
import com.ds.antddun.service.JayuReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/jayu")
@Log4j2
@RequiredArgsConstructor
public class JayuReplyController {
    private final JayuReplyService jayuReplyService;

    @PostMapping("/reply/register")
    //Json(application/json)형태의 HTTP Body 내용을 Java Object로 변환시켜주는 역할
    public ResponseEntity<Long> register(@RequestBody JayuReplyDTO jayuReplyDTO,
                                         @AuthenticationPrincipal PrincipalDetails principal ){
        jayuReplyDTO.setMno(principal.getMember().getMno());
        Long jayuRno = jayuReplyService.register(jayuReplyDTO);

        return new ResponseEntity<>(jayuRno, HttpStatus.OK);
    }

    @PostMapping("/reply/modify/{jayuRno}")
    public ResponseEntity<String> modify(@PathVariable("jayuRno") Long jayuRno,
                                         @RequestBody JayuReplyDTO jayuReplyDTO){
        jayuReplyService.modify(jayuReplyDTO);

        return new ResponseEntity<>("modify",HttpStatus.OK);
    }

    @DeleteMapping("/reply/remove/{jayuRno}")
    public ResponseEntity<String> remove(@PathVariable("jayuRno") Long jayuRno){
        jayuReplyService.replyRemove(jayuRno);

        return new ResponseEntity<>("remove", HttpStatus.OK);
    }
}
