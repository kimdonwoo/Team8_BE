package com.kakao.techcampus.wekiki._core.facade;

import com.kakao.techcampus.wekiki.page.service.PageConcurrencyService;
import com.kakao.techcampus.wekiki.pageInfo.service.port.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NamedLockFacadeTest {

    @Autowired
    private PageConcurrencyService pageConcurrencyService;
    @Autowired
    private PageRepository pageRepository;


    @Transactional
    public void likePageWithNamedLock(Long pageId) {
        try {
            pageRepository.getLock(pageId.toString());
            pageConcurrencyService.likePageWithNamedLockAndLettuceLock(pageId);
        } finally {
            pageRepository.releaseLock(pageId.toString());
        }
    }

}
