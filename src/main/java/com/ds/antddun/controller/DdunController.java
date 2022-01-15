package com.ds.antddun.controller;

import com.ds.antddun.config.auth.PrincipalDetails;
import com.ds.antddun.dto.DdunDTO;
import com.ds.antddun.service.DdunService;
import com.ds.antddun.service.QnaService;
import com.ds.antddun.service.SosoJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class DdunController {

    private final DdunService ddunService;
    private final SosoJobService sosoJobService;
    private final QnaService qnaService;

    /* 뚠 충전 */
    @ResponseBody
    @GetMapping("/mypage/wallet/save")
    public void walletSave(Long amount, DdunDTO ddunDTO, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        ddunDTO.setInputAmount(amount);
        ddunDTO.setMember(principalDetails.getMember().getMno());
        ddunDTO.setOutputAmount(0L);
        ddunDTO.setContent("뚠 충전");

        ddunService.saveDdun(ddunDTO);
    }
    /* 소소잡 뚠 거래 */
    @ResponseBody
    @GetMapping("/sosojob/buy/{sosoNo}/{mno}")
    public ResponseEntity<String> sosoBuy(@PathVariable("sosoNo") Long sosoNo,
                                  @PathVariable("mno") Long mno,
                                  @AuthenticationPrincipal PrincipalDetails principalDetails,
                                  @RequestParam("amount") Long amount,
                                  DdunDTO ddunDTO) {

        String contentName = sosoJobService.read(sosoNo).getContent();
        Long buyer = ddunService.totalAmountByMno(principalDetails.getMember().getMno());

        if (buyer >= amount) {
            ddunDTO.setContent(contentName);
            ddunService.sosoBuy(principalDetails.getMember(), amount, ddunDTO);
            ddunService.sosoSell(mno, amount, ddunDTO);

            return new ResponseEntity<>("구매 성공", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("뚠 충전 필요", HttpStatus.OK);
        }
    }

    /* QnA 뚠 거래 */
    @ResponseBody
    @GetMapping("/qna/betDdun")
    public String qnaBetDdun(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                             @RequestParam("title") String title,
                                             @RequestParam("amount") Long amount,
                                             @RequestParam("totalDdun") Long totalDdun,
                                             DdunDTO ddunDTO) {
        if ( totalDdun < amount ) {
            return "뚠 부족";
        } else {
            ddunDTO.setContent(title);
            ddunService.sosoBuy(principalDetails.getMember(), amount, ddunDTO);
            return "등록 성공";
        }
    }

    @ResponseBody
    @PostMapping("/qna/selected")
    public ResponseEntity<String> selectAnswer(@RequestBody JSONObject param,
                                               DdunDTO ddunDTO) {
        String title = param.getAsString("title");
        Long amount = Long.parseLong(param.getAsString("amount"));
        Long qnaNo = Long.parseLong(param.getAsString("qnaNo"));
        Long replyNo = Long.parseLong(param.getAsString("replyNo"));
        Long replier = Long.parseLong(param.getAsString("replier"));

        ddunDTO.setContent(title);
        ddunService.sosoSell(replier, amount, ddunDTO);
        qnaService.setSolvedSelected(qnaNo, replyNo);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}