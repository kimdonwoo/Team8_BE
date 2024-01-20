package com.kakao.techcampus.wekiki.group.infrastructure;

import com.kakao.techcampus.wekiki.group.domain.OfficialGroup;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("official_group")
public class OfficialGroupEntity extends GroupEntity {

    @Builder(builderMethodName = "officialGroupBuilder")
    public OfficialGroupEntity(Long id, String groupName, String groupProfileImage, List<GroupMemberEntity> groupMemberEntities, int memberCount, LocalDateTime created_at) {
        super(id, groupName, groupProfileImage,groupMemberEntities,memberCount, created_at);
    }

    public OfficialGroup toModel(){
        return OfficialGroup.officialGroupBuilder()
                .id(super.getId())
                .groupName(super.getGroupName())
                .groupProfileImage(super.getGroupProfileImage())
                .groupMembers(super.getGroupMemberEntities().stream().map(GroupMemberEntity::toModel).toList())
                .memberCount(super.getMemberCount())
                .created_at(super.getCreated_at())
                .build();
    }

    public static OfficialGroupEntity fromModel(OfficialGroup officialGroup){
        return OfficialGroupEntity.officialGroupBuilder()
                .id(officialGroup.getId())
                .groupName(officialGroup.getGroupName())
                .groupProfileImage(officialGroup.getGroupProfileImage())
                .groupMemberEntities(officialGroup.getGroupMembers().stream().map(GroupMemberEntity::fromModel).toList())
                .memberCount(officialGroup.getMemberCount())
                .created_at(officialGroup.getCreated_at())
                .build();

    }

}
