package com.kakao.techcampus.wekiki.history.infrastructure;

import com.kakao.techcampus.wekiki.group.infrastructure.GroupMemberEntity;
import com.kakao.techcampus.wekiki.history.domain.History;
import com.kakao.techcampus.wekiki.post.infrastructure.PostEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "history_tb")
public class HistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private GroupMemberEntity groupMemberEntity;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="POST_ID")
    private PostEntity postEntity;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;
    private LocalDateTime created_at;

    @Builder
    public HistoryEntity(Long id, GroupMemberEntity groupMemberEntity, PostEntity postEntity, String title, String content, LocalDateTime created_at) {
        this.id = id;
        this.groupMemberEntity = groupMemberEntity;
        this.postEntity = postEntity;
        this.title = title;
        this.content = content;
        this.created_at = created_at;
    }

    public static HistoryEntity fromModel(History history){
        return HistoryEntity.builder()
                .id(history.getId())
                .groupMemberEntity(GroupMemberEntity.fromModel(history.getGroupMember()))
                .postEntity(PostEntity.fromModel(history.getPost()))
                .title(history.getTitle())
                .content(history.getContent())
                .created_at(history.getCreated_at())
                .build();
    }

    public History toModel(){
        return History.builder()
                .id(id)
                .groupMember(groupMemberEntity.toModel())
                .post(postEntity.toModel())
                .title(title)
                .content(content)
                .created_at(created_at)
                .build();
    }



    public void updateGroupMember(GroupMemberEntity groupMemberEntity) {
        this.groupMemberEntity = groupMemberEntity;
    }
}
