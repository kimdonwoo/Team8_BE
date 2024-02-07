package com.kakao.techcampus.wekiki.page.service;


import com.kakao.techcampus.wekiki._core.facade.LettuceLockFacadeTest;
import com.kakao.techcampus.wekiki._core.facade.NamedLockFacadeTest;
import com.kakao.techcampus.wekiki._core.facade.OptimisticLockFacadeTest;
import com.kakao.techcampus.wekiki._core.facade.RedissonLockFacadeTest;
import com.kakao.techcampus.wekiki.group.domain.Group;
import com.kakao.techcampus.wekiki.group.domain.UnOfficialClosedGroup;
import com.kakao.techcampus.wekiki.group.infrastructure.GroupRepositoryImpl;
import com.kakao.techcampus.wekiki.pageInfo.domain.PageInfo;
import com.kakao.techcampus.wekiki.pageInfo.infrastructure.PageInfoEntity;
import com.kakao.techcampus.wekiki.pageInfo.infrastructure.PageJPARepository;
import com.kakao.techcampus.wekiki.pageInfo.service.port.PageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class PageConcurrencyServiceTest {

    @Autowired
    private PageConcurrencyService pageConcurrencyService;

    @Autowired
    private GroupRepositoryImpl groupRepository;
    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private PageJPARepository pageJPARepository;

    @Autowired
    private OptimisticLockFacadeTest optimisticLockFacade;
    @Autowired
    private NamedLockFacadeTest namedLockFacade;
    @Autowired
    private LettuceLockFacadeTest lettuceLockFacade;
    @Autowired
    private RedissonLockFacadeTest redissonLockFacade;

    private long testPageId;

    @BeforeEach
    public void setUp(){
        // 페이지 하나 저장
        UnOfficialClosedGroup group = UnOfficialClosedGroup.unOfficialClosedGroupBuilder()
                .groupName("TestGroup")
                .groupProfileImage("s3/url")
                //.groupMembers(this.groupMemberEntities.stream().map(GroupMemberEntity::toModel).toList())
                .memberCount(0)
                .created_at(LocalDateTime.now())
                .build();

        Group savedGroup = groupRepository.save(group);

        PageInfo pageInfo = PageInfo.builder()
                .group(savedGroup)
                .pageName("testPage")
                .goodCount(0)
                .badCount(0)
                .viewCount(0)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();


        PageInfo newPageInfo = pageRepository.saveAndFlush(pageInfo);
        testPageId = newPageInfo.getId();

    }

    @AfterEach
    public void after(){
        pageRepository.deleteAll();
    }

    @Test
    public void 페이지_좋아요() {

        // 페이지 좋아요 기능
        pageConcurrencyService.likePageTest(testPageId);

        PageInfoEntity pageInfo = pageJPARepository.findById(testPageId).get();
        assertEquals(1,pageInfo.getGoodCount());
    }

    @Test
    public void 동시에_페이지_좋아요_100개_요청_RaceCondition_발생() throws InterruptedException {

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0 ; i < threadCount ; i++){
            executorService.submit(()->{
                try{
                    pageConcurrencyService.likePageTest(testPageId);
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        PageInfoEntity pageInfo = pageJPARepository.findById(testPageId).get();
        assertNotEquals(100 ,pageInfo.getGoodCount());
    }

    @Test
    public void 동시에_페이지_좋아요_100개_요청_WithSynchronized() throws InterruptedException {

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0 ; i < threadCount ; i++){
            executorService.submit(()->{
                try{
                    pageConcurrencyService.likePageWithSynchronizedSync(testPageId);
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        PageInfoEntity pageInfo = pageJPARepository.findById(testPageId).get();
        assertEquals(100 ,pageInfo.getGoodCount());
    }

    @Test
    public void 동시에_페이지_좋아요_100개_요청_WithNamedLock() throws InterruptedException {

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0 ; i < threadCount ; i++){
            executorService.submit(()->{
                try{
                    namedLockFacade.likePageWithNamedLock(testPageId);
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        PageInfoEntity pageInfo = pageJPARepository.findById(testPageId).get();
        assertEquals(100 ,pageInfo.getGoodCount());
    }

    @Test
    public void 동시에_페이지_좋아요_100개_요청_WithRedis_byLuttceLock() throws InterruptedException {

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0 ; i < threadCount ; i++){
            executorService.submit(()->{
                try{
                    lettuceLockFacade.likePageWithLettuceLock(testPageId);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        PageInfoEntity pageInfo = pageJPARepository.findById(testPageId).get();
        assertEquals(100 ,pageInfo.getGoodCount());
    }

    @Test
    public void 동시에_페이지_좋아요_100개_요청_WithRedis_byRedissonLock() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    redissonLockFacade.likePageWithRedissonLock(testPageId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        PageInfoEntity pageInfo = pageJPARepository.findById(testPageId).get();
        assertEquals(100 ,pageInfo.getGoodCount());
    }

//    @Test
//    public void 동시에_페이지_좋아요_100개_요청_WithPessimisticLock() throws InterruptedException {
//
//        int threadCount = 100;
//        ExecutorService executorService = Executors.newFixedThreadPool(32);
//        CountDownLatch latch = new CountDownLatch(threadCount);
//
//        for(int i = 0 ; i < threadCount ; i++){
//            executorService.submit(()->{
//                try{
//                    pageConcurrencyService.likePageWithPessimisticLock(testPageId);
//                }finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//
//        PageInfoEntity pageInfo = pageJPARepository.findById(testPageId).get();
//        assertEquals(100 ,pageInfo.getGoodCount());
//    }

//    @Test
//    public void 동시에_페이지_좋아요_100개_요청_WithOptimisticLock() throws InterruptedException {
//
//        int threadCount = 10;
//        ExecutorService executorService = Executors.newFixedThreadPool(32);
//        CountDownLatch latch = new CountDownLatch(threadCount);
//
//        for(int i = 0 ; i < threadCount ; i++){
//            executorService.submit(()->{
//                try{
//                    optimisticLockFacade.likePageWithOptimisticLock(testPageId);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//
//        PageInfoEntity pageInfo = pageJPARepository.findById(testPageId).get();
//        assertEquals(10 ,pageInfo.getGoodCount());
//    }
}
