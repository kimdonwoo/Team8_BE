package com.kakao.techcampus.wekiki.pageInfo.infrastructure;

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
public interface PageJPARepository extends JpaRepository<PageInfoEntity, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PageInfoEntity p WHERE p.id = :pageId")
    PageInfoEntity findByIdWithPessimisticLock(@Param("pageId")Long pageId);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("SELECT p FROM PageInfoEntity p WHERE p.id = :pageId")
    PageInfoEntity findByIdWithOptimisticLock(@Param("pageId")Long pageId);

    @Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
    void getLock(@Param("key") String key);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    void releaseLock(@Param("key") String key);



    // ----------------------
    @Query("SELECT p FROM PageInfoEntity p WHERE p.groupEntity.id = :groupId ORDER BY p.updated_at DESC")
    List<PageInfoEntity> findByGroupIdOrderByUpdatedAtDesc(@Param("groupId") Long groupId, Pageable pageable);

    @Query("SELECT p FROM PageInfoEntity p where p.groupEntity.id = :groupId AND p.pageName=:title")
    Optional<PageInfoEntity> findByTitle(@Param("groupId") Long groupId, @Param("title") String title);

    @Query("SELECT p FROM PageInfoEntity p LEFT JOIN FETCH p.postEntities ps WHERE p.id = :pageId ORDER BY ps.orders ASC")
    Optional<PageInfoEntity> findByPageIdWithPosts(@Param("pageId") Long pageId);


    @Query("SELECT p FROM PageInfoEntity p WHERE p.groupEntity.id = :groupId AND p.pageName LIKE %:keyword% ")
    Page<PageInfoEntity> findPages(@Param("groupId") Long groupId, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM PageInfoEntity p WHERE p.groupEntity.id = :groupId")
    List<PageInfoEntity> findAllByGroupId(@Param("groupId") Long groupId);
}
