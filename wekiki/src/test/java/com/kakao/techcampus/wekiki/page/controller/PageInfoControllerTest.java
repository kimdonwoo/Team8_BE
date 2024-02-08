package com.kakao.techcampus.wekiki.page.controller;

import com.kakao.techcampus.wekiki._core.utils.ApiUtils;
import com.kakao.techcampus.wekiki.mock.TestContainer;
import com.kakao.techcampus.wekiki.pageInfo.controller.request.PageInfoRequest;
import com.kakao.techcampus.wekiki.pageInfo.controller.response.PageInfoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class PageInfoControllerTest {


    /**
     *  1. 사용자는_올바른_요청에_대해_pageId로_pageInfo와_Post_조회가_가능하다()
     *
     *  2. 사용자는_올바른_요청에_대해_pageInfo_생성이_가능하다()
     *
     *  3. 사용자는_올바른_요청에_대해_pageInfo_삭제가_가능하다()
     *
     *  4. 사용자는_올바른_요청에_대해_pageInfo_좋아요가_가능하다
     *
     *  5. 사용자는_올바른_요청에_대해_pageInfo_싫어요가_가능하다
     *
     *  6. 사용자는_올바른_요청에_대해_pageInfo_목차_조회가_가능하다
     *
     *  7. 사용자는_올바른_요청에_대해_pageInfo_제목으로_조회가_가능하다
     *
     *  8. 사용자는_올바른_요청에_대해_키워드로_pageInfo_검색이_가능하다
     *
     *  9. 사용자는_올바른_요청에_대해_최근_바뀐_pageInfo_조회가_가능하다
     *
     *  10. 사용자는_올바른_요청에_대해_페이지명으로_링크를위한_pageId_조회가_가능하다
     *
     */

    @Test
    void 사용자는_올바른_요청에_대해_pageId로_pageInfo와_Post_조회가_가능하다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testPageInfoControllerSetting();
        Long groupId = 1L;
        Long pageId = 1L;

        // when
        ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.getPageFromIdDTO>> result = testContainer.pageInfoController.getPageFromId(groupId, pageId);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getResponse().getPageId()).isEqualTo(1L);
        assertThat(result.getBody().getResponse().getPageName()).isEqualTo("Test Page");
        assertThat(result.getBody().getResponse().getBadCount()).isEqualTo(0);
        assertThat(result.getBody().getResponse().getGoodCount()).isEqualTo(0);

        assertThat(result.getBody().getResponse().getPostList().get(0).getPostId()).isEqualTo(1L);
        assertThat(result.getBody().getResponse().getPostList().get(0).getIndex()).isEqualTo("1");
        assertThat(result.getBody().getResponse().getPostList().get(0).getParentPostId()).isEqualTo(0L);
        assertThat(result.getBody().getResponse().getPostList().get(0).getOrder()).isEqualTo(0);
        assertThat(result.getBody().getResponse().getPostList().get(0).getPostTitle()).isEqualTo("Test Post1");
        assertThat(result.getBody().getResponse().getPostList().get(0).getContent()).isEqualTo("해당 포스트는 테스트용 포스트1입니다.");

        assertThat(result.getBody().getResponse().getPostList().get(1).getPostId()).isEqualTo(2L);
        assertThat(result.getBody().getResponse().getPostList().get(1).getIndex()).isEqualTo("2");
        assertThat(result.getBody().getResponse().getPostList().get(1).getParentPostId()).isEqualTo(0L);
        assertThat(result.getBody().getResponse().getPostList().get(1).getOrder()).isEqualTo(1);
        assertThat(result.getBody().getResponse().getPostList().get(1).getPostTitle()).isEqualTo("Test Post2");
        assertThat(result.getBody().getResponse().getPostList().get(1).getContent()).isEqualTo("해당 포스트는 테스트용 포스트2입니다.");

    }

    @Test
    void 사용자는_올바른_요청에_대해_pageInfo_생성이_가능하다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testPageInfoControllerSetting();
        Long groupId = 1L;

        PageInfoRequest.createPageDTO request = new PageInfoRequest.createPageDTO();
        request.setPageName("새로운 페이지!!");

        // when
        ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.createPageDTO>> result = testContainer.pageInfoController.createPage(groupId, request);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getResponse().getPageId()).isEqualTo(3L);
        assertThat(result.getBody().getResponse().getPageName()).isEqualTo("새로운 페이지!!");

    }

    @Test
    void 사용자는_올바른_요청에_대해_pageInfo_삭제가_가능하다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testPageInfoControllerSetting();
        Long groupId = 1L;
        Long pageId = 2L;

        // when
        ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.deletePageDTO>> result = testContainer.pageInfoController.deletePage(groupId, pageId);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getResponse().getPageId()).isEqualTo(2L);
        assertThat(result.getBody().getResponse().getTitle()).isEqualTo("Test Page2");

    }

    @Test
    void 사용자는_올바른_요청에_대해_pageInfo_좋아요가_가능하다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testPageInfoControllerSetting();
        Long groupId = 1L;
        Long pageId = 1L;

        // when
        ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.likePageDTO>> result = testContainer.pageInfoController.likePage(groupId, pageId);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getResponse().getPageId()).isEqualTo(1L);
        assertThat(result.getBody().getResponse().getGoodCount()).isEqualTo(1);

    }

    @Test
    void 사용자는_올바른_요청에_대해_pageInfo_싫어요가_가능하다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testPageInfoControllerSetting();
        Long groupId = 1L;
        Long pageId = 1L;

        // when
        ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.hatePageDTO>> result = testContainer.pageInfoController.hatePage(groupId, pageId);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getResponse().getPageId()).isEqualTo(1L);
        assertThat(result.getBody().getResponse().getBadCount()).isEqualTo(1);

    }

    @Test
    void 사용자는_올바른_요청에_대해_pageInfo_목차_조회가_가능하다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testPageInfoControllerSetting();
        Long groupId = 1L;
        Long pageId = 1L;

        // when
        ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.getPageIndexDTO>> result = testContainer.pageInfoController.getPageIndex(groupId, pageId);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getResponse().getPageName()).isEqualTo("Test Page");

        assertThat(result.getBody().getResponse().getPostList().get(0).getPostId()).isEqualTo(1L);
        assertThat(result.getBody().getResponse().getPostList().get(0).getIndex()).isEqualTo("1");
        assertThat(result.getBody().getResponse().getPostList().get(0).getPostTitle()).isEqualTo("Test Post1");

        assertThat(result.getBody().getResponse().getPostList().get(1).getPostId()).isEqualTo(2L);
        assertThat(result.getBody().getResponse().getPostList().get(1).getIndex()).isEqualTo("2");
        assertThat(result.getBody().getResponse().getPostList().get(1).getPostTitle()).isEqualTo("Test Post2");

    }

    @Test
    void 사용자는_올바른_요청에_대해_pageInfo_제목으로_조회가_가능하다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testPageInfoControllerSetting();
        Long groupId = 1L;
        String title = "Test Page";

        // when
        ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.getPageFromIdDTO>> result = testContainer.pageInfoController.getPageFromTitle(groupId, title);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getResponse().getPageId()).isEqualTo(1L);
        assertThat(result.getBody().getResponse().getPageName()).isEqualTo("Test Page");
        assertThat(result.getBody().getResponse().getBadCount()).isEqualTo(0);
        assertThat(result.getBody().getResponse().getGoodCount()).isEqualTo(0);

        assertThat(result.getBody().getResponse().getPostList().get(0).getPostId()).isEqualTo(1L);
        assertThat(result.getBody().getResponse().getPostList().get(0).getIndex()).isEqualTo("1");
        assertThat(result.getBody().getResponse().getPostList().get(0).getParentPostId()).isEqualTo(0L);
        assertThat(result.getBody().getResponse().getPostList().get(0).getOrder()).isEqualTo(0);
        assertThat(result.getBody().getResponse().getPostList().get(0).getPostTitle()).isEqualTo("Test Post1");
        assertThat(result.getBody().getResponse().getPostList().get(0).getContent()).isEqualTo("해당 포스트는 테스트용 포스트1입니다.");

        assertThat(result.getBody().getResponse().getPostList().get(1).getPostId()).isEqualTo(2L);
        assertThat(result.getBody().getResponse().getPostList().get(1).getIndex()).isEqualTo("2");
        assertThat(result.getBody().getResponse().getPostList().get(1).getParentPostId()).isEqualTo(0L);
        assertThat(result.getBody().getResponse().getPostList().get(1).getOrder()).isEqualTo(1);
        assertThat(result.getBody().getResponse().getPostList().get(1).getPostTitle()).isEqualTo("Test Post2");
        assertThat(result.getBody().getResponse().getPostList().get(1).getContent()).isEqualTo("해당 포스트는 테스트용 포스트2입니다.");

    }

    @Test
    void 사용자는_올바른_요청에_대해_키워드로_pageInfo_검색이_가능하다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testPageInfoControllerSetting();
        Long groupId = 1L;
        String keyword = "Test";
        int page = 1;

        // when
        ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.searchPageDTO>> result = testContainer.pageInfoController.searchPage(groupId, keyword, page);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(result.getBody().getResponse().getPages().get(0).getPageId()).isEqualTo(1L);
        assertThat(result.getBody().getResponse().getPages().get(0).getPageName()).isEqualTo("Test Page");
        assertThat(result.getBody().getResponse().getPages().get(0).getContent()).isEqualTo("해당 포스트는 테스트용 포스트1입니다.");

        assertThat(result.getBody().getResponse().getPages().get(1).getPageId()).isEqualTo(2L);
        assertThat(result.getBody().getResponse().getPages().get(1).getPageName()).isEqualTo("Test Page2");
        assertThat(result.getBody().getResponse().getPages().get(1).getContent()).isEqualTo("");

    }

    @Test
    void 사용자는_올바른_요청에_대해_최근_바뀐_pageInfo_조회가_가능하다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testPageInfoControllerSetting();
        Long groupId = 1L;

        // when
        ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.getRecentPageDTO>> result = testContainer.pageInfoController.getRecentPage(groupId);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(result.getBody().getResponse().getRecentPage().get(0).getPageId()).isEqualTo(1L);
        assertThat(result.getBody().getResponse().getRecentPage().get(0).getPageName()).isEqualTo("Test Page");
        assertThat(result.getBody().getResponse().getRecentPage().get(0).getUpdated_at()).isEqualTo(testContainer.getTestTime());

        assertThat(result.getBody().getResponse().getRecentPage().get(1).getPageId()).isEqualTo(2L);
        assertThat(result.getBody().getResponse().getRecentPage().get(1).getPageName()).isEqualTo("Test Page2");
        assertThat(result.getBody().getResponse().getRecentPage().get(1).getUpdated_at()).isEqualTo(testContainer.getTestTime());

    }

    @Test
    void 사용자는_올바른_요청에_대해_페이지명으로_링크를위한_pageId_조회가_가능하다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testPageInfoControllerSetting();
        Long groupId = 1L;
        String title = "Test Page";

        // when
        ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.getPageLinkDTO>> result = testContainer.pageInfoController.getPageLink(groupId, title);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getResponse().getPageId()).isEqualTo(1L);

    }


}
