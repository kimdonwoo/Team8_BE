package com.kakao.techcampus.wekiki.report.domain;

import com.kakao.techcampus.wekiki.group.domain.GroupMember;
import com.kakao.techcampus.wekiki.history.domain.History;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Report {

    private final Long id;
    private final GroupMember fromMember;
    private final History history;
    private final String content;
    private final LocalDateTime created_at;

    @Builder
    public Report(Long id, GroupMember fromMember, History history, String content, LocalDateTime created_at) {
        this.id = id;
        this.fromMember = fromMember;
        this.history = history;
        this.content = content;
        this.created_at = created_at;
    }
}
