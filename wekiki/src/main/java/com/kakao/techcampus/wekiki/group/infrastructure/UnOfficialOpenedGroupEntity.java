package com.kakao.techcampus.wekiki.group.infrastructure;

import com.kakao.techcampus.wekiki.group.domain.UnOfficialOpenedGroup;
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
@DiscriminatorValue("un_official_opened_group")
public class UnOfficialOpenedGroupEntity extends GroupEntity {

    private String introduction;
    private String entranceHint;
    private String entrancePassword;

    @Builder(builderMethodName = "unOfficialOpenedGroupBuilder")
    public UnOfficialOpenedGroupEntity(Long id, String groupName, String groupProfileImage, List<GroupMemberEntity> groupMemberEntities, int memberCount, LocalDateTime created_at, String introduction, String entranceHint, String entrancePassword) {
        super(id, groupName, groupProfileImage,groupMemberEntities,memberCount, created_at);
        this.introduction = introduction;
        this.entranceHint = entranceHint;
        this.entrancePassword = entrancePassword;
    }

    public UnOfficialOpenedGroup toModel(){
        return UnOfficialOpenedGroup.unOfficialOpenedGroupBuilder()
                .id(super.getId())
                .groupName(super.getGroupName())
                .groupProfileImage(super.getGroupProfileImage())
                .groupMembers(super.getGroupMemberEntities().stream().map(GroupMemberEntity::toModel).toList())
                .memberCount(super.getMemberCount())
                .created_at(super.getCreated_at())
                .introduction(this.introduction)
                .entranceHint(this.entranceHint)
                .entrancePassword(this.entrancePassword)
                .build();
    }

    public static UnOfficialOpenedGroupEntity fromModel(UnOfficialOpenedGroup unOfficialOpendGroup){
        return UnOfficialOpenedGroupEntity.unOfficialOpenedGroupBuilder()
                .id(unOfficialOpendGroup.getId())
                .groupName(unOfficialOpendGroup.getGroupName())
                .groupProfileImage(unOfficialOpendGroup.getGroupProfileImage())
                .groupMemberEntities(unOfficialOpendGroup.getGroupMembers().stream().map(GroupMemberEntity::fromModel).toList())
                .memberCount(unOfficialOpendGroup.getMemberCount())
                .created_at(unOfficialOpendGroup.getCreated_at())
                .introduction(unOfficialOpendGroup.getIntroduction())
                .entranceHint(unOfficialOpendGroup.getEntranceHint())
                .entrancePassword(unOfficialOpendGroup.getEntrancePassword())
                .build();
    }

}
