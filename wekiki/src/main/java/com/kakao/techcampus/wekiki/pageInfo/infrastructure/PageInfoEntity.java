package com.kakao.techcampus.wekiki.pageInfo.infrastructure;

import com.kakao.techcampus.wekiki.group.infrastructure.GroupEntity;
import com.kakao.techcampus.wekiki.pageInfo.domain.PageInfo;
import com.kakao.techcampus.wekiki.post.infrastructure.PostEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "pageinfo_tb")
public class PageInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private GroupEntity groupEntity;
    private String pageName;

    @OneToMany(mappedBy = "pageInfoEntity", cascade = CascadeType.REMOVE)
    private List<PostEntity> postEntities = new ArrayList<>();
    private int goodCount;
    private int badCount;
    private int viewCount;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

//    @Version
//    private Long version;
    @Builder
    public PageInfoEntity(Long id, GroupEntity groupEntity, String pageName, List<PostEntity> postEntities, int goodCount, int badCount, int viewCount, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.groupEntity = groupEntity;
        this.pageName = pageName;
        this.postEntities = postEntities;
        this.goodCount = goodCount;
        this.badCount = badCount;
        this.viewCount = viewCount;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public static PageInfoEntity fromModel(PageInfo pageInfo){
        return PageInfoEntity.builder()
                .id(pageInfo.getId())
                .groupEntity(GroupEntity.fromModel(pageInfo.getGroup()))
                .pageName(pageInfo.getPageName())
                .postEntities(pageInfo.getPosts().stream().map(p->PostEntity.fromModel(p)).toList())
                .goodCount(pageInfo.getGoodCount())
                .badCount(pageInfo.getBadCount())
                .viewCount(pageInfo.getViewCount())
                .created_at(pageInfo.getCreated_at())
                .updated_at(pageInfo.getUpdated_at())
                .build();
    }

    public PageInfo toModel(){
        return PageInfo.builder()
                .id(id)
                .group(groupEntity.toModel())
                .pageName(pageName)
                .posts(postEntities.stream().map(PostEntity::toModel).toList())
                .goodCount(goodCount)
                .badCount(badCount)
                .viewCount(viewCount)
                .created_at(created_at)
                .updated_at(updated_at)
                .build();
    }


    public void addPostEntity(PostEntity postEntity){
        this.postEntities.add(postEntity);
        postEntity.setPageInfoEntity(this);
    }

}
