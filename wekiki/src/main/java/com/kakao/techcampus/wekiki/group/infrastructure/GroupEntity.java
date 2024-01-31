package com.kakao.techcampus.wekiki.group.infrastructure;

import com.kakao.techcampus.wekiki.group.domain.Group;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "group_tb")
@DiscriminatorColumn(name = "group_type", discriminatorType = DiscriminatorType.STRING)
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String groupName;
    private String groupProfileImage;

    @OneToMany(mappedBy = "groupEntity", cascade = CascadeType.REMOVE)
    private List<GroupMemberEntity> groupMemberEntities = new ArrayList<>();

    private int memberCount;
    private LocalDateTime created_at;

    @Builder
    public GroupEntity(Long id, String groupName, String groupProfileImage, List<GroupMemberEntity> groupMemberEntities, int memberCount, LocalDateTime created_at) {
        this.id = id;
        this.groupName = groupName;
        this.groupProfileImage = groupProfileImage;
        this.groupMemberEntities = groupMemberEntities;
        this.memberCount = memberCount;
        this.created_at = created_at;
    }

    public Group toModel(){
        System.out.println("@@@@@@@");
        return Group.builder()
                .id(this.id)
                .groupName(this.groupName)
                .groupProfileImage(this.groupProfileImage)
                //.groupMembers(this.groupMemberEntities.stream().map(GroupMemberEntity::toModel).toList())
                .memberCount(this.memberCount)
                .created_at(this.created_at)
                .build();
    }

    public static GroupEntity fromModel(Group group){
        return GroupEntity.builder()
                .id(group.getId())
                .groupName(group.getGroupName())
                .groupProfileImage(group.getGroupProfileImage())
                //.groupMemberEntities(group.getGroupMembers().stream().map(GroupMemberEntity::fromModel).toList())
                .memberCount(group.getMemberCount())
                .created_at(group.getCreated_at())
                .build();
    }



    public void addGroupMember(GroupMemberEntity groupMemberEntity) {
        this.groupMemberEntities.add(groupMemberEntity);
        this.memberCount++;
    }

    @Override
    public String toString() {
        return "GroupEntity{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", groupProfileImage='" + groupProfileImage + '\'' +
                ", memberCount=" + memberCount +
                ", created_at=" + created_at +
                '}';
    }
}
