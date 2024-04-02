package com.kakao.techcampus.wekiki.pageInfo.controller.port;

import com.kakao.techcampus.wekiki.pageInfo.controller.response.PageInfoResponse;

public interface PageInfoUpdateRedissonLockFacade {
    PageInfoResponse.likePageDTO likePageWithRedissonLock(Long pageId , Long groupId, Long memberId);
    PageInfoResponse.hatePageDTO hatePageWithRedissonLock(Long pageId , Long groupId, Long memberId);

}
