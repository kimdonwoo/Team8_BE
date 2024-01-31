package com.kakao.techcampus.wekiki.comment.controller.port;

import com.kakao.techcampus.wekiki.comment.controller.response.CommentResponse;

public interface CommentReadService {
    CommentResponse.getCommentDTO getComment(Long memberId, Long groupId, Long postId, int pageNo);
}
