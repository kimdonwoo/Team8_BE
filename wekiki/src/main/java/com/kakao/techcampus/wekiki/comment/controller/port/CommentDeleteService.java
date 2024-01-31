package com.kakao.techcampus.wekiki.comment.controller.port;

import com.kakao.techcampus.wekiki.comment.controller.response.CommentResponse;

public interface CommentDeleteService {
    CommentResponse.deleteCommentDTO deleteComment(Long memberId, Long groupId, Long commentId);
}
