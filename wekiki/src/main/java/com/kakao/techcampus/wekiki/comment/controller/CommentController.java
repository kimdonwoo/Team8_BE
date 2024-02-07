package com.kakao.techcampus.wekiki.comment.controller;


import com.kakao.techcampus.wekiki._core.utils.ApiUtils;
import com.kakao.techcampus.wekiki._core.utils.port.SecurityUtils;
import com.kakao.techcampus.wekiki.comment.controller.port.CommentCreateService;
import com.kakao.techcampus.wekiki.comment.controller.port.CommentDeleteService;
import com.kakao.techcampus.wekiki.comment.controller.port.CommentReadService;
import com.kakao.techcampus.wekiki.comment.controller.port.CommentUpdateService;
import com.kakao.techcampus.wekiki.comment.controller.request.CommentRequest;
import com.kakao.techcampus.wekiki.comment.controller.response.CommentResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/group/{groupid}")
@RequiredArgsConstructor
@Validated
@Builder
public class CommentController {

    private final CommentReadService commentReadService;
    private final CommentCreateService commentCreateService;
    private final CommentUpdateService commentUpdateService;
    private final CommentDeleteService commentDeleteService;
    private final SecurityUtils securityUtils;


    // 댓글 조회하기
    @GetMapping("/post/{postid}/comment")
    public ResponseEntity<ApiUtils.ApiResult<CommentResponse.getCommentDTO>> getComment(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                        @Positive(message = "유효하지 않은 postID입니다.") @PathVariable Long postid,
                                                                                        @Positive(message = "유효하지 않은 pageID입니다.") @RequestParam(value = "page", defaultValue = "1") int page){

        CommentResponse.getCommentDTO response = commentReadService.getComment(securityUtils.currentMember(), groupid, postid, page-1);

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // 댓글 쓰기
    @PostMapping("/post/{postid}/comment")
    public ResponseEntity<ApiUtils.ApiResult<CommentResponse.createCommentDTO>> createComment(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                              @Positive(message = "유효하지 않은 postID입니다.") @PathVariable Long postid,
                                                                                              @Valid @RequestBody CommentRequest.createComment request){

        CommentResponse.createCommentDTO response = commentCreateService.createComment(securityUtils.currentMember(), groupid, postid, request.getContent());

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // 댓글 삭제하기
    @DeleteMapping("/comment/{commentid}")
    public ResponseEntity<ApiUtils.ApiResult<CommentResponse.deleteCommentDTO>> deleteComment(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                              @Positive(message = "유효하지 않은 commentID입니다.") @PathVariable Long commentid){

        CommentResponse.deleteCommentDTO response = commentDeleteService.deleteComment(securityUtils.currentMember(), groupid, commentid);

        return ResponseEntity.ok(ApiUtils.success(response));

    }

    // 댓글 수정하기
    @PatchMapping("/comment/{commentid}")
    public ResponseEntity<ApiUtils.ApiResult<CommentResponse.updateCommentDTO>> updateComment(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                              @Positive(message = "유효하지 않은 commentID입니다.") @PathVariable Long commentid,
                                                                                              @Valid @RequestBody CommentRequest.updateComment request){

        CommentResponse.updateCommentDTO response = commentUpdateService.updateComment(securityUtils.currentMember(), groupid, commentid, request.getContent());

        return ResponseEntity.ok(ApiUtils.success(response));

    }
}
