package com.kakao.techcampus.wekiki._core.facade;

import com.kakao.techcampus.wekiki.page.service.PageConcurrencyService;
import com.kakao.techcampus.wekiki.pageInfo.service.port.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NamedLockFacadeTest {

    private final PageConcurrencyService pageConcurrencyService;
    private final PageRepository pageRepository;


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
