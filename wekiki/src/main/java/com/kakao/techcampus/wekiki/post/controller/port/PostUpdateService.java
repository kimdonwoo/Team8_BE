package com.kakao.techcampus.wekiki.post.controller.port;

import com.kakao.techcampus.wekiki.post.controller.response.PostResponse;

public interface PostUpdateService {
    PostResponse.modifyPostDTO modifyPost(Long memberId , Long groupId, Long postId , String title, String content);

}
