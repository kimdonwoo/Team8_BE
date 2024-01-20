package com.kakao.techcampus.wekiki.post.infrastructure;

import com.kakao.techcampus.wekiki.pageInfo.domain.PageInfo;
import com.kakao.techcampus.wekiki.pageInfo.infrastructure.PageInfoEntity;
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
        return postJPARepository.findPostsByPageIdAndOrderGreaterThan(pageId,orders)
                .stream().map(PostEntity::toModel).toList();
    }

    @Override
    public Optional<Post> findPostWithPageFromPostId(Long postId) {
        return postJPARepository.findPostWithPageFromPostId(postId).map(PostEntity::toModel);
    }

    @Override
    public boolean existsByPageInfoId(Long pageInfoId) {
        return postJPARepository.existsByPageInfoEntityId(pageInfoId);
    }

    @Override
    public boolean existsByParentId(Long parentId) {
        return postJPARepository.existsByParentId(parentId);
    }

    @Override
    public List<Post> findPostInPages(List<PageInfo> pages) {
        return postJPARepository.findPostInPages(pages.stream().map(p->PageInfoEntity.fromModel(p)).toList())
                .stream().map(PostEntity::toModel).toList();
    }

    @Override
    public Optional<Post> findById(Long postId) {
        return postJPARepository.findById(postId).map(PostEntity::toModel);
    }

    @Override
    public Post save(Post newPost) {
        PostEntity postEntity = PostEntity.fromModel(newPost);
        PageInfoEntity.fromModel(newPost.getPageInfo()).addPostEntity(postEntity);
        return postJPARepository.save(PostEntity.fromModel(newPost)).toModel();
    }

    @Override
    public void deleteById(Long postId) {
        postJPARepository.deleteById(postId);
    }
}
