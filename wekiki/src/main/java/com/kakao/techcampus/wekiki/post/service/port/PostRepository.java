package com.kakao.techcampus.wekiki.post.service.port;

import com.kakao.techcampus.wekiki.page.domain.PageInfo;
import com.kakao.techcampus.wekiki.post.domain.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    List<Post> findPostsByPageIdAndOrderGreaterThan(Long pageId, int orders);
    Optional<Post> findPostWithPageFromPostId(Long postId);
    boolean existsByPageInfoId(Long pageInfoId);
    boolean existsByParentId(Long parentId);
    List<Post> findPostInPages(List<PageInfo> pages);
    Optional<Post> findById(Long postId);
    Post save(Post newPost);
    void deleteById(Long postId);
}
