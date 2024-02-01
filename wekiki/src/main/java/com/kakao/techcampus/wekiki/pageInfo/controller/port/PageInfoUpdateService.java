package com.kakao.techcampus.wekiki.pageInfo.controller.port;

import com.kakao.techcampus.wekiki.pageInfo.controller.response.PageInfoResponse;

public interface PageInfoUpdateService {
    PageInfoResponse.likePageDTO likePage(Long pageId , Long groupId, Long memberId);
    PageInfoResponse.hatePageDTO hatePage(Long pageId , Long groupId, Long memberId);

}
