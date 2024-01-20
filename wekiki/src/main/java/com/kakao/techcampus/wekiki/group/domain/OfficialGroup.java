package com.kakao.techcampus.wekiki.group.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OfficialGroup extends Group  {

    @Builder(builderMethodName = "officialGroupBuilder")
    public OfficialGroup(Long id, String groupName, String groupProfileImage, List<GroupMember> groupMembers, int memberCount, LocalDateTime created_at) {
        super(id, groupName, groupProfileImage, groupMembers,memberCount, created_at);
    }

}
