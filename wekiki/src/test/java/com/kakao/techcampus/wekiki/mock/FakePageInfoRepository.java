package com.kakao.techcampus.wekiki.mock;

import com.kakao.techcampus.wekiki.pageInfo.domain.PageInfo;
import com.kakao.techcampus.wekiki.pageInfo.service.port.PageRepository;
import com.kakao.techcampus.wekiki.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class FakePageInfoRepository implements PageRepository {

    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final ArrayList<PageInfo> data = new ArrayList<>();


    //특정 groupId를 가진 Page들 order by로 updated_at이 최신인 10개 Page 조회
    @Override
    public List<PageInfo> findByGroupIdOrderByUpdatedAtDesc(Long groupId, Pageable pageable) {
        return data.stream().filter(item -> item.getGroup().getId().equals(groupId))
                .sorted(Comparator.comparing(PageInfo::getUpdated_at))
                .limit(10).toList();
    }

    @Override
    public Optional<PageInfo> findByTitle(Long groupId, String title) {
        return data.stream().filter(item -> item.getGroup().getId().equals(groupId))
                .filter(item -> item.getPageName().equals(title)).findAny();
    }

    @Override
    public Optional<PageInfo> findByTitleWithPosts(Long groupId, String title) {
        return data.stream().filter(item -> item.getGroup().getId().equals(groupId))
                .filter(item -> item.getPageName().equals(title)).findAny();
    }

    @Override
    public Optional<PageInfo> findByPageIdWithPosts(Long pageId) {
        return data.stream().filter(item -> item.getId().equals(pageId)).findAny();
    }

    @Override
    public Page<PageInfo> findPages(Long groupId, String keyword, Pageable pageable) {
        List<PageInfo> pageInfos = data.stream().filter(item -> item.getGroup().getId().equals(groupId))
                .filter(item -> item.getPageName().contains(keyword)).toList();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), pageInfos.size());
        return new PageImpl<>(pageInfos.subList(start, end), pageable, pageInfos.size());
    }

    @Override
    public List<PageInfo> findAllByGroupId(Long groupId) {
        return data.stream().filter(item -> item.getGroup().getId().equals(groupId)).toList();
    }

    @Override
    public PageInfo save(PageInfo pageInfo) {
        if(pageInfo.getId() == null || pageInfo.getId() == 0){
            // 새로운 댓글이면 save
            PageInfo newPageInfo = PageInfo.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .group(pageInfo.getGroup())
                    .pageName(pageInfo.getPageName())
                    .posts(new ArrayList<>())
                    .goodCount(pageInfo.getGoodCount())
                    .badCount(pageInfo.getBadCount())
                    .viewCount(pageInfo.getViewCount())
                    .created_at(pageInfo.getCreated_at())
                    .updated_at(pageInfo.getUpdated_at())
                    .build();

            data.add(newPageInfo);
            return newPageInfo;
        }else{
            // 아니면 update
            data.removeIf(item -> Objects.equals(item.getId(), pageInfo.getId()));
            data.add(pageInfo);
            return pageInfo;
        }
    }

    @Override
    public void deleteById(Long pageId) {
        data.removeIf(item -> Objects.equals(item.getId(), pageId));
    }

    @Override
    public Optional<PageInfo> findById(Long pageId) {
        return data.stream().filter(item -> item.getId().equals(pageId)).findAny();
    }

    @Override
    public void deleteAll() {
        data.clear();
    }


    public void addPost(PageInfo pageInfo, Post post){
        data.stream().filter(item -> item.equals(pageInfo)).findAny()
                .get().getPosts().add(post);
    }

    // ----- 사용안하는 메소드 삭제 예정


    @Override
    public Page<PageInfo> findPagesByTitleContainingKeyword(Long groupId, String keyword, Pageable pageable) {
        return null;
    }

    @Override
    public Page<PageInfo> findPagesWithPosts(Long groupId, String keyword, Pageable pageable) {
        return null;
    }

    // ----- 동시성 테스트 용

    @Override
    public void deletePageInfos(List<PageInfo> pageInfos) {

    }

    @Override
    public void saveAndFlush(PageInfo page) {

    }

    @Override
    public PageInfo findByIdWithPessimisticLock(Long pageId) {
        return null;
    }
    @Override
    public PageInfo findByIdWithOptimisticLock(Long pageId) {
        return null;
    }
    @Override
    public void getLock(String key) {
    }
    @Override
    public void releaseLock(String key) {
    }
}
