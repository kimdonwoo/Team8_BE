package com.kakao.techcampus.wekiki.comment.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
public class CommentRequest {

    @Getter @Setter
    public static class createComment{
        @NotBlank(message = "댓글 내용을 입력해주세요.")
        @Size(max=200, message = "댓글 작성은 200자 이내로 가능합니다.")
        private String content;
    }

    @Getter @Setter
    public static class updateComment{
        @NotBlank(message = "수정할 댓글 내용을 입력해주세요.")
        @Size(max=200, message = "댓글 작성은 200자 이내로 가능합니다.")
        private String content;
    }
}
