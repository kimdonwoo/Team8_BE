package com.kakao.techcampus.wekiki.page.service;

import com.kakao.techcampus.wekiki._core.error.exception.Exception404;
import com.kakao.techcampus.wekiki.pageInfo.domain.PageInfo;
import com.kakao.techcampus.wekiki.pageInfo.service.port.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PageConcurrencyWithSync {

    @Autowired
    private PageRepository pageRepository;

    @Transactional
    public void likePageWithSynchronized(Long pageId){
        PageInfo page = pageRepository.findById(pageId).orElseThrow(() -> new Exception404("존재하지 않는 페이지 입니다."));
        PageInfo newPage = page.plusGoodCount();
        pageRepository.update(newPage);
    }

}
