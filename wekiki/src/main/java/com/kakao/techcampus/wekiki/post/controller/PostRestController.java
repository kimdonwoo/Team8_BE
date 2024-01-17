package com.kakao.techcampus.wekiki.post.controller;


import com.kakao.techcampus.wekiki._core.utils.ApiUtils;
import com.kakao.techcampus.wekiki.post.service.PostService;
import com.kakao.techcampus.wekiki.post.controller.request.PostRequest;
import com.kakao.techcampus.wekiki.post.controller.response.PostResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.kakao.techcampus.wekiki._core.utils.SecurityUtils.currentMember;

@RestController
@RequestMapping("/group/{groupid}/post")
@RequiredArgsConstructor
@Validated
public class PostRestController {

    private final PostService postService;

    // 게시글 생성
    @PostMapping("/create")
    public ResponseEntity<ApiUtils.ApiResult<PostResponse.createPostDTO>> createPost(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                     @Valid @RequestBody PostRequest.createPostDTO request) {

        PostResponse.createPostDTO response = postService.createPost(currentMember(),groupid,request.getPageId(), request.getParentPostId(),
                request.getOrder(), request.getTitle(),request.getContent());

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // 게시글 수정
    @PutMapping("/modify")
    public ResponseEntity<ApiUtils.ApiResult<PostResponse.modifyPostDTO>> modifyPost(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                     @Valid @RequestBody PostRequest.modifyPostDTO request){

        PostResponse.modifyPostDTO response = postService.modifyPost(currentMember(),groupid, request.getPostId(), request.getTitle(), request.getContent());

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // 게시글 삭제
    @DeleteMapping("/{postid}")
    public ResponseEntity<ApiUtils.ApiResult<PostResponse.deletePostDTO>> deletePost(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                     @Positive(message = "유효하지 않은 postID입니다.") @PathVariable Long postid){

        PostResponse.deletePostDTO response = postService.deletePost(currentMember(), groupid, postid);

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // 게시글 히스토리 조회
    @GetMapping("/{postid}/history")
    public ResponseEntity<ApiUtils.ApiResult<PostResponse.getPostHistoryDTO>> getPostHistory(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                             @Positive(message = "유효하지 않은 postID입니다.") @PathVariable Long postid
            , @RequestParam(value = "page", defaultValue = "1") int page){

        PostResponse.getPostHistoryDTO response = postService.getPostHistory(currentMember(), groupid, postid, page - 1);

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // 게시글 신고하기
    @PostMapping("/report")
    public ResponseEntity<ApiUtils.ApiResult<PostResponse.createReportDTO>> createReport(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                         @Valid @RequestBody PostRequest.createReportDTO request){

        PostResponse.createReportDTO response = postService.createReport(currentMember(), groupid, request.getPostId(), request.getContent());

        return ResponseEntity.ok(ApiUtils.success(response));
    }

}
