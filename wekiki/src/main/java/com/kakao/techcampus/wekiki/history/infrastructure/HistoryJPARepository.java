package com.kakao.techcampus.wekiki.history.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryJPARepository extends JpaRepository<HistoryEntity, Long> {

    @Query("SELECT h FROM HistoryEntity h WHERE h.groupMemberEntity.id = :groupMemberId")
    Page<HistoryEntity> findAllByGroupMember(@Param("groupMemberId") Long groupMemberId, Pageable pageable);

    @Query("SELECT h FROM HistoryEntity h JOIN FETCH h.groupMemberEntity m WHERE h.postEntity.id = :postId ORDER BY h.created_at DESC")
    Page<HistoryEntity> findHistoryWithMemberByPostId(@Param("postId") Long postId, Pageable pageable);

    @Query("SELECT h FROM HistoryEntity h WHERE h.postEntity.id = :postId ORDER BY h.created_at DESC")
    List<HistoryEntity> findHistoryByPostId(@Param("postId") Long postId, Pageable pageable);
}
