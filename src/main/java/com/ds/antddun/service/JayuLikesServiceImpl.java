package com.ds.antddun.service;

import com.ds.antddun.entity.JayuLikes;
import com.ds.antddun.repository.JayuLikesRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Log4j2
public class JayuLikesServiceImpl implements JayuLikesService {

    @Autowired
    private JayuLikesRepository jayuLikesRepository;

    @Transactional
    @Override
    public void addLikes(Long jayuNo, Long mno) {
        jayuLikesRepository.addLikes(jayuNo, mno);
    }

    @Transactional
    @Override
    public void deleteLikes(Long jayuNo, Long mno)  {
        jayuLikesRepository.deleteLikes(jayuNo, mno);
    }

    @Override
    public JayuLikes checkLikes(Long jayuNo, Long mno) {
        return jayuLikesRepository.checkLikeByBnoAndMno(jayuNo, mno);
    }

    @Override
    public int countLikes(Long jayuNo) {
        return jayuLikesRepository.countLikes(jayuNo);
    }
}
