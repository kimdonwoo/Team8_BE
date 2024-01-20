package com.kakao.techcampus.wekiki.post.domain;

import com.kakao.techcampus.wekiki.comment.domain.Comment;
import com.kakao.techcampus.wekiki.group.domain.GroupMember;
import com.kakao.techcampus.wekiki.history.domain.History;
import com.kakao.techcampus.wekiki.pageInfo.domain.PageInfo;
import com.kakao.techcampus.wekiki.post.controller.request.PostRequest;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Post {
    private final Long id;
    private final Post parent;
    private final int orders;
    private final GroupMember groupMember;
    private final PageInfo pageInfo;
    private final List<History> historys;
    private final List<Comment> comments;
    private final String title;
    private final String content;
    private final LocalDateTime created_at;

    @Builder
    public Post(Long id, Post parent, int orders, GroupMember groupMember, PageInfo pageInfo, List<History> historys, List<Comment> comments, String title, String content, LocalDateTime created_at) {
        this.id = id;
        this.parent = parent;
        this.orders = orders;
        this.groupMember = groupMember;
        this.pageInfo = pageInfo;
        this.historys = historys;
        this.comments = comments;
        this.title = title;
        this.content = content;
        this.created_at = created_at;
    }

    public Post plusOrder(){
        return Post.builder()
                .id(id)
                .parent(parent)
                .orders(orders+1)
                .groupMember(groupMember)
                .pageInfo(pageInfo)
                .historys(historys)
                .comments(comments)
                .title(title)
                .content(content)
                .created_at(created_at)
                .build();
    }

    public Post minusOrder(){
        return Post.builder()
                .id(id)
                .parent(parent)
                .orders(orders-1)
                .groupMember(groupMember)
                .pageInfo(pageInfo)
                .historys(historys)
                .comments(comments)
                .title(title)
                .content(content)
                .created_at(created_at)
                .build();
    }

    public Post modifyPost(GroupMember groupMember, String title, String content){
        return Post.builder()
                .id(this.id)
                .parent(this.parent)
                .orders(this.orders)
                .groupMember(groupMember)
                .pageInfo(this.pageInfo)
                .historys(this.historys)
                .comments(this.comments)
                .title(title)
                .content(content)
                .created_at(this.created_at)
                .build();
    }

    public static Post from(PostRequest.createPostDTO request, Post parent, GroupMember activeGroupMember, PageInfo pageInfo){
        return Post.builder()
                .parent(parent)
                .orders(request.getOrder())
                .groupMember(activeGroupMember)
                .pageInfo(pageInfo) // history랑 comment는 ?
                .title(request.getTitle())
                .content(request.getContent())
                .created_at(LocalDateTime.now())
                .build();
    }
}