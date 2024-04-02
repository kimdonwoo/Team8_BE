package com.kakao.techcampus.wekiki.post.facade;

import com.kakao.techcampus.wekiki._core.error.exception.Exception500;
import com.kakao.techcampus.wekiki.pageInfo.controller.port.PageInfoUpdateService;
import com.kakao.techcampus.wekiki.post.controller.port.PostUpdateRedissonLockFacade;
import com.kakao.techcampus.wekiki.post.controller.port.PostUpdateService;
import com.kakao.techcampus.wekiki.post.controller.response.PostResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
@RequiredArgsConstructor
@Builder
public class PostUpdateRedissonLockFacadeImpl implements PostUpdateRedissonLockFacade {

    private final RedissonClient redissonClient;
    private final PostUpdateService postUpdateService;

    @Override
    public PostResponse.modifyPostDTO modifyPostWithRedissonLock(Long memberId, Long groupId, Long postId, String title, String content) {

        RLock lock = redissonClient.getLock(postId.toString()+"postModify");

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!available) {
                throw new Exception500("현재 게시글 수정이 불가능합니다.");
            }
            return postUpdateService.modifyPost(memberId, groupId, postId, title, content);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

    }
}
