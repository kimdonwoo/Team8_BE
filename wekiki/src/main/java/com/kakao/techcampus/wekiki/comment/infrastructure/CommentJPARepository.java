package com.kakao.techcampus.wekiki.comment.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentJPARepository extends JpaRepository<CommentEntity, Long> {

    @Query("SELECT c FROM CommentEntity c LEFT JOIN FETCH c.groupMemberEntity WHERE c.postEntity.id = :postId ORDER BY c.created_at")
    Page<CommentEntity> findCommentsByPostIdWithGroupMembers(@Param("postId") Long postId, Pageable pageable);

    @Query("SELECT c FROM CommentEntity c LEFT JOIN FETCH c.groupMemberEntity WHERE c.id = :commentId")
    Optional<CommentEntity> findCommentWithGroupMember(@Param("commentId") Long commentId);
}
