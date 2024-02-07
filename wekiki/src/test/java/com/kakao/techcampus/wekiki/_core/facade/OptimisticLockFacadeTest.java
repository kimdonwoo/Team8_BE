package com.kakao.techcampus.wekiki._core.facade;


import com.kakao.techcampus.wekiki.page.service.PageConcurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OptimisticLockFacadeTest {

    @Autowired
    private PageConcurrencyService pageConcurrencyService;

    public void likePageWithOptimisticLock(Long pageId) throws InterruptedException {
        while (true) {
            try {
                pageConcurrencyService.likePageWithOptimisticLock(pageId);

                break;
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
                System.out.println(e.getMessage());
                System.out.println("흠..");
                Thread.sleep(500);
            }
        }
    }

}
