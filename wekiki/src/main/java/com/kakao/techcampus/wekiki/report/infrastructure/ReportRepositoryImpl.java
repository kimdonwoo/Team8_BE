package com.kakao.techcampus.wekiki.report.infrastructure;

import com.kakao.techcampus.wekiki.history.domain.History;
import com.kakao.techcampus.wekiki.history.infrastructure.HistoryEntity;
import com.kakao.techcampus.wekiki.report.domain.Report;
import com.kakao.techcampus.wekiki.report.service.port.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepository {

    private final ReportJPARepository reportJPARepository;

    @Override
    public void deleteReportsByHistoryInQuery(List<History> historys) {
        reportJPARepository.deleteReportsByHistoryInQuery(historys.stream().map(HistoryEntity::fromModel).toList());
    }

    @Override
    public Report save(Report report) {
        return reportJPARepository.save(ReportEntity.fromModel(report)).toModel();
    }
}
