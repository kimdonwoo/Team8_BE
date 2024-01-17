package com.kakao.techcampus.wekiki.domain.page;

import com.kakao.techcampus.wekiki._core.facade.LettuceLockFacade;
import com.kakao.techcampus.wekiki._core.facade.NamedLockFacade;
import com.kakao.techcampus.wekiki._core.facade.OptimisticLockFacade;
import com.kakao.techcampus.wekiki._core.facade.RedissonLockFacade;
import com.kakao.techcampus.wekiki.page.domain.PageInfo;
import com.kakao.techcampus.wekiki.page.infrastructure.PageJPARepository;
import com.kakao.techcampus.wekiki.page.service.PageService;
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
public class PageServiceTest {

    @Autowired
    private PageService pageService;
    @Autowired
    private PageJPARepository pageJPARepository;
    @Autowired
    private OptimisticLockFacade optimisticLockFacade;
    @Autowired
    private NamedLockFacade namedLockFacade;
    @Autowired
    private LettuceLockFacade lettuceLockFacade;
    @Autowired
    private RedissonLockFacade redissonLockFacade;

    private long testPageId;

    @BeforeEach
    public void setUp(){
        // 페이지 하나 저장
        PageInfo newPageInfo = PageInfo.builder()
                .pageName("testPage")
                .goodCount(0)
                .badCount(0)
                .viewCount(0)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();

        pageJPARepository.saveAndFlush(newPageInfo);
        testPageId = newPageInfo.getId();

    }

    @AfterEach
    public void after(){
        pageJPARepository.deleteAll();
    }

    @Test
    public void 페이지_좋아요() {

        // 페이지 좋아요 기능
        pageService.likePageTest(testPageId);

        PageInfo rePage = pageService.checkPageFromPageId(testPageId);

        assertEquals(1,rePage.getGoodCount());
    }

    @Test
    public void 동시에_페이지_좋아요_100개_요청_RaceCondition_발생() throws InterruptedException {

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0 ; i < threadCount ; i++){
            executorService.submit(()->{
                try{
                    pageService.likePageTest(testPageId);
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        PageInfo rePage = pageService.checkPageFromPageId(testPageId);
        assertNotEquals(100 ,rePage.getGoodCount());
    }

    @Test
    public void 동시에_페이지_좋아요_100개_요청_WithSynchronized() throws InterruptedException {

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0 ; i < threadCount ; i++){
            executorService.submit(()->{
                try{
                    pageService.likePageWithSynchronized(testPageId);
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        PageInfo rePage = pageService.checkPageFromPageId(testPageId);
        assertEquals(100 ,rePage.getGoodCount());
    }

    @Test
    public void 동시에_페이지_좋아요_100개_요청_WithPessimisticLock() throws InterruptedException {

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0 ; i < threadCount ; i++){
            executorService.submit(()->{
                try{
                    pageService.likePageWithPessimisticLock(testPageId);
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        PageInfo rePage = pageService.checkPageFromPageId(testPageId);
        assertEquals(100 ,rePage.getGoodCount());
    }

    @Test
    public void 동시에_페이지_좋아요_100개_요청_WithOptimisticLock() throws InterruptedException {

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0 ; i < threadCount ; i++){
            executorService.submit(()->{
                try{
                    optimisticLockFacade.likePageWithOptimisticLock(testPageId);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        PageInfo rePage = pageService.checkPageFromPageId(testPageId);
        assertEquals(100 ,rePage.getGoodCount());
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

        PageInfo rePage = pageService.checkPageFromPageId(testPageId);
        assertEquals(100 ,rePage.getGoodCount());
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

        PageInfo rePage = pageService.checkPageFromPageId(testPageId);
        assertEquals(100 ,rePage.getGoodCount());
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

        PageInfo rePage = pageService.checkPageFromPageId(testPageId);
        assertEquals(100 ,rePage.getGoodCount());
    }

}
