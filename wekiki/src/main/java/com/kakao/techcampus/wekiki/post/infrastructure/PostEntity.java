package com.kakao.techcampus.wekiki.post.infrastructure;

import com.kakao.techcampus.wekiki.comment.infrastructure.CommentEntity;
import com.kakao.techcampus.wekiki.group.infrastructure.GroupMemberEntity;
import com.kakao.techcampus.wekiki.history.infrastructure.HistoryEntity;
import com.kakao.techcampus.wekiki.pageInfo.infrastructure.PageInfoEntity;
import com.kakao.techcampus.wekiki.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post_tb")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private PostEntity parent;

    private int orders;

    @ManyToOne(fetch = FetchType.LAZY)
    private GroupMemberEntity groupMemberEntity;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private PageInfoEntity pageInfoEntity;

    @OneToMany(mappedBy = "postEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoryEntity> historyEntities = new ArrayList<>();

    @OneToMany(mappedBy = "postEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> commentEntities = new ArrayList<>();

    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private LocalDateTime created_at;

    @Builder
    public PostEntity(Long id, PostEntity parent, int orders, GroupMemberEntity groupMemberEntity, PageInfoEntity pageInfoEntity, List<HistoryEntity> historyEntities, List<CommentEntity> commentEntities, String title, String content, LocalDateTime created_at) {
        this.id = id;
        this.parent = parent;
        this.orders = orders;
        this.groupMemberEntity = groupMemberEntity;
        this.pageInfoEntity = pageInfoEntity;
        this.historyEntities = historyEntities;
        this.commentEntities = commentEntities;
        this.title = title;
        this.content = content;
        this.created_at = created_at;
    }
    /*
        TODO : 이렇게 toModel을 하나만 가지고 Post로 바꿔버리면
            post가 별로 필요없는 상황에서도 history 정보나 comment 정보 다 긁어올듯?
            흠... toModel을 따로 사용하지 않고 HistoryRepositoryImpl에서 builder를 사용하는 것은 ?

     */
    public Post toModel(){
        return Post.builder()
                .id(id)
                .parent(parent.toModel())
                .orders(orders)
                .groupMember(groupMemberEntity.toModel())
                .pageInfo(pageInfoEntity.toModel())
                .historys(historyEntities.stream().map(HistoryEntity::toModel).toList())
                .comments(commentEntities.stream().map(CommentEntity::toModel).toList())
                .title(title)
                .content(content)
                .created_at(created_at)
                .build();
    }

    public static PostEntity fromModel(Post post){
        return PostEntity.builder()
                .id(post.getId())
                .parent(PostEntity.fromModel(post.getParent()))
                .orders(post.getOrders())
                .groupMemberEntity(GroupMemberEntity.fromModel(post.getGroupMember()))
                .pageInfoEntity(PageInfoEntity.fromModel(post.getPageInfo()))
                .historyEntities(post.getHistorys().stream().map(HistoryEntity::fromModel).toList())
                .commentEntities(post.getComments().stream().map(CommentEntity::fromModel).toList())
                .title(post.getTitle())
                .content(post.getContent())
                .created_at(post.getCreated_at())
                .build();
    }

    public void addCommentEntity(CommentEntity commentEntity){
        this.commentEntities.add(commentEntity);
        commentEntity.setPostEntity(this);
    }

    public void addHistoryEntity(HistoryEntity historyEntity){
        this.historyEntities.add(historyEntity);
        historyEntity.setPostEntity(this);
    }

}
