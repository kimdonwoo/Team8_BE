package com.kakao.techcampus.wekiki._core.facade;

import com.kakao.techcampus.wekiki.page.service.PageConcurrencyService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedissonLockFacadeTest {

    private final RedissonClient redissonClient;

    private final PageConcurrencyService pageConcurrencyService;

    public void likePageWithRedissonLock(Long pageId){
        RLock lock = redissonClient.getLock(pageId.toString());

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!available) {
                System.out.println("lock 획득 실패");
                return;
            }

            pageConcurrencyService.likePageWithNamedLockAndLettuceLock(pageId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

}
