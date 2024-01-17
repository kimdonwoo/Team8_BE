package com.kakao.techcampus.wekiki.report.service.port;

import com.kakao.techcampus.wekiki.history.domain.History;
import com.kakao.techcampus.wekiki.report.domain.Report;

import java.util.List;

public interface ReportRepository {
    void deleteReportsByHistoryInQuery(List<History> historys);
    Report save(Report report);
}
