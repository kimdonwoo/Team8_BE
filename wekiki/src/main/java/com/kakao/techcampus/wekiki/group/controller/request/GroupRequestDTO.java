package com.kakao.techcampus.wekiki.group.controller.request;

import com.kakao.techcampus.wekiki.group.domain.GroupType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GroupRequestDTO {

    // 그룹 생성
    public record CreateUnOfficialGroupRequestDTO(
            @NotNull(message = "그룹 유형을 선택해 주세요.")
            GroupType groupType,
            @NotBlank(message = "그룹 이름을 입력해 주세요.")
            String groupName,
            @NotNull(message = "그룹 이미지를 첨부해 주세요.")
            String groupImage,
            @NotBlank(message = "그룹 닉네임을 설정해 주세요.")
            String groupNickName,
            String introduction,
            String entranceHint,
            String entrancePassword
    ) {
    }

    public record GroupEntryRequestDTO(
            String entrancePassword
    ) {
    }

    public record JoinGroupRequestDTO(
            @NotBlank(message = "그룹 닉네임을 설정해 주세요.")
            String nickName
    ) {
    }

    public record UpdateMyGroupPageDTO(
            @NotBlank(message = "그룹 닉네임을 설정해 주세요.")
            String groupNickName
    ) {
    }
}
