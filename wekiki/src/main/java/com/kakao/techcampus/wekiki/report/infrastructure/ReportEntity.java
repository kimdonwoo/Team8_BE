package com.kakao.techcampus.wekiki.report.infrastructure;

import com.kakao.techcampus.wekiki.group.infrastructure.GroupMemberEntity;
import com.kakao.techcampus.wekiki.history.infrastructure.HistoryEntity;
import com.kakao.techcampus.wekiki.report.domain.Report;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "report_tb")
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private GroupMemberEntity fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private HistoryEntity historyEntity;
    private String content;

    private LocalDateTime created_at;

    @Builder
    public ReportEntity(Long id, GroupMemberEntity groupMemberEntity, HistoryEntity historyEntity, String content, LocalDateTime created_at) {
        this.id = id;
        this.fromMember = groupMemberEntity;
        this.historyEntity = historyEntity;
        this.content = content;
        this.created_at = created_at;
    }

    public Report toModel(){
        return Report.builder()
                .id(id)
                .fromMember(fromMember.toModel())
                .history(historyEntity.toModel())
                .content(content)
                .created_at(created_at)
                .build();
    }

    public Report toPureModel(){
        return Report.builder()
                .id(id)
                .content(content)
                .created_at(created_at)
                .build();
    }

    public static ReportEntity fromModel(Report report){
        return ReportEntity.builder()
                .groupMemberEntity(GroupMemberEntity.fromPureModelWithId(report.getFromMember()))
                .historyEntity(HistoryEntity.fromPureModel(report.getHistory()))
                .content(report.getContent())
                .created_at(report.getCreated_at())
                .build();
    }

}
