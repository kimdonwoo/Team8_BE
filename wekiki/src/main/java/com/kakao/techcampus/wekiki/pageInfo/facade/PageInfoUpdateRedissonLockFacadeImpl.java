package com.kakao.techcampus.wekiki.pageInfo.facade;

import com.kakao.techcampus.wekiki.pageInfo.controller.port.PageInfoUpdateRedissonLockFacade;
import com.kakao.techcampus.wekiki.pageInfo.controller.port.PageInfoUpdateService;
import com.kakao.techcampus.wekiki.pageInfo.controller.response.PageInfoResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Builder
public class PageInfoUpdateRedissonLockFacadeImpl implements PageInfoUpdateRedissonLockFacade {

    private final RedissonClient redissonClient;

    private final PageInfoUpdateService pageInfoUpdateService;

    public PageInfoResponse.likePageDTO likePageWithRedissonLock(Long pageId , Long groupId, Long memberId){
        RLock lock = redissonClient.getLock(pageId.toString()+"like");

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!available) {
                System.out.println("lock 획득 실패");
                return null;
            }

            return pageInfoUpdateService.likePage(pageId,groupId,memberId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public PageInfoResponse.hatePageDTO hatePageWithRedissonLock(Long pageId , Long groupId, Long memberId){
        RLock lock = redissonClient.getLock(pageId.toString()+"hate");

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!available) {
                System.out.println("lock 획득 실패");
                return null;
            }

            return pageInfoUpdateService.hatePage(pageId,groupId,memberId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

}
