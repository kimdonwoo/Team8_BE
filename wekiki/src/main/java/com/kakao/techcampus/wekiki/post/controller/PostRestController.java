package com.kakao.techcampus.wekiki.post.controller;


import com.kakao.techcampus.wekiki._core.utils.ApiUtils;
import com.kakao.techcampus.wekiki._core.utils.port.SecurityUtils;
import com.kakao.techcampus.wekiki.post.controller.port.PostCreateService;
import com.kakao.techcampus.wekiki.post.controller.port.PostDeleteService;
import com.kakao.techcampus.wekiki.post.controller.port.PostReadService;
import com.kakao.techcampus.wekiki.post.controller.port.PostUpdateService;
import com.kakao.techcampus.wekiki.post.controller.request.PostRequest;
import com.kakao.techcampus.wekiki.post.controller.response.PostResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/group/{groupid}/post")
@RequiredArgsConstructor
@Validated
@Builder
public class PostRestController {

    private final PostReadService postReadService;
    private final PostCreateService postCreateService;
    private final PostUpdateService postUpdateService;
    private final PostDeleteService postDeleteService;
    private final SecurityUtils securityUtils;

    // 게시글 생성
    @PostMapping("/create")
    public ResponseEntity<ApiUtils.ApiResult<PostResponse.createPostDTO>> createPost(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                     @Valid @RequestBody PostRequest.createPostDTO request) {

        PostResponse.createPostDTO response = postCreateService.createPost(securityUtils.currentMember(),groupid,request);

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // 게시글 수정
    @PutMapping("/modify")
    public ResponseEntity<ApiUtils.ApiResult<PostResponse.modifyPostDTO>> modifyPost(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                     @Valid @RequestBody PostRequest.modifyPostDTO request){

        PostResponse.modifyPostDTO response = postUpdateService.modifyPost(securityUtils.currentMember(),groupid, request.getPostId(), request.getTitle(), request.getContent());

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // 게시글 삭제
    @DeleteMapping("/{postid}")
    public ResponseEntity<ApiUtils.ApiResult<PostResponse.deletePostDTO>> deletePost(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                     @Positive(message = "유효하지 않은 postID입니다.") @PathVariable Long postid){

        PostResponse.deletePostDTO response = postDeleteService.deletePost(securityUtils.currentMember(), groupid, postid);

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // 게시글 히스토리 조회
    @GetMapping("/{postid}/history")
    public ResponseEntity<ApiUtils.ApiResult<PostResponse.getPostHistoryDTO>> getPostHistory(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                             @Positive(message = "유효하지 않은 postID입니다.") @PathVariable Long postid
            , @RequestParam(value = "page", defaultValue = "1") int page){

        PostResponse.getPostHistoryDTO response = postReadService.getPostHistory(securityUtils.currentMember(), groupid, postid, page - 1);

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // 게시글 신고하기
    @PostMapping("/report")
    public ResponseEntity<ApiUtils.ApiResult<PostResponse.createReportDTO>> createReport(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                         @Valid @RequestBody PostRequest.createReportDTO request){

        PostResponse.createReportDTO response = postCreateService.createReport(securityUtils.currentMember(), groupid, request.getPostId(), request.getContent());

        return ResponseEntity.ok(ApiUtils.success(response));
    }

}
