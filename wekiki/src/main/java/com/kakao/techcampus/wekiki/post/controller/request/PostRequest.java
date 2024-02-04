package com.kakao.techcampus.wekiki.post.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class PostRequest {
    @Getter
    @Builder
    public static class createPostDTO {

        @Positive(message = "유효하지 않은 pageID입니다.")
        private Long pageId;

        @Positive(message = "유효하지 않은 parentPostID입니다.")
        private Long parentPostId;

        @PositiveOrZero(message = "유효하지 않은 order입니다.")
        private int order;

        @NotBlank(message = "게시글 제목을 입력해주세요.")
        @Size(max=200, message = "게시글 제목은 최대 200자까지 가능합니다.")
        private String title;

        @Size(max=10000, message = "게시글 내용은 최대 10000자 이내로 가능합니다.")
        private String content;

    }

    @Getter
    @Builder
    public static class modifyPostDTO{
        @Positive(message = "유효하지 않은 postID입니다.")
        private Long postId;

        @NotBlank(message = "게시글 제목을 입력해주세요.")
        @Size(max=200, message = "게시글 제목은 최대 200자까지 가능합니다.")
        private String title;

        @Size(max=10000, message = "게시글 내용은 최대 10000자 이내로 가능합니다.")
        private String content;
    }

    @Getter
    @Builder
    public static class createReportDTO{
        @Positive(message = "유효하지 않은 postID입니다.")
        private Long postId;

        @Size(max=500, message = "신고글 내용은 최대 500자 이내로 가능합니다.")
        private String content;
    }
}
