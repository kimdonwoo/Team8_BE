package com.kakao.techcampus.wekiki.member.domain;

import com.kakao.techcampus.wekiki.group.domain.GroupMember;
import com.kakao.techcampus.wekiki.member.infrastructure.Authority;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Member {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final LocalDateTime created_at;
    private final List<GroupMember> groupMembers;
    Authority authority;

    @Builder
    public Member(Long id, String name, String email, String password, LocalDateTime created_at, List<GroupMember> groupMembers, Authority authority) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.created_at = created_at;
        this.groupMembers = groupMembers;
        this.authority = authority;
    }

    public Member changePassword(String password){
        return Member.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .password(password)
                .created_at(this.created_at)
                .groupMembers(this.groupMembers)
                .authority(this.authority)
                .build();
    }

    public Member changeNickName(String name){
        return Member.builder()
                .id(this.id)
                .name(name)
                .email(this.email)
                .password(this.password)
                .created_at(this.created_at)
                .groupMembers(this.groupMembers)
                .authority(this.authority)
                .build();
    }

    public Member delete(String randomPassword){
        return Member.builder()
                .id(this.id)
                .name("알수없음")
                .email("-")
                .password(randomPassword)
                .created_at(this.created_at)
                .groupMembers(this.groupMembers)
                .authority(Authority.none)
                .build();
    }
}
