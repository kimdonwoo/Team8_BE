package com.kakao.techcampus.wekiki.history.infrastructure;

import com.kakao.techcampus.wekiki.history.domain.History;
import com.kakao.techcampus.wekiki.history.service.port.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HistoryRepositoryImpl implements HistoryRepository {

    private final HistoryJPARepository historyJPARepository;

    @Override
    public Page<History> findAllByGroupMember(Long id, Pageable pageable) {
        return historyJPARepository.findAllByGroupMember(id,pageable);
    }

    @Override
    public void save(History newHistory) {
        historyJPARepository.save(newHistory);
    }

    @Override
    public Page<History> findHistoryWithMemberByPostId(Long postId, PageRequest of) {
        return historyJPARepository.findHistoryWithMemberByPostId(postId,of);
    }

    @Override
    public List<History> findHistoryByPostId(Long postId, PageRequest of) {
        return historyJPARepository.findHistoryByPostId(postId,of);
    }
}
