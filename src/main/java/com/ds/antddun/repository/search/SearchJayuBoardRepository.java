package com.ds.antddun.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchJayuBoardRepository {

    Page<Object[]> search(int cate, String type, String keyword, Pageable pageable);
}
