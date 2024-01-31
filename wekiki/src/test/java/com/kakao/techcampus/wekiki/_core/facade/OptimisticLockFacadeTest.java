package com.kakao.techcampus.wekiki._core.facade;


import com.kakao.techcampus.wekiki.page.service.PageConcurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptimisticLockFacadeTest {

    private final PageConcurrencyService pageConcurrencyService;

    public void likePageWithOptimisticLock(Long pageId) throws InterruptedException {
        while (true) {
            try {
                pageConcurrencyService.likePageWithOptimisticLock(pageId);

                break;
            } catch (Exception e) {
                Thread.sleep(500);
            }
        }
    }

}
