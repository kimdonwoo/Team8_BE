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
        return postJPARepository.findPostsByPageIdAndOrderGreaterThan(pageId, orders)
                .stream().map(PostEntity::toPureModel).toList();
    }

    @Override
    public Optional<Post> findPostWithPageFromPostId(Long postId) {
        return postJPARepository.findPostWithPageFromPostId(postId).map(PostEntity::toModelWihPageInfo);
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
        return postJPARepository.findPostInPages(pages.stream().map(p->PageInfoEntity.fromPureModelWithId(p)).toList())
                .stream().map(PostEntity::toModelWihPageInfo).toList();
    }

    @Override
    public Optional<Post> findById(Long postId) {
        return postJPARepository.findById(postId).map(PostEntity::toPureModel);
    }

    @Override
    public Post save(Post newPost) {
        PostEntity postEntity = PostEntity.create(newPost);
        postEntity.getPageInfoEntity().addPostEntity(postEntity);
        return postJPARepository.save(postEntity).toPureModel();
    }

    @Override
    public Post update(Post updatedPost){
        PostEntity postEntity = postJPARepository.findById(updatedPost.getId()).get();
        postEntity.update(updatedPost);
        return postEntity.toPureModel();
    }

    @Override
    public void deleteById(Long postId) {
        postJPARepository.deleteById(postId);
    }
}
