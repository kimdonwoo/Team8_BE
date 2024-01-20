package com.kakao.techcampus.wekiki.comment.domain;

import com.kakao.techcampus.wekiki.group.domain.GroupMember;
import com.kakao.techcampus.wekiki.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class Comment {

    private final Long id;
    private final GroupMember groupMember;
    private final Post post;
    private final String content;
    private final LocalDateTime created_at;

    @Builder
    public Comment(Long id, GroupMember groupMember, Post post, String content, LocalDateTime created_at) {
        this.id = id;
        this.groupMember = groupMember;
        this.post = post;
        this.content = content;
        this.created_at = created_at;
    }

    public Comment updateContent(String newContent){
        return Comment.builder()
                .id(id)
                .groupMember(groupMember)
                .post(post)
                .content(newContent)
                .created_at(created_at)
                .build();
    }

    public static Comment from(GroupMember activeGroupMember, Post post, String content){
        return Comment.builder()
                .groupMember(activeGroupMember)
                .post(post)
                .content(content)
                .created_at(LocalDateTime.now())
                .build();
    }

}
