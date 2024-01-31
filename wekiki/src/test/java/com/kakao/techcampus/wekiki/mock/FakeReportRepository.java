package com.kakao.techcampus.wekiki.mock;

import com.kakao.techcampus.wekiki.history.domain.History;
import com.kakao.techcampus.wekiki.report.domain.Report;
import com.kakao.techcampus.wekiki.report.service.port.ReportRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class FakeReportRepository implements ReportRepository {

    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final ArrayList<Report> data = new ArrayList<>();
    @Override
    public void deleteReportsByHistoryInQuery(List<History> historys) {
        data.removeIf(item -> historys.contains(item.getHistory()));
    }

    @Override
    public Report save(Report report) {
        if(report.getId() == null || report.getId() == 0){
            // 새로운 유저이면 save
            Report newReport = Report.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .fromMember(report.getFromMember())
                    .history(report.getHistory())
                    .content(report.getContent())
                    .created_at(report.getCreated_at())
                    .build();
            data.add(newReport);
            return newReport;
        }else{
            // 아니면 update
            data.removeIf(item -> Objects.equals(item.getId(), report.getId()));
            data.add(report);
            return report;
        }
    }


}
