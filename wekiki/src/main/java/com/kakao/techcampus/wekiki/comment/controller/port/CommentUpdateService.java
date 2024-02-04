package com.kakao.techcampus.wekiki.comment.controller.port;

import com.kakao.techcampus.wekiki.comment.controller.response.CommentResponse;

public interface CommentUpdateService {
    CommentResponse.updateCommentDTO updateComment(Long memberId, Long groupId, Long commentId, String updateContent);
}
