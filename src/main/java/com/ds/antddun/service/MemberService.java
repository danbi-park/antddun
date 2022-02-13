package com.ds.antddun.service;

import com.ds.antddun.dto.JobListDTO;
import com.ds.antddun.dto.MemberDTO;
import com.ds.antddun.entity.AntMemberRoleSet;
import com.ds.antddun.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    String findByPhoneNum(String phoneNum);

    Long join(MemberDTO memberDTO, JobListDTO jobListDTO);

    //소셜로그인 후 추가 정보 기입
    void socialJoin(MemberDTO memberDTO, JobListDTO jobListDTO);

    int idCheck(String username) throws Exception;

    int mobileCheck(String mobile) throws Exception;

    boolean recommendUserCheck(String recommendUser);

    Member welcomeMsg(Long mno);

    void modifyMember(MemberDTO memberDTO);

    void certifiedPhoneNumber(String phoneNum, String numStr);

    default Member dtoToEntity(MemberDTO memberDTO) {
        Member member = Member.builder()
                .mno(memberDTO.getMno())
                .username(memberDTO.getUsername())
                .password(memberDTO.getPassword())
                .firstName(memberDTO.getFirstName())
                .lastName(memberDTO.getLastName())
                .phoneNum(memberDTO.getPhoneNum())
                .salary(memberDTO.getSalary())
                .createDate(memberDTO.getCreateDate())
//                .recommendUser(memberDTO.getRecommendUser())
                .build();
        return member;
    }

    default MemberDTO entityToDTO(Member member) {
        MemberDTO memberDTO = MemberDTO.builder()
                .mno(member.getMno())
                .username(member.getUsername())
                .password(member.getPassword())
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .phoneNum(member.getPhoneNum())
                .experience(member.getExperience())
//                .recommendUser(member.getRecommendUser())
                .salary(member.getSalary())
                .build();
        return memberDTO;
    }

    //소셜 로그인 후 추가정보 업데이트
    default Member socialDtoToEntity(MemberDTO memberDTO) {

        Member member = Member.builder()
                .mno(memberDTO.getMno())
                .username(memberDTO.getUsername())
                .password(memberDTO.getPassword())
                .firstName(memberDTO.getFirstName())
                .lastName(memberDTO.getLastName())
                .phoneNum(memberDTO.getPhoneNum())
                .role(AntMemberRoleSet.USER)
                .experience(memberDTO.getExperience())
                .salary(memberDTO.getSalary())
                .startTime(memberDTO.getStartTime())
                .endTime(memberDTO.getEndTime())
                .fromSocial(true)
                .build();
        return member;
    }
}

