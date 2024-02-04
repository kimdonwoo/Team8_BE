package com.kakao.techcampus.wekiki._core.facade;

import com.kakao.techcampus.wekiki._core.utils.port.RedisUtils;
import com.kakao.techcampus.wekiki.page.service.PageConcurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockFacadeTest {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private PageConcurrencyService pageConcurrencyService;


    public void likePageWithLettuceLock(Long pageId) throws InterruptedException {
        while (!redisUtils.lock(pageId)) {
            Thread.sleep(100);
        }

        try {
            pageConcurrencyService.likePageWithNamedLockAndLettuceLock(pageId);
        } finally {
            redisUtils.unlock(pageId);
        }
    }


}
