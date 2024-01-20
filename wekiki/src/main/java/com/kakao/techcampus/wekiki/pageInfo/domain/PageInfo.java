package com.kakao.techcampus.wekiki.pageInfo.domain;

import com.kakao.techcampus.wekiki.group.domain.Group;
import com.kakao.techcampus.wekiki.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PageInfo {
    private final Long id;
    private final Group group;
    private final String pageName;
    private final List<Post> posts;
    private final int goodCount;
    private final int badCount;
    private final int viewCount;
    private final LocalDateTime created_at;
    private final LocalDateTime updated_at;

    @Builder
    public PageInfo(Long id, Group group, String pageName, List<Post> posts, int goodCount, int badCount, int viewCount, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.group = group;
        this.pageName = pageName;
        this.posts = posts;
        this.goodCount = goodCount;
        this.badCount = badCount;
        this.viewCount = viewCount;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

//    public PageInfo createPage(){
//        PageInfo.builder()
//                .group(activeGroupMember.getGroup())
//                .pageName(title)
//                .goodCount(0)
//                .badCount(0)
//                .viewCount(0)
//                .created_at(LocalDateTime.now())
//                .updated_at(LocalDateTime.now())
//                .build();
//    }

    public PageInfo plusGoodCount(){
        return PageInfo.builder()
                .id(id)
                .group(group)
                .pageName(pageName)
                .posts(posts)
                .goodCount(goodCount+1)
                .badCount(badCount)
                .viewCount(viewCount)
                .created_at(created_at)
                .updated_at(updated_at)
                .build();
    }

    public PageInfo plusBadCount(){
        return PageInfo.builder()
                .id(id)
                .group(group)
                .pageName(pageName)
                .posts(posts)
                .goodCount(goodCount)
                .badCount(badCount+1)
                .viewCount(viewCount)
                .created_at(created_at)
                .updated_at(updated_at)
                .build();
    }

    public PageInfo updatePage(){
        return PageInfo.builder()
                .id(id)
                .group(group)
                .pageName(pageName)
                .posts(posts)
                .goodCount(goodCount)
                .badCount(badCount+1)
                .viewCount(viewCount)
                .created_at(created_at)
                .updated_at(LocalDateTime.now())
                .build();
    }

}
