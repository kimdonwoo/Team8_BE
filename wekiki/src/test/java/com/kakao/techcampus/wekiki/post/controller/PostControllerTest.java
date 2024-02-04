package com.kakao.techcampus.wekiki.post.controller;

import com.kakao.techcampus.wekiki._core.utils.ApiUtils;
import com.kakao.techcampus.wekiki.mock.TestContainer;
import com.kakao.techcampus.wekiki.post.controller.request.PostRequest;
import com.kakao.techcampus.wekiki.post.controller.response.PostResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class PostControllerTest {

    /**
     *  1. 사용자는_올바른_요청에_대해_post_생성이_가능하다()
     *
     *  2. 사용자는_올바른_요청에_대해_post_수정이_가능하다()
     *
     *  3. 사용자는_올바른_요청에_대해_post_삭제가_가능하다()
     *
     *  4. 사용자는_올바른_요청에_대해_post_히스토리_조회가_가능하다()
     *
     *  5. 사용자는_올바른_요청에_대해_post_신고가_가능하다()
     *
     */

    @Test
    void 사용자는_올바른_요청에_대해_post_생성이_가능하다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testPostControllerSetting();
        Long groupId = 1L;
        PostRequest.createPostDTO request = PostRequest.createPostDTO.builder()
                .pageId(1L)
                .parentPostId(0L)
                .order(2)
                .title("새로운 Post의 title")
                .content("새로운 Post의 content")
                .build();

        // when
        ResponseEntity<ApiUtils.ApiResult<PostResponse.createPostDTO>> result = testContainer.postRestController.createPost(groupId, request);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getResponse().getPostId()).isEqualTo(3L);
        assertThat(result.getBody().getResponse().getTitle()).isEqualTo("새로운 Post의 title");
        assertThat(result.getBody().getResponse().getContent()).isEqualTo("새로운 Post의 content");

    }

    @Test
    void 사용자는_올바른_요청에_대해_post_수정이_가능하다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testPostControllerSetting();
        Long groupId = 1L;
        PostRequest.modifyPostDTO request = PostRequest.modifyPostDTO.builder()
                .postId(1L)
                .title("수정된 Post의 title")
                .content("수정된 Post의 content")
                .build();

        // when
        ResponseEntity<ApiUtils.ApiResult<PostResponse.modifyPostDTO>> result = testContainer.postRestController.modifyPost(groupId, request);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getResponse().getPostId()).isEqualTo(1L);
        assertThat(result.getBody().getResponse().getTitle()).isEqualTo("수정된 Post의 title");
        assertThat(result.getBody().getResponse().getContent()).isEqualTo("수정된 Post의 content");

    }

    @Test
    void 사용자는_올바른_요청에_대해_post_삭제가_가능하다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testPostControllerSetting();
        Long groupId = 1L;
        Long postId = 1L;

        // when
        ResponseEntity<ApiUtils.ApiResult<PostResponse.deletePostDTO>> result = testContainer.postRestController.deletePost(groupId, postId);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getResponse().getPostId()).isEqualTo(1L);
        assertThat(result.getBody().getResponse().getTitle()).isEqualTo("Test Post1");

    }

    @Test
    void 사용자는_올바른_요청에_대해_post_히스토리_조회가_가능하다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testPostControllerSetting();
        Long groupId = 1L;
        Long postId = 1L;
        int page = 1;

        // when
        ResponseEntity<ApiUtils.ApiResult<PostResponse.getPostHistoryDTO>> result = testContainer.postRestController.getPostHistory(groupId, postId, page);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getResponse().getPostId()).isEqualTo(1L);
        assertThat(result.getBody().getResponse().getHistoryList().get(0).getHistoryId()).isEqualTo(1L);
        assertThat(result.getBody().getResponse().getHistoryList().get(0).getTitle()).isEqualTo("Test Post1");
        assertThat(result.getBody().getResponse().getHistoryList().get(0).getContent()).isEqualTo("해당 포스트는 테스트용 포스트1입니다.");
        assertThat(result.getBody().getResponse().getHistoryList().get(0).getMemberId()).isEqualTo(1L);
        assertThat(result.getBody().getResponse().getHistoryList().get(0).getNickName()).isEqualTo("TestMember1의 groupMember");
        assertThat(result.getBody().getResponse().getHistoryList().get(0).getCreatedAt()).isEqualTo(testContainer.getTestTime());

    }

    @Test
    void 사용자는_올바른_요청에_대해_post_신고가_가능하다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testPostControllerSetting();
        Long groupId = 1L;
        PostRequest.createReportDTO request = PostRequest.createReportDTO.builder()
                .postId(1L)
                .content("해당 post 신고합니다!")
                .build();

        // when
        ResponseEntity<ApiUtils.ApiResult<PostResponse.createReportDTO>> result = testContainer.postRestController.createReport(groupId, request);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getResponse().getReportId()).isEqualTo(1L);

    }
}
