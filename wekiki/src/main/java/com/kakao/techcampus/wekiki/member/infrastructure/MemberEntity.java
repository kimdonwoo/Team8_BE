package com.kakao.techcampus.wekiki.member.infrastructure;

import com.kakao.techcampus.wekiki.group.infrastructure.GroupMemberEntity;
import com.kakao.techcampus.wekiki.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "member_tb")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @NotNull
    private String email;
    private String password;
    private LocalDateTime created_at;

    @OneToMany(mappedBy = "memberEntity")
    private List<GroupMemberEntity> groupMemberEntities = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    Authority authority;

    @Builder
    public MemberEntity(Long id, String name, String email, String password, LocalDateTime created_at, List<GroupMemberEntity> groupMemberEntities, Authority authority) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.created_at = created_at;
        this.groupMemberEntities = groupMemberEntities;
        this.authority = authority;
    }

    public Member toModel(){
        return Member.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .created_at(created_at)
                .groupMembers(groupMemberEntities.stream().map(GroupMemberEntity::toModel).toList())
                .authority(authority)
                .build();
    }

    public static MemberEntity fromModel(Member member){
        return MemberEntity.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .password(member.getPassword())
                .created_at(member.getCreated_at())
                .groupMemberEntities(member.getGroupMembers().stream().map(GroupMemberEntity::fromModel).toList())
                .authority(member.getAuthority())
                .build();
    }

}
