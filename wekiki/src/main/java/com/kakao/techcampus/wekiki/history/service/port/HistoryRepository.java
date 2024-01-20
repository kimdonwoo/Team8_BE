package com.kakao.techcampus.wekiki.history.service.port;

import com.kakao.techcampus.wekiki.history.domain.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HistoryRepository {
    Page<History> findAllByGroupMember(Long id, Pageable pageable);

    void save(History newHistory);

    Page<History> findHistoryWithMemberByPostId(Long postId, PageRequest pageRequest);

    List<History> findHistoryByPostId(Long postId, PageRequest pageRequest);
}
