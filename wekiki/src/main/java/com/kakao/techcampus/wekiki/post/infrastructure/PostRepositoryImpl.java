package com.kakao.techcampus.wekiki.post.infrastructure;

import com.kakao.techcampus.wekiki.page.domain.PageInfo;
import com.kakao.techcampus.wekiki.post.domain.Post;
import com.kakao.techcampus.wekiki.post.service.port.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostJPARepository postJPARepository;

    @Override
    public List<Post> findPostsByPageIdAndOrderGreaterThan(Long pageId, int orders) {
        return postJPARepository.findPostsByPageIdAndOrderGreaterThan(pageId,orders);
    }

    @Override
    public Optional<Post> findPostWithPageFromPostId(Long postId) {
        return postJPARepository.findPostWithPageFromPostId(postId);
    }

    @Override
    public boolean existsByPageInfoId(Long pageInfoId) {
        return postJPARepository.existsByPageInfoId(pageInfoId);
    }

    @Override
    public boolean existsByParentId(Long parentId) {
        return postJPARepository.existsByParentId(parentId);
    }

    @Override
    public List<Post> findPostInPages(List<PageInfo> pages) {
        return postJPARepository.findPostInPages(pages);
    }

    @Override
    public Optional<Post> findById(Long postId) {
        return postJPARepository.findById(postId);
    }

    @Override
    public Post save(Post newPost) {
        return postJPARepository.save(newPost);
    }

    @Override
    public void deleteById(Long postId) {
        postJPARepository.deleteById(postId);
    }
}
