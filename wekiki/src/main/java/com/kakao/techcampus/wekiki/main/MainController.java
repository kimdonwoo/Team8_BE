package com.kakao.techcampus.wekiki.main;

import com.kakao.techcampus.wekiki._core.utils.ApiUtils;
import com.kakao.techcampus.wekiki.page.controller.response.PageInfoResponse;
import com.kakao.techcampus.wekiki.page.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final PageService pageService;
    @GetMapping("/main")
    public ResponseEntity<?> getMainPage() {
        PageInfoResponse.mainPageDTO response = pageService.getMainPage();
        return ResponseEntity.ok(ApiUtils.success(response));
    }
}
