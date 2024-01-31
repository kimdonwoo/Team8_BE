package com.kakao.techcampus.wekiki.pageInfo.infrastructure;

import com.kakao.techcampus.wekiki.pageInfo.domain.PageInfo;
import com.kakao.techcampus.wekiki.pageInfo.service.port.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PageRepositoryImpl implements PageRepository {

    private final PageJPARepository pageJPARepository;


    @Override
    public PageInfo findByIdWithPessimisticLock(Long pageId) {
        return pageJPARepository.findByIdWithPessimisticLock(pageId).toModel();
    }

    @Override
    public PageInfo findByIdWithOptimisticLock(Long pageId) {
        return pageJPARepository.findByIdWithOptimisticLock(pageId).toModel();
    }

    @Override
    public void getLock(String key) {
        pageJPARepository.getLock(key);
    }

    @Override
    public void releaseLock(String key) {
        pageJPARepository.releaseLock(key);
    }

    @Override
    public Page<PageInfo> findPagesByTitleContainingKeyword(Long groupId, String keyword, Pageable pageable) {
        return pageJPARepository.findPagesByTitleContainingKeyword(groupId,keyword,pageable).map(PageInfoEntity::toModel);
    }

    @Override
    public List<PageInfo> findByGroupIdOrderByUpdatedAtDesc(Long groupId, Pageable pageable) {
        return pageJPARepository.findByGroupIdOrderByUpdatedAtDesc(groupId,pageable)
                .stream().map(PageInfoEntity::toModel).toList();
    }

    @Override
    public Optional<PageInfo> findByTitle(Long groupId, String title) {
        return pageJPARepository.findByTitle(groupId,title).map(PageInfoEntity::toModel);
    }

    @Override
    public Optional<PageInfo> findByTitleWithPosts(Long groupId, String title) {
        return pageJPARepository.findByTitleWithPosts(groupId,title).map(PageInfoEntity::toModel);
    }

    @Override
    public Optional<PageInfo> findByPageIdWithPosts(Long pageId) {
        return pageJPARepository.findByPageIdWithPosts(pageId).map(PageInfoEntity::toModel);
    }

    @Override
    public Page<PageInfo> findPagesWithPosts(Long groupId, String keyword, Pageable pageable) {
        return pageJPARepository.findPagesWithPosts(groupId,keyword,pageable).map(PageInfoEntity::toModel);
    }

    @Override
    public Page<PageInfo> findPages(Long groupId, String keyword, Pageable pageable) {
        return pageJPARepository.findPages(groupId,keyword,pageable)
                .map(PageInfoEntity::toModel);
    }

    @Override
    public List<PageInfo> findAllByGroupId(Long groupId) {
        return pageJPARepository.findAllByGroupId(groupId)
                .stream().map(PageInfoEntity::toModel).toList();
    }

    @Override
    public PageInfo save(PageInfo newPageInfo) {
        return pageJPARepository.save(PageInfoEntity.fromModel(newPageInfo)).toModel();
    }

    @Override
    public void deleteById(Long pageId) {
        pageJPARepository.deleteById(pageId);
    }

    @Override
    public PageInfo saveAndFlush(PageInfo pageInfo) {
        return pageJPARepository.saveAndFlush(PageInfoEntity.fromModelWithId(pageInfo)).toModel();
    }

    @Override
    public Optional<PageInfo> findById(Long pageId) {
        //PageInfo pageInfo = pageJPARepository.findById(pageId).orElseThrow(() -> new Exception404("존재하지 않는 페이지 입니다."));
        Optional<PageInfo> temp = pageJPARepository.findById(pageId).map(PageInfoEntity::toModel);
        return temp;
    }

    @Override
    public void deleteAll() {
        pageJPARepository.deleteAll();
    }

    @Override
    public void deletePageInfos(List<PageInfo> pageInfos){
        pageJPARepository.deleteAll(pageInfos.stream().map(PageInfoEntity::fromModel).toList());
    }

}

