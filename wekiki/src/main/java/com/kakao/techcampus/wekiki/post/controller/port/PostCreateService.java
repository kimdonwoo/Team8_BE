package com.kakao.techcampus.wekiki.post.controller.port;

import com.kakao.techcampus.wekiki.post.controller.request.PostRequest;
import com.kakao.techcampus.wekiki.post.controller.response.PostResponse;

public interface PostCreateService {
    PostResponse.createPostDTO createPost(Long memberId, Long groupId, PostRequest.createPostDTO request);
    PostResponse.createReportDTO createReport(Long memberId, Long groupId, Long postId , String content);

}
