package com.kakao.techcampus.wekiki.pageInfo.service.port;

import com.kakao.techcampus.wekiki.post.domain.Post;

import java.util.HashMap;
import java.util.List;

public interface PageIndexGenerator {
    HashMap<Long, String> createIndex(List<Post> posts);
}
