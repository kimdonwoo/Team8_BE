package com.kakao.techcampus.wekiki.pageInfo.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class PageInfoRequest {

    @Getter
    @Setter
    public static class createPageDTO {

        @NotBlank(message = "페이지명을 입력해주세요.")
        @Size(max=200, message = "페이지명은 최대 200자까지 가능합니다.")
        private String pageName;
    }

}
