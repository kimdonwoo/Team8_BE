package com.kakao.techcampus.wekiki.domain.group.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("official_group")
public class OfficialGroup extends Group {

    @Builder(builderMethodName = "officialGroupBuilder")
    public OfficialGroup(Long id, String groupName, String groupProfileImage, LocalDateTime created_at) {
        super(id, groupName, groupProfileImage, created_at);
    }
}
