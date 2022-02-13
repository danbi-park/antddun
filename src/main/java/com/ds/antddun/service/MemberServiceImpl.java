package com.ds.antddun.service;

import com.ds.antddun.dto.JobListDTO;
import com.ds.antddun.dto.MemberDTO;
import com.ds.antddun.entity.AntMemberRoleSet;
import com.ds.antddun.entity.JobList;
import com.ds.antddun.entity.Member;
import com.ds.antddun.repository.JobListRepository;
import com.ds.antddun.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JobListRepository jobListRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String findByPhoneNum(String phoneNum) {
        String username = memberRepository.findByPhoneNum(phoneNum);
        return username;
    }

    /* 회원가입 */
    @Override
    public Long join(MemberDTO memberDTO, JobListDTO jobListDTO) {

        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        JobList jobList = jobListRepository.findById(jobListDTO.getJno()).get();

        return memberRepository.save(Member.builder()
                .mno(memberDTO.getMno())
                .username(memberDTO.getUsername())
                .password(memberDTO.getPassword())
                .firstName(memberDTO.getFirstName())
                .lastName(memberDTO.getLastName())
                .phoneNum(memberDTO.getPhoneNum())
                .salary(memberDTO.getSalary())
                .startTime(memberDTO.getStartTime())
                .endTime(memberDTO.getEndTime())
                .role(AntMemberRoleSet.USER)
                .job(jobList)
                .experience(memberDTO.getExperience())
                .fromSocial(false)
                .createDate(memberDTO.getCreateDate())
                .build()).getMno();

    }

    /* 회원정보 수정 */
    @Transactional
    @Override
    public void modifyMember(MemberDTO memberDTO) {
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        log.info("S.MODIFY.M >>> " + memberDTO);
        JobList jobList = jobListRepository.findById(Integer.parseInt(memberDTO.getJob())).get();
        log.info("S.MODIFY.J >>> " + jobList);

        memberRepository.save(Member.builder()
                .mno(memberDTO.getMno())
                .password(memberDTO.getPassword())
                .username(memberDTO.getUsername())
                .phoneNum(memberDTO.getPhoneNum())
                .firstName(memberDTO.getFirstName())
                .lastName(memberDTO.getLastName())
                .job(jobList)
                .experience(memberDTO.getExperience())
                .salary(memberDTO.getSalary())
                .role(AntMemberRoleSet.USER)
                .startTime(memberDTO.getStartTime())
                .endTime(memberDTO.getEndTime())
                .fromSocial(false)
                .createDate(memberDTO.getCreateDate())
                .build());
    }

    /*
     * 아이디 중복 체크
     * */
    @Override
    public int idCheck(String username) throws Exception {

        int result = 0;

        result = memberRepository.idCheck(username);

        return result;
    }

    /*
     * 연락처 중복 체크
     * */
    @Override
    public int mobileCheck(String phoneNum) throws Exception {

        int result = 0;

        result = memberRepository.phoneNumCheck(phoneNum);

        return result;
    }

    /*
     * 추천인 확인
     * */
    @Override
    public boolean recommendUserCheck(String recommendUser) {

        boolean result = false;
        int userCnt = memberRepository.recommendUserCheck(recommendUser);

        if (userCnt >= 1) {
            result = true;
            return result;
        } else if (userCnt == 0) {
            result = false;
            return result;
        }
        return false;
    }

    /*
     * 시간 변환
     * */

    @Override
    public Member welcomeMsg(Long mno) {
//        Member member = Member.builder().mno(mno).build();
        Member result = memberRepository.welcomeMsg(mno);
//        List<Member> result = memberRepository.welcomeMsg(mno);
        return result;
    }

    @Transactional
    @Override
    public void socialJoin(MemberDTO memberDTO, JobListDTO jobListDTO) {

        JobList jobList = jobListRepository.getById(jobListDTO.getJno());
        Member member = socialDtoToEntity(memberDTO);
        member.setMno(memberDTO.getMno()); //update되도록 mno알려주기
        member.setJob(jobList); //Member에 setter 추가해줌
        memberRepository.save(member);
    }

    @Override
    public void certifiedPhoneNumber(String phoneNum, String cerNum) {

        String api_key = "NCSG3RPOMGHEDQIL";
        String api_secret = "YHQLZ2W26BAMRUYGFVLULFGI08HJTT6K";
        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<>();
        params.put("to", phoneNum);    // 수신전화번호
        params.put("from", "01071570271");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "개미는 뚠뚠의 휴대폰인증 테스트 메시지 : 인증번호는" + "["+cerNum+"]" + "입니다.");
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }

    }


}
