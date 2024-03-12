package com.kakao.techcampus.wekiki.pageInfo.service.port;

import com.kakao.techcampus.wekiki.pageInfo.domain.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface PageRepository {
    PageInfo findByIdWithPessimisticLock(Long pageId);
    PageInfo findByIdWithOptimisticLock(Long pageId);
    void getLock(String key);
    void releaseLock(String key);
    List<PageInfo> findByGroupIdOrderByUpdatedAtDesc(Long groupId, Pageable pageable);
    Optional<PageInfo> findByTitle(Long groupId, String title);
    Optional<PageInfo> findByPageIdWithPosts(Long pageId);
    Page<PageInfo> findPages(Long groupId, String keyword, Pageable pageable);
    List<PageInfo> findAllByGroupId(Long groupId);
    PageInfo save(PageInfo newPageInfo);
    void deleteById(Long pageId);
    PageInfo saveAndFlush(PageInfo page);
    Optional<PageInfo> findById(Long pageId);
    void deleteAll();
    void deletePageInfos(List<PageInfo> pageInfos);
    PageInfo update(PageInfo updatedPageInfo);
}
