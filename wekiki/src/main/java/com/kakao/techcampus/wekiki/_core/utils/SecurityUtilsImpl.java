package com.kakao.techcampus.wekiki._core.utils;

import com.kakao.techcampus.wekiki._core.error.exception.Exception400;
import com.kakao.techcampus.wekiki._core.utils.port.SecurityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtilsImpl implements SecurityUtils {

    public Long currentMember() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if(name.equals("anonymousUser"))
            throw new Exception400("JWT 토큰이 잘못되었습니다.");
        return Long.parseLong(name);
    }

}
