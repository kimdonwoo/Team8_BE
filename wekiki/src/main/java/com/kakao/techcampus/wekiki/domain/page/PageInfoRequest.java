package com.kakao.techcampus.wekiki.domain.page;

import lombok.Getter;
import lombok.Setter;

public class PageInfoRequest {

    @Getter
    @Setter
    public static class createPageDTO {
        private String pageName;
    }


}
