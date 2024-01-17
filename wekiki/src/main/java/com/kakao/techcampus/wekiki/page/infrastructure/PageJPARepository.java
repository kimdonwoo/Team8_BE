package com.kakao.techcampus.wekiki.page.infrastructure;

import com.kakao.techcampus.wekiki.page.domain.PageInfo;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PageJPARepository extends JpaRepository<PageInfo, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PageInfo  p WHERE p.id = :pageId")
    PageInfo findByIdWithPessimisticLock(@Param("pageId")Long pageId);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("SELECT p FROM PageInfo  p WHERE p.id = :pageId")
    PageInfo findByIdWithOptimisticLock(@Param("pageId")Long pageId);

    @Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
    void getLock(@Param("key") String key);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    void releaseLock(@Param("key") String key);



    // ----------------------
    @Query("SELECT p FROM PageInfo p WHERE p.group.id = :groupId AND p.pageName LIKE :keyword%")
    Page<PageInfo> findPagesByTitleContainingKeyword(@Param("groupId") Long groupId, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM PageInfo p WHERE p.group.id = :groupId ORDER BY p.updated_at DESC")
    List<PageInfo> findByGroupIdOrderByUpdatedAtDesc(Long groupId, Pageable pageable);

    @Query("SELECT p FROM PageInfo p where p.group.id = :groupId AND p.pageName=:title")
    Optional<PageInfo> findByTitle(@Param("groupId") Long groupId, @Param("title") String title);

    @Query("SELECT p FROM PageInfo p LEFT JOIN FETCH p.posts ps WHERE p.group.id = :groupId AND p.pageName = :title ORDER BY ps.orders ASC")
    Optional<PageInfo> findByTitleWithPosts(@Param("groupId") Long groupId, @Param("title") String title);

    @Query("SELECT p FROM PageInfo p LEFT JOIN FETCH p.posts ps WHERE p.id = :pageId ORDER BY ps.orders ASC")
    Optional<PageInfo> findByPageIdWithPosts(@Param("pageId") Long pageId);

//    @Query("SELECT p FROM PageInfo p LEFT JOIN FETCH p.posts ps WHERE p.group.id = :groupId AND p.pageName LIKE %:keyword%")
//    Page<PageInfo> findPagesWithPosts(@Param("groupId") Long groupId, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT distinct p FROM PageInfo p LEFT JOIN FETCH p.posts ps WHERE p.group.id = :groupId AND p.pageName LIKE %:keyword% AND (ps.orders = 1 or ps.orders is null)")
    Page<PageInfo> findPagesWithPosts(@Param("groupId") Long groupId, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM PageInfo p WHERE p.group.id = :groupId AND p.pageName LIKE %:keyword% ")
    Page<PageInfo> findPages(@Param("groupId") Long groupId, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM PageInfo p WHERE p.group.id = :groupId")
    List<PageInfo> findAllByGroupId(@Param("groupId") Long groupId);
}
