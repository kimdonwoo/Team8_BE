package com.kakao.techcampus.wekiki.group.infrastructure;

import com.kakao.techcampus.wekiki.group.domain.GroupMember;
import com.kakao.techcampus.wekiki.member.infrastructure.MemberEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "group_member_tb")
public class GroupMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private MemberEntity memberEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    private GroupEntity groupEntity;

    private String nickName;
    private int memberLevel;
    private LocalDateTime created_at;
    private boolean activeStatus;

    @Builder
    public GroupMemberEntity(Long id, MemberEntity memberEntity, GroupEntity groupEntity, String nickName, int memberLevel, LocalDateTime created_at, boolean activeStatus) {
        this.id = id;
        this.memberEntity = memberEntity;
        this.groupEntity = groupEntity;
        this.nickName = nickName;
        this.memberLevel = memberLevel;
        this.created_at = created_at;
        this.activeStatus = activeStatus;
    }


    public GroupMember toModel(){
        return GroupMember.builder()
                .id(this.id)
                .member(memberEntity.toModel())
                .group(groupEntity.toModel())
                .nickName(this.nickName)
                .memberLevel(this.memberLevel)
                .created_at(this.created_at)
                .activeStatus(this.activeStatus)
                .build();
    }

    public static GroupMemberEntity fromModel(GroupMember groupMember){
        return GroupMemberEntity.builder()
                .id(groupMember.getId())
                .memberEntity(MemberEntity.fromModel(groupMember.getMember()))
                .groupEntity(GroupEntity.fromModel(groupMember.getGroup()))
                .nickName(groupMember.getNickName())
                .memberLevel(groupMember.getMemberLevel())
                .created_at(groupMember.getCreated_at())
                .activeStatus(groupMember.isActiveStatus())
                .build();
    }

    public void deleteGroupMember() {
        this.nickName = "알수없음";
        this.activeStatus = false;
    }

}
