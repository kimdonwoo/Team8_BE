package com.kakao.techcampus.wekiki.page.infrastructure;

import com.kakao.techcampus.wekiki._core.error.exception.Exception404;
import com.kakao.techcampus.wekiki.page.domain.PageInfo;
import com.kakao.techcampus.wekiki.page.service.port.PageRepository;
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
        return pageJPARepository.findByIdWithPessimisticLock(pageId);
    }

    @Override
    public PageInfo findByIdWithOptimisticLock(Long pageId) {
        return pageJPARepository.findByIdWithOptimisticLock(pageId);
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
        return pageJPARepository.findPagesByTitleContainingKeyword(groupId,keyword,pageable);
    }

    @Override
    public List<PageInfo> findByGroupIdOrderByUpdatedAtDesc(Long groupId, Pageable pageable) {
        return pageJPARepository.findByGroupIdOrderByUpdatedAtDesc(groupId,pageable);
    }

    @Override
    public Optional<PageInfo> findByTitle(Long groupId, String title) {
        return pageJPARepository.findByTitle(groupId,title);
    }

    @Override
    public Optional<PageInfo> findByTitleWithPosts(Long groupId, String title) {
        return pageJPARepository.findByTitleWithPosts(groupId,title);
    }

    @Override
    public Optional<PageInfo> findByPageIdWithPosts(Long pageId) {
        return pageJPARepository.findByPageIdWithPosts(pageId);
    }

    @Override
    public Page<PageInfo> findPagesWithPosts(Long groupId, String keyword, Pageable pageable) {
        return pageJPARepository.findPagesWithPosts(groupId,keyword,pageable);
    }

    @Override
    public Page<PageInfo> findPages(Long groupId, String keyword, Pageable pageable) {
        return pageJPARepository.findPages(groupId,keyword,pageable);
    }

    @Override
    public List<PageInfo> findAllByGroupId(Long groupId) {
        return pageJPARepository.findAllByGroupId(groupId);
    }

    @Override
    public PageInfo save(PageInfo newPageInfo) {
        return pageJPARepository.save(newPageInfo);
    }

    @Override
    public void deleteById(Long pageId) {
        pageJPARepository.deleteById(pageId);
    }

    @Override
    public void saveAndFlush(PageInfo page) {
        pageJPARepository.saveAndFlush(page);
    }

    @Override
    public Optional<PageInfo> findById(Long pageId) {
        //PageInfo pageInfo = pageJPARepository.findById(pageId).orElseThrow(() -> new Exception404("존재하지 않는 페이지 입니다."));
        return pageJPARepository.findById(pageId);
    }

    @Override
    public void deleteAll() {
        pageJPARepository.deleteAll();
    }

}

