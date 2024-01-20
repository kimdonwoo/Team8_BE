package com.kakao.techcampus.wekiki.history.infrastructure;

import com.kakao.techcampus.wekiki.history.domain.History;
import com.kakao.techcampus.wekiki.history.service.port.HistoryRepository;
import com.kakao.techcampus.wekiki.post.infrastructure.PostEntity;
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
        return historyJPARepository.findAllByGroupMember(id,pageable).map(HistoryEntity::toModel);
    }

    @Override
    public void save(History newHistory) {
        HistoryEntity historyEntity = HistoryEntity.fromModel(newHistory);
        PostEntity.fromModel(newHistory.getPost()).addHistoryEntity(historyEntity);
        historyJPARepository.save(HistoryEntity.fromModel(newHistory));
    }

    @Override
    public Page<History> findHistoryWithMemberByPostId(Long postId, PageRequest pageRequest) {
        return historyJPARepository.findHistoryWithMemberByPostId(postId,pageRequest).map(HistoryEntity::toModel);
    }

    @Override
    public List<History> findHistoryByPostId(Long postId, PageRequest pageRequest) {
        return historyJPARepository.findHistoryByPostId(postId,pageRequest)
                .stream().map(HistoryEntity::toModel).toList();
    }
}
