package com.kakao.techcampus.wekiki.pageInfo.controller;


import com.kakao.techcampus.wekiki.pageInfo.controller.port.PageInfoUpdateRedissonLockFacade;
import com.kakao.techcampus.wekiki.pageInfo.facade.PageInfoUpdateRedissonLockFacadeImpl;
import com.kakao.techcampus.wekiki._core.utils.ApiUtils;
import com.kakao.techcampus.wekiki._core.utils.port.SecurityUtils;
import com.kakao.techcampus.wekiki.pageInfo.controller.port.PageInfoCreateService;
import com.kakao.techcampus.wekiki.pageInfo.controller.port.PageInfoDeleteService;
import com.kakao.techcampus.wekiki.pageInfo.controller.port.PageInfoReadService;
import com.kakao.techcampus.wekiki.pageInfo.controller.request.PageInfoRequest;
import com.kakao.techcampus.wekiki.pageInfo.controller.response.PageInfoResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/group/{groupid}/page")
@RequiredArgsConstructor
@Validated
@Builder
public class PageInfoController {

    private final PageInfoCreateService pageInfoCreateService;
    private final PageInfoDeleteService pageInfoDeleteService;
    private final PageInfoReadService pageInfoReadService;
    private final PageInfoUpdateRedissonLockFacade pageInfoUpdateRedissonLockFacade;
    private final SecurityUtils securityUtils;

    // 페이지 ID로 페이지 + 글 조회
    @GetMapping("/{pageid}")
    public ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.getPageFromIdDTO>> getPageFromId(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                               @Positive(message = "유효하지 않은 pageID입니다.") @PathVariable Long pageid) {

        PageInfoResponse.getPageFromIdDTO response = pageInfoReadService.getPageFromId(securityUtils.currentMember(), groupid, pageid);

        return ResponseEntity.ok(ApiUtils.success(response));
    }


    // 페이지 생성하기
    @PostMapping("/create")
    public ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.createPageDTO>> createPage(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                         @Valid @RequestBody PageInfoRequest.createPageDTO request) {

        PageInfoResponse.createPageDTO response = pageInfoCreateService.createPage(request.getPageName(), groupid, securityUtils.currentMember());

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // 페이지 삭제하기
    @DeleteMapping("/{pageid}")
    public ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.deletePageDTO>> deletePage(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                         @Positive(message = "유효하지 않은 pageID입니다.") @PathVariable Long pageid) {

        PageInfoResponse.deletePageDTO response = pageInfoDeleteService.deletePage(securityUtils.currentMember(), groupid, pageid);

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // 페이지 좋아요하기
    @PostMapping("/{pageid}/like")
    public ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.likePageDTO>> likePage(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                     @Positive(message = "유효하지 않은 pageID입니다.") @PathVariable Long pageid) {

        PageInfoResponse.likePageDTO response = pageInfoUpdateRedissonLockFacade.likePageWithRedissonLock(pageid, groupid, securityUtils.currentMember());

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // 페이지 좋아요하기
    @PostMapping("/{pageid}/hate")
    public ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.hatePageDTO>> hatePage(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                     @Positive(message = "유효하지 않은 pageID입니다.") @PathVariable Long pageid) {

        PageInfoResponse.hatePageDTO response = pageInfoUpdateRedissonLockFacade.hatePageWithRedissonLock(pageid, groupid, securityUtils.currentMember());

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // 페이지 목차 조회하기
    @GetMapping("/{pageid}/index")
    public ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.getPageIndexDTO>> getPageIndex(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                             @Positive(message = "유효하지 않은 pageID입니다.") @PathVariable Long pageid) {

        PageInfoResponse.getPageIndexDTO response = pageInfoReadService.getPageIndex(groupid,securityUtils.currentMember(), pageid);

        return ResponseEntity.ok(ApiUtils.success(response));
    }



    // 페이지 제목으로 조회하기
    @GetMapping
    public ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.getPageFromIdDTO>> getPageFromTitle(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                                  @NotBlank(message = "페이지명을 입력해주세요.") @Size(max=200, message = "페이지명은 최대 200자까지 가능합니다.") @RequestParam(value = "title") String title) {

        PageInfoResponse.getPageFromIdDTO response = pageInfoReadService.getPageFromTitle(securityUtils.currentMember(), groupid,title);

        return ResponseEntity.ok(ApiUtils.success(response));

    }

    // 페이지 키워드 검색하기
    @GetMapping("/search")
    public ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.searchPageDTO>> searchPage(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                         @Size(max=10, message = "키워드는 최대 10자까지 가능합니다.") @RequestParam(value = "keyword" , defaultValue = "") String keyword,
                                                                                         @RequestParam(value = "page", defaultValue = "1") int page) {

        PageInfoResponse.searchPageDTO response = pageInfoReadService.searchPage(groupid, securityUtils.currentMember(),page - 1, keyword);

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // 최근 바뀐 페이지 목차 조회하기
    @GetMapping("/recent")
    public ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.getRecentPageDTO>> getRecentPage(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid){

        PageInfoResponse.getRecentPageDTO response = pageInfoReadService.getRecentPage(securityUtils.currentMember(), groupid);

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // 페이지명으로 링크 확인하기
    @GetMapping("/link")
    public ResponseEntity<ApiUtils.ApiResult<PageInfoResponse.getPageLinkDTO>> getPageLink(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                           @Size(max=200, message = "페이지명은 최대 200자까지 가능합니다.") @RequestParam(value = "title") String title){

        PageInfoResponse.getPageLinkDTO response = pageInfoReadService.getPageLink(groupid, title);

        return ResponseEntity.ok(ApiUtils.success(response));
    }
}
