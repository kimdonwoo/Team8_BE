package com.kakao.techcampus.wekiki.group.infrastructure;

import com.kakao.techcampus.wekiki.group.domain.GroupMember;
import com.kakao.techcampus.wekiki.group.service.port.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GroupMemberRepositoryImpl implements GroupMemberRepository {

    private final GroupMemberJPARepository groupMemberJPARepository;


    @Override
    public Optional<GroupMember> findGroupMemberByNickName(Long groupId, String nickName) {
        return groupMemberJPARepository.findGroupMemberByNickName(groupId,nickName);
    }

    @Override
    public Optional<GroupMember> findGroupMemberByMemberIdAndGroupIdFetchJoin(Long memberId, Long groupId) {
        return groupMemberJPARepository.findGroupMemberByMemberIdAndGroupIdFetchJoin(memberId,groupId);
    }

    @Override
    public GroupMember findGroupMemberByMemberIdAndGroupId(Long memberId, Long groupId) {
        return groupMemberJPARepository.findGroupMemberByMemberIdAndGroupId(memberId,groupId);
    }

    @Override
    public List<GroupMember> findAllByGroupId(Long id) {
        return groupMemberJPARepository.findAllByGroupId(id);
    }

    @Override
    public GroupMember save(GroupMember groupMember) {
        return groupMemberJPARepository.save(groupMember);
    }
}
