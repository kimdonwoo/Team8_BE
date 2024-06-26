package com.kakao.techcampus.wekiki.comment.infrastructure;

import com.kakao.techcampus.wekiki.comment.domain.Comment;
import com.kakao.techcampus.wekiki.group.infrastructure.GroupMemberEntity;
import com.kakao.techcampus.wekiki.post.infrastructure.PostEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment_tb")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private GroupMemberEntity groupMemberEntity;
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private PostEntity postEntity;
    @Column(columnDefinition = "TEXT")
    private String content;
    private LocalDateTime created_at;

    @Builder
    public CommentEntity(Long id, GroupMemberEntity groupMemberEntity, PostEntity postEntity, String content, LocalDateTime created_at) {
        this.id = id;
        this.groupMemberEntity = groupMemberEntity;
        this.postEntity = postEntity;
        this.content = content;
        this.created_at = created_at;
    }

    public Comment toModel(){
        return Comment.builder()
                .id(id)
                .groupMember(groupMemberEntity.toModel())
                .post(postEntity.toModel())
                .content(content)
                .created_at(created_at)
                .build();
    }

    public Comment toModelWithGroupMember(){
        return Comment.builder()
                .id(id)
                .groupMember(groupMemberEntity.toPureModel())
                .content(content)
                .created_at(created_at)
                .build();
    }

    public Comment toPureModel(){
        return Comment.builder()
                .id(id)
                .content(content)
                .created_at(created_at)
                .build();
    }

    public static CommentEntity fromPureModel(Comment comment){
        return CommentEntity.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .created_at(comment.getCreated_at())
                .build();
    }


    public static CommentEntity createFromModel(Comment comment){
        return CommentEntity.builder()
                .groupMemberEntity(GroupMemberEntity.fromPureModelWithId(comment.getGroupMember()))
                .postEntity(PostEntity.fromModelWithComments(comment.getPost()))
                .content(comment.getContent())
                .created_at(comment.getCreated_at())
                .build();
    }


    public void update(Comment comment){
        this.content = comment.getContent();
    }

}