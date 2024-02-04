package com.kakao.techcampus.wekiki.mock;

import com.kakao.techcampus.wekiki._core.utils.port.SecurityUtils;

public class FakeSecurityUtils implements SecurityUtils {
    @Override
    public Long currentMember() {
        return 1L;
    }
}
