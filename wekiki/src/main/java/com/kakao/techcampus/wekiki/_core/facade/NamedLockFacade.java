package com.kakao.techcampus.wekiki._core.facade;

import com.kakao.techcampus.wekiki.domain.page.PageJPARepository;
import com.kakao.techcampus.wekiki.domain.page.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NamedLockFacade {

    private final PageService pageService;
    private final PageJPARepository pageJPARepository;


    @Transactional
    public void likePageWithNamedLock(Long pageId) {
        try {
            pageJPARepository.getLock(pageId.toString());
            pageService.likePageWithNamedLockAndLettuceLock(pageId);
        } finally {
            pageJPARepository.releaseLock(pageId.toString());
        }
    }

}
