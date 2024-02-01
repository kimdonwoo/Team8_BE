package com.kakao.techcampus.wekiki.pageInfo.controller.port;

import com.kakao.techcampus.wekiki.pageInfo.controller.response.PageInfoResponse;

public interface PageInfoCreateService {
    PageInfoResponse.createPageDTO createPage(String title, Long groupId, Long memberId);

}
