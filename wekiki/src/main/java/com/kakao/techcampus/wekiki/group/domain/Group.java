package com.kakao.techcampus.wekiki.group.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Group {

    private final Long id;
    private final String groupName;
    private final String groupProfileImage;
    private final List<GroupMember> groupMembers;
    private final int memberCount;
    private final LocalDateTime created_at;

    @Builder
    public Group(Long id, String groupName, String groupProfileImage, List<GroupMember> groupMembers, int memberCount, LocalDateTime created_at) {
        this.id = id;
        this.groupName = groupName;
        this.groupProfileImage = groupProfileImage;
        this.groupMembers = groupMembers;
        this.memberCount = memberCount;
        this.created_at = created_at;
    }

    public Group minusMemberCount(){
        return Group.builder()
                .id(this.id)
                .groupName(this.groupName)
                .groupProfileImage(this.groupProfileImage)
                .groupMembers(this.groupMembers)
                .memberCount(this.memberCount-1)
                .created_at(this.created_at)
                .build();
    }
}
