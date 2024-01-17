package com.kakao.techcampus.wekiki._core.facade;

import com.kakao.techcampus.wekiki.page.service.PageService;
import com.kakao.techcampus.wekiki.page.service.port.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NamedLockFacade {

    private final PageService pageService;
    private final PageRepository pageRepository;


    @Transactional
    public void likePageWithNamedLock(Long pageId) {
        try {
            pageRepository.getLock(pageId.toString());
            pageService.likePageWithNamedLockAndLettuceLock(pageId);
        } finally {
            pageRepository.releaseLock(pageId.toString());
        }
    }

}
