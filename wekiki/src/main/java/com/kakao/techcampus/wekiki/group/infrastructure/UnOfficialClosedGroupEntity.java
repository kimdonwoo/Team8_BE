package com.kakao.techcampus.wekiki.group.infrastructure;

import com.kakao.techcampus.wekiki.group.domain.UnOfficialClosedGroup;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("un_official_closed_group")
public class UnOfficialClosedGroupEntity extends GroupEntity {

    @Builder(builderMethodName = "unOfficialClosedGroupBuilder")
    public UnOfficialClosedGroupEntity(Long id, String groupName, String groupProfileImage, List<GroupMemberEntity> groupMemberEntities, int memberCount, LocalDateTime created_at) {
        super(id, groupName, groupProfileImage,groupMemberEntities,memberCount, created_at);
    }

    public UnOfficialClosedGroup toModel(){
        return UnOfficialClosedGroup.unOfficialClosedGroupBuilder()
                .id(super.getId())
                .groupName(super.getGroupName())
                .groupProfileImage(super.getGroupProfileImage())
                .groupMembers(super.getGroupMemberEntities().stream().map(GroupMemberEntity::toModel).toList())
                .memberCount(super.getMemberCount())
                .created_at(super.getCreated_at())
                .build();
    }

    public static UnOfficialClosedGroupEntity fromModel(UnOfficialClosedGroup unOfficialClosedGroup){
        return UnOfficialClosedGroupEntity.unOfficialClosedGroupBuilder()
                .id(unOfficialClosedGroup.getId())
                .groupName(unOfficialClosedGroup.getGroupName())
                .groupProfileImage(unOfficialClosedGroup.getGroupProfileImage())
                .groupMemberEntities(unOfficialClosedGroup.getGroupMembers().stream().map(GroupMemberEntity::fromModel).toList())
                .memberCount(unOfficialClosedGroup.getMemberCount())
                .created_at(unOfficialClosedGroup.getCreated_at())
                .build();

    }

}
