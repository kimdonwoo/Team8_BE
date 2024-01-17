package com.kakao.techcampus.wekiki.group.service.port;

import com.kakao.techcampus.wekiki.group.domain.GroupMember;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository {

    Optional<GroupMember> findGroupMemberByNickName(Long groupId, String nickName);
    Optional<GroupMember> findGroupMemberByMemberIdAndGroupIdFetchJoin(Long memberId, Long groupId);
    GroupMember findGroupMemberByMemberIdAndGroupId( Long memberId, Long groupId);
    List<GroupMember> findAllByGroupId(Long id);
    GroupMember save(GroupMember groupMember);
}
