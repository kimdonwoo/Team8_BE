package com.kakao.techcampus.wekiki.group.domain;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class UnOfficialOpenedGroup extends Group {

    private String introduction;
    private String entranceHint;
    private String entrancePassword;

    @Builder(builderMethodName = "unOfficialOpenedGroupBuilder")
    public UnOfficialOpenedGroup(Long id, String groupName, String groupProfileImage, List<GroupMember> groupMembers, int memberCount, LocalDateTime created_at, String introduction, String entranceHint, String entrancePassword) {
        super(id, groupName, groupProfileImage,groupMembers,memberCount, created_at);
        this.introduction = introduction;
        this.entranceHint = entranceHint;
        this.entrancePassword = entrancePassword;
    }
}
