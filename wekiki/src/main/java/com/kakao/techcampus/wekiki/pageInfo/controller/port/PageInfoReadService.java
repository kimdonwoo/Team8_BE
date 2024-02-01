package com.kakao.techcampus.wekiki.pageInfo.controller.port;

import com.kakao.techcampus.wekiki.pageInfo.controller.response.PageInfoResponse;

public interface PageInfoReadService {
    PageInfoResponse.getPageIndexDTO getPageIndex(Long groupId, Long memberId, Long pageId);
    PageInfoResponse.getPageFromIdDTO getPageFromId(Long memberId,Long groupId, Long pageId);
    PageInfoResponse.searchPageDTO searchPage(Long groupId, Long memberId, int pageNo, String keyword);
    PageInfoResponse.getRecentPageDTO getRecentPage(Long memberId , Long groupId);
    PageInfoResponse.getPageFromIdDTO getPageFromTitle(Long memberId, Long groupId, String title);
    PageInfoResponse.getPageLinkDTO getPageLink(Long groupId, String title);
    PageInfoResponse.mainPageDTO getMainPage();
}
