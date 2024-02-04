package com.kakao.techcampus.wekiki.comment.controller.port;

import com.kakao.techcampus.wekiki.comment.controller.response.CommentResponse;

public interface CommentCreateService {
    CommentResponse.createCommentDTO createComment(Long memberId, Long groupId, Long postId, String content);
}
