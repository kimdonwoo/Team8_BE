package com.kakao.techcampus.wekiki.report.infrastructure;

import com.kakao.techcampus.wekiki.history.infrastructure.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportJPARepository extends JpaRepository<ReportEntity, Long> {

    @Modifying
    @Query("DELETE FROM ReportEntity r WHERE r.historyEntity IN :historys")
    void deleteReportsByHistoryInQuery(@Param("historys") List<HistoryEntity> historyEntities);
}
