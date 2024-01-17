package com.kakao.techcampus.wekiki._core.facade;

import com.kakao.techcampus.wekiki._core.utils.RedisUtils;
import com.kakao.techcampus.wekiki.page.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LettuceLockFacade {

    private final RedisUtils redisUtils;
    private final PageService pageService;


    public void likePageWithLettuceLock(Long pageId) throws InterruptedException {
        while (!redisUtils.lock(pageId)) {
            Thread.sleep(100);
        }

        try {
            pageService.likePageWithNamedLockAndLettuceLock(pageId);
        } finally {
            redisUtils.unlock(pageId);
        }
    }


}
