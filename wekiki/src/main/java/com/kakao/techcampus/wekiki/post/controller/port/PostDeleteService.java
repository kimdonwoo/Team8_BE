package com.kakao.techcampus.wekiki.post.controller.port;

import com.kakao.techcampus.wekiki.post.controller.response.PostResponse;

public interface PostDeleteService {
    PostResponse.deletePostDTO deletePost(Long memberId, Long groupId, Long postId);
}
