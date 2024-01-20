package com.kakao.techcampus.wekiki.group.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberJPARepository extends JpaRepository<GroupMemberEntity, Long> {
    @Query("SELECT gm FROM GroupMemberEntity gm WHERE gm.groupEntity.id = :groupId AND gm.nickName = :nickName")
    Optional<GroupMemberEntity> findGroupMemberByNickName(@Param("groupId") Long groupId, @Param("nickName") String nickName);

    // 여기다 멤버 group fetchjoin
    @Query("SELECT gm FROM GroupMemberEntity gm LEFT JOIN FETCH gm.memberEntity LEFT JOIN FETCH gm.groupEntity WHERE gm.memberEntity.id = :memberId AND gm.groupEntity.id = :groupId")
    Optional<GroupMemberEntity> findGroupMemberByMemberIdAndGroupIdFetchJoin(@Param("memberId") Long memberId, @Param("groupId") Long groupId);

    @Query("SELECT gm FROM GroupMemberEntity gm WHERE gm.memberEntity.id = :memberId AND gm.groupEntity.id = :groupId")
    GroupMemberEntity findGroupMemberByMemberIdAndGroupId(@Param("memberId") Long memberId, @Param("groupId") Long groupId);

    @Query("SELECT gm FROM GroupMemberEntity gm WHERE gm.groupEntity.id = :id")
    List<GroupMemberEntity> findAllByGroupId(@Param("id") Long id);
}
