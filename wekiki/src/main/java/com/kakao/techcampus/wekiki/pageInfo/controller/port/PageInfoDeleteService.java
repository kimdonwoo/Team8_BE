package com.kakao.techcampus.wekiki.pageInfo.controller.port;

import com.kakao.techcampus.wekiki.pageInfo.controller.response.PageInfoResponse;

public interface PageInfoDeleteService {
    PageInfoResponse.deletePageDTO deletePage(Long memberId, Long groupId, Long pageId);

}
