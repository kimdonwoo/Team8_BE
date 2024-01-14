package com.kakao.techcampus.wekiki.domain.comment;


import com.kakao.techcampus.wekiki._core.utils.ApiUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.kakao.techcampus.wekiki._core.utils.SecurityUtils.currentMember;

@RestController
@RequestMapping("/group/{groupid}")
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    // 댓글 조회하기
    @GetMapping("/post/{postid}/comment")
    public ResponseEntity<ApiUtils.ApiResult<CommentResponse.getCommentDTO>> getComment(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                        @Positive(message = "유효하지 않은 postID입니다.") @PathVariable Long postid,
                                                                                        @Positive(message = "유효하지 않은 pageID입니다.") @RequestParam(value = "page", defaultValue = "1") int page){

        CommentResponse.getCommentDTO response = commentService.getComment(currentMember(), groupid, postid, page-1);

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // 댓글 쓰기
    @PostMapping("/post/{postid}/comment")
    public ResponseEntity<ApiUtils.ApiResult<CommentResponse.createCommentDTO>> createComment(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                              @Positive(message = "유효하지 않은 postID입니다.") @PathVariable Long postid,
                                                                                              @Valid @RequestBody CommentRequest.createComment request){

        CommentResponse.createCommentDTO response = commentService.createComment(currentMember(), groupid, postid, request.getContent());

        return ResponseEntity.ok(ApiUtils.success(response));
    }

    // 댓글 삭제하기
    @DeleteMapping("/comment/{commentid}")
    public ResponseEntity<ApiUtils.ApiResult<CommentResponse.deleteCommentDTO>> deleteComment(@Positive(message = "유효하지 않은 groupID입니다.") Long groupid,
                                                                                              @Positive(message = "유효하지 않은 commentID입니다.") @PathVariable Long commentid){

        CommentResponse.deleteCommentDTO response = commentService.deleteComment(currentMember(), groupid, commentid);

        return ResponseEntity.ok(ApiUtils.success(response));

    }

    // 댓글 수정하기
    @PatchMapping("/comment/{commentid}")
    public ResponseEntity<ApiUtils.ApiResult<CommentResponse.updateCommentDTO>> updateComment(@Positive(message = "유효하지 않은 groupID입니다.") @PathVariable Long groupid,
                                                                                              @Positive(message = "유효하지 않은 commentID입니다.") @PathVariable Long commentid,
                                                                                              @Valid @RequestBody CommentRequest.updateComment request){

        CommentResponse.updateCommentDTO response = commentService.updateComment(currentMember(), groupid, commentid, request.getContent());

        return ResponseEntity.ok(ApiUtils.success(response));

    }
}
