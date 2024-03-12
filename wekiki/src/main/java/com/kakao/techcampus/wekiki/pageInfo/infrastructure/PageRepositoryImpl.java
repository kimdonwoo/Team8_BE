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
        return pageJPARepository.findByIdWithPessimisticLock(pageId).toPureModel();
    }

    @Override
    public PageInfo findByIdWithOptimisticLock(Long pageId) {
        return pageJPARepository.findByIdWithOptimisticLock(pageId).toPureModel();
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
    public List<PageInfo> findByGroupIdOrderByUpdatedAtDesc(Long groupId, Pageable pageable) {
        return pageJPARepository.findByGroupIdOrderByUpdatedAtDesc(groupId,pageable)
                .stream().map(PageInfoEntity::toPureModel).toList();
    }

    @Override
    public Optional<PageInfo> findByTitle(Long groupId, String title) {
        return pageJPARepository.findByTitle(groupId,title).map(PageInfoEntity::toModel);
    }

    @Override
    public Optional<PageInfo> findByPageIdWithPosts(Long pageId) {
        return pageJPARepository.findByPageIdWithPosts(pageId).map(PageInfoEntity::toModelWithPost);
    }

    @Override
    public Page<PageInfo> findPages(Long groupId, String keyword, Pageable pageable) {
        return pageJPARepository.findPages(groupId,keyword,pageable)
                .map(PageInfoEntity::toPureModel);
    }

    @Override
    public List<PageInfo> findAllByGroupId(Long groupId) {
        return pageJPARepository.findAllByGroupId(groupId)
                .stream().map(PageInfoEntity::toModel).toList();
    }

    @Override
    public PageInfo save(PageInfo newPageInfo) {
        return pageJPARepository.save(PageInfoEntity.fromModelWithGroup(newPageInfo)).toPureModel();
    }

    @Override
    public PageInfo update(PageInfo updatedPageInfo){
        PageInfoEntity pageInfo = pageJPARepository.findById(updatedPageInfo.getId()).get();
        pageInfo.update(updatedPageInfo);
        return pageInfo.toPureModel();
    }

    @Override
    public void deleteById(Long pageId) {
        pageJPARepository.deleteById(pageId);
    }

    @Override
    public PageInfo saveAndFlush(PageInfo pageInfo) {
        return pageJPARepository.saveAndFlush(PageInfoEntity.fromModelWithId(pageInfo)).toPureModel();
    }

    @Override
    public Optional<PageInfo> findById(Long pageId) {
        return pageJPARepository.findById(pageId).map(PageInfoEntity::toPureModel);
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

