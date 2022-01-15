package com.ds.antddun.controller;

import com.ds.antddun.config.auth.PrincipalDetails;
import com.ds.antddun.entity.JayuLikes;
import com.ds.antddun.repository.JayuLikesRepository;
import com.ds.antddun.service.JayuLikesService;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/jayu")
public class JayuLikesController {

    @Autowired
    private JayuLikesService jayuLikesService;

    @Autowired
    private JayuLikesRepository jayuLikesRepository;

    @PostMapping("/likes/{jayuNo}")
    public JSONObject addLikes(@PathVariable Long jayuNo, @AuthenticationPrincipal PrincipalDetails principal)  {

        Long mno = principal.getMember().getMno();
        JSONObject result = new JSONObject();

        JayuLikes target = jayuLikesService.checkLikes(jayuNo, mno);
        if(target != null){ //있으면!!
            jayuLikesService.deleteLikes(jayuNo, mno); //좋아요 해제 (row 삭제)
            result.put("response", null);
            result.put("likesCnt", jayuLikesRepository.countLikes(jayuNo)); //그때 그때 불러서 보여줘야함 (변수 사용 x)
            return result;
        } else {
            jayuLikesService.addLikes(jayuNo, mno);  //좋아요 (row 추가)
            result.put("likesCnt", jayuLikesRepository.countLikes(jayuNo));
            return result;
        }
    }

}
