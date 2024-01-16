package com.kakao.techcampus.wekiki._core.facade;


import com.kakao.techcampus.wekiki.domain.page.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptimisticLockFacade {

    private final PageService pageService;

    public void likePageWithOptimisticLock(Long pageId) throws InterruptedException {
        while (true) {
            try {
                pageService.likePageWithOptimisticLock(pageId);

                break;
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }

}
