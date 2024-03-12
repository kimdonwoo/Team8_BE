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

    @OneToMany(mappedBy = "postEntity",orphanRemoval = true)
    private List<HistoryEntity> historyEntities = new ArrayList<>();

    @OneToMany(mappedBy = "postEntity",orphanRemoval = true)
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

    public Post toPureModel(){
        return Post.builder()
                .id(id)
                .orders(orders)
                .title(title)
                .content(content)
                .created_at(created_at)
                .build();
    }

    public Post toModelWithParent(){
        return Post.builder()
                .id(id)
                .parent(this.parent == null ? null : this.parent.toPureModel())
                .orders(orders)
                .title(title)
                .content(content)
                .created_at(created_at)
                .build();
    }


    public Post toModelWihPageInfo(){
        return Post.builder()
                .id(id)
                .orders(orders)
                .pageInfo(pageInfoEntity.toPureModel())
                .title(title)
                .content(content)
                .created_at(created_at)
                .build();
    }

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

    public static PostEntity create(Post post){
        return PostEntity.builder()
                .parent(post.getParent() == null ? null : PostEntity.fromPureModel(post.getParent()))
                .orders(post.getOrders())
                .groupMemberEntity(GroupMemberEntity.fromPureModelWithId(post.getGroupMember()))
                .pageInfoEntity(PageInfoEntity.fromModelWithPosts(post.getPageInfo()))
                .title(post.getTitle())
                .content(post.getContent())
                .created_at(post.getCreated_at())
                .build();
    }

    public static PostEntity fromPureModel(Post post){
        return PostEntity.builder()
                .id(post.getId())
                .orders(post.getOrders())
                .title(post.getTitle())
                .content(post.getContent())
                .created_at(post.getCreated_at())
                .build();
    }

    public static PostEntity fromModelWithComments(Post post){
        return PostEntity.builder()
                .id(post.getId())
                .orders(post.getOrders())
                .title(post.getTitle())
                .commentEntities(new ArrayList<>())
                .content(post.getContent())
                .created_at(post.getCreated_at())
                .build();
    }

    public static PostEntity fromModelWithHisotries(Post post){
        return PostEntity.builder()
                .id(post.getId())
                .orders(post.getOrders())
                .title(post.getTitle())
                .historyEntities(new ArrayList<>())
                .content(post.getContent())
                .created_at(post.getCreated_at())
                .build();
    }

    public void addCommentEntity(CommentEntity commentEntity){
        this.commentEntities.add(commentEntity);
        commentEntity.setPostEntity(this);
    }

    public void update(Post updatedPost){
        this.title = updatedPost.getTitle();
        this.content = updatedPost.getContent();
        this.orders = updatedPost.getOrders();

    }

}
