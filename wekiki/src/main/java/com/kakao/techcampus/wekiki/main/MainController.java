package com.kakao.techcampus.wekiki.main;

import com.kakao.techcampus.wekiki._core.utils.ApiUtils;
import com.kakao.techcampus.wekiki._core.utils.port.SecurityUtils;
import com.kakao.techcampus.wekiki.pageInfo.controller.response.PageInfoResponse;
import com.kakao.techcampus.wekiki.pageInfo.service.PageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final PageServiceImpl pageService;
    private final SecurityUtils securityUtils;

    @GetMapping("/main")
    public ResponseEntity<?> getMainPage() {
        PageInfoResponse.mainPageDTO response = pageService.getMainPage(securityUtils.currentMember());
        return ResponseEntity.ok(ApiUtils.success(response));
    }
}
