package com.ds.antddun.service;

import com.ds.antddun.entity.JayuLikes;

public interface JayuLikesService {

    void addLikes(Long jayuNo, Long mno);

    void deleteLikes(Long jayuNo, Long mno);

    JayuLikes checkLikes(Long jayuNo, Long mno);

    int countLikes(Long jayuNo);

}
