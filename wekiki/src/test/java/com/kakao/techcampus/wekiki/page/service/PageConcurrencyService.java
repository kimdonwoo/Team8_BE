package com.kakao.techcampus.wekiki.page.service;

import com.kakao.techcampus.wekiki._core.error.exception.Exception404;
import com.kakao.techcampus.wekiki._core.utils.port.RedisUtils;
import com.kakao.techcampus.wekiki.group.service.port.GroupMemberRepository;
import com.kakao.techcampus.wekiki.group.service.port.GroupRepository;
import com.kakao.techcampus.wekiki.member.service.port.MemberRepository;
import com.kakao.techcampus.wekiki.pageInfo.domain.PageInfo;
import com.kakao.techcampus.wekiki.pageInfo.service.port.PageIndexGenerator;
import com.kakao.techcampus.wekiki.pageInfo.service.port.PageRepository;
import com.kakao.techcampus.wekiki.post.service.port.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PageConcurrencyService {

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private PageConcurrencyWithSync pageConcurrencyWithSync;
    
    @Transactional
    public PageInfo likePageTest(Long pageId){
        PageInfo page = pageRepository.findById(pageId).orElseThrow(() -> new Exception404("존재하지 않는 페이지 입니다."));
        PageInfo newPage = page.plusGoodCount();
        PageInfo pageInfo = pageRepository.update(newPage);
        return pageInfo;
    }

    public synchronized void likePageWithSynchronizedSync(Long pageId){
        pageConcurrencyWithSync.likePageWithSynchronized(pageId);
    }



    @Transactional
    public void likePageWithPessimisticLock(Long pageId){
        PageInfo page = pageRepository.findByIdWithPessimisticLock(pageId);
        PageInfo newPage = page.plusGoodCount();
        pageRepository.update(newPage);
    }

    @Transactional
    public void likePageWithOptimisticLock(Long pageId){
        PageInfo page = pageRepository.findByIdWithOptimisticLock(pageId);
        PageInfo newPage = page.plusGoodCount();
        pageRepository.update(newPage);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void likePageWithNamedLockAndLettuceLock(Long pageId){
        PageInfo page = pageRepository.findById(pageId).orElseThrow(
                () -> new Exception404("존재하지 않는 페이지 입니다."));
        PageInfo newPage = page.plusGoodCount();
        pageRepository.update(newPage);
    }

}
