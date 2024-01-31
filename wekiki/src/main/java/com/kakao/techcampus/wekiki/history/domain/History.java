package com.kakao.techcampus.wekiki.history.domain;

import com.kakao.techcampus.wekiki.group.domain.GroupMember;
import com.kakao.techcampus.wekiki.post.domain.Post;

import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class History {

    private final Long id;
    private final GroupMember groupMember;
    private final Post post;
    private final String title;
    private final String content;
    private final LocalDateTime created_at;

    @Builder
    public History(Long id, GroupMember groupMember, Post post, String title, String content, LocalDateTime created_at) {
        this.id = id;
        this.groupMember = groupMember;
        this.post = post;
        this.title = title;
        this.content = content;
        this.created_at = created_at;
    }

    public static History from(Post post, GroupMember activeGroupMember){
        return History.builder()
                .groupMember(activeGroupMember)
                .post(post)
                .title(post.getTitle())
                .content(post.getContent())
                .created_at(post.getCreated_at())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return Objects.equals(id, history.id) && Objects.equals(groupMember, history.groupMember) && Objects.equals(post, history.post) && Objects.equals(title, history.title) && Objects.equals(content, history.content) && Objects.equals(created_at, history.created_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupMember, post, title, content, created_at);
    }
}
