package com.kakao.techcampus.wekiki.comment.controller;

import com.kakao.techcampus.wekiki._core.utils.ApiUtils;
import com.kakao.techcampus.wekiki.comment.controller.request.CommentRequest;
import com.kakao.techcampus.wekiki.comment.controller.response.CommentResponse;
import com.kakao.techcampus.wekiki.mock.TestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.assertj.core.api.Assertions.assertThat;

public class CommentControllerTest {

    /**
     * 1. 사용자는_올바른_요청에_대해_댓글생성이_가능하다()
     *
     * 2. 사용자는_올바른_요청에_대해_댓글조회가_가능하다()
     *
     * 3. 사용자는_올바른_요청에_대해_댓글삭제가_가능하다()
     *
     * 4. 사용자는_올바른_요청에_대해_댓글수정이_가능하다()
     */

    @Test
    void 사용자는_올바른_요청에_대해_댓글생성이_가능하다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testCommentControllerSetting();
        Long groupId = 1L;
        Long postId = 1L;
        CommentRequest.createComment request = new CommentRequest.createComment("create Comment Controller Test");

        // when
        ResponseEntity<ApiUtils.ApiResult<CommentResponse.createCommentDTO>> result = testContainer.commentController.createComment(groupId, postId, request);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getResponse().getCommentId()).isEqualTo(3L);
        assertThat(result.getBody().getResponse().getContent()).isEqualTo("create Comment Controller Test");
        assertThat(result.getBody().getResponse().getNickName()).isEqualTo("TestMember1의 groupMember");
    }

    @Test
    void 사용자는_올바른_요청에_대해_댓글조회가_가능하다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testCommentControllerSetting();
        Long groupId = 1L;
        Long postId = 1L;
        int page= 1;

        // when
        ResponseEntity<ApiUtils.ApiResult<CommentResponse.getCommentDTO>> result = testContainer.commentController.getComment(groupId, postId, page);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getResponse().getPostId()).isEqualTo(1L);
        assertThat(result.getBody().getResponse().getComments().get(0).getCommentId()).isEqualTo(1L);
        assertThat(result.getBody().getResponse().getComments().get(0).getNickName()).isEqualTo("TestMember1의 groupMember");
        assertThat(result.getBody().getResponse().getComments().get(0).getContent()).isEqualTo("TestMember1의 Test Comment1 입니다.");
        assertThat(result.getBody().getResponse().getComments().get(1).getCommentId()).isEqualTo(2L);
        assertThat(result.getBody().getResponse().getComments().get(1).getNickName()).isEqualTo("TestMember1의 groupMember");
        assertThat(result.getBody().getResponse().getComments().get(1).getContent()).isEqualTo("TestMember1의 Test Comment2 입니다.");

    }

    @Test
    void 사용자는_올바른_요청에_대해_댓글삭제가_가능하다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testCommentControllerSetting();
        Long groupId = 1L;
        Long commentId = 2L;

        // when
        ResponseEntity<ApiUtils.ApiResult<CommentResponse.deleteCommentDTO>> result = testContainer.commentController.deleteComment(groupId, commentId);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getResponse().getCommentId()).isEqualTo(2L);
        assertThat(result.getBody().getResponse().getContent()).isEqualTo("TestMember1의 Test Comment2 입니다.");
    }

    @Test
    void 사용자는_올바른_요청에_대해_댓글수정이_가능하다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.testCommentControllerSetting();
        Long groupId = 1L;
        Long commentId = 2L;
        CommentRequest.updateComment request = new CommentRequest.updateComment("update Comment Controller Test");

        // when
        ResponseEntity<ApiUtils.ApiResult<CommentResponse.updateCommentDTO>> result = testContainer.commentController.updateComment(groupId, commentId, request);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getResponse().getCommentId()).isEqualTo(2L);
        assertThat(result.getBody().getResponse().getNewContent()).isEqualTo("update Comment Controller Test");
    }



}
