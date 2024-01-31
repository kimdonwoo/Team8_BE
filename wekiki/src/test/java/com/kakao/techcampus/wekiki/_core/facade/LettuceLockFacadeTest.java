package com.kakao.techcampus.wekiki._core.facade;

import com.kakao.techcampus.wekiki._core.utils.port.RedisUtils;
import com.kakao.techcampus.wekiki.page.service.PageConcurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LettuceLockFacadeTest {

    private final RedisUtils redisUtils;
    private final PageConcurrencyService pageConcurrencyService;


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
