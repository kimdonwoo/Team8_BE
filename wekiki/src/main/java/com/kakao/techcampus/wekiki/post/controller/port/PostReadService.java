package com.kakao.techcampus.wekiki.post.controller.port;

import com.kakao.techcampus.wekiki.post.controller.response.PostResponse;

public interface PostReadService {
    PostResponse.getPostHistoryDTO getPostHistory(Long memberId, Long groupId, Long postId , int pageNo);

}
