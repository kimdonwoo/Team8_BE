package com.kakao.techcampus.wekiki.report.infrastructure;

import com.kakao.techcampus.wekiki.history.domain.History;
import com.kakao.techcampus.wekiki.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportJPARepository extends JpaRepository<Report, Long> {

    @Modifying
    @Query("DELETE FROM Report r WHERE r.history IN :historys")
    void deleteReportsByHistoryInQuery(@Param("historys") List<History> historys);
}
