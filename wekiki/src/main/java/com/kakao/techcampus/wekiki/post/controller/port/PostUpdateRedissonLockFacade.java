package com.kakao.techcampus.wekiki.post.controller.port;

import com.kakao.techcampus.wekiki.post.controller.response.PostResponse;

public interface PostUpdateRedissonLockFacade {
    PostResponse.modifyPostDTO modifyPostWithRedissonLock(Long memberId , Long groupId, Long postId , String title, String content);
}
