package com.kakao.techcampus.wekiki.group.domain;

import com.kakao.techcampus.wekiki.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GroupMember {

    private Long id;
    private Member member;
    private Group group;
    private String nickName;
    private int memberLevel;
    private LocalDateTime created_at;
    private boolean activeStatus;

    @Builder
    public GroupMember(Long id, Member member, Group group, String nickName, int memberLevel, LocalDateTime created_at, boolean activeStatus) {
        this.id = id;
        this.member = member;
        this.group = group;
        this.nickName = nickName;
        this.memberLevel = memberLevel;
        this.created_at = created_at;
        this.activeStatus = activeStatus;
    }

    public GroupMember update(String groupNickName){
        return GroupMember.builder()
                .id(this.id)
                .member(this.member)
                .group(this.group)
                .nickName(groupNickName)
                .memberLevel(this.memberLevel)
                .created_at(this.created_at)
                .activeStatus(this.activeStatus)
                .build();
    }

    public GroupMember changeStatus(){
        return GroupMember.builder()
                .id(this.id)
                .member(this.member)
                .group(this.group)
                .nickName(this.nickName)
                .memberLevel(this.memberLevel)
                .created_at(this.created_at)
                .activeStatus(!this.activeStatus)
                .build();
    }
}
