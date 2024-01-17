package com.kakao.techcampus.wekiki.group.infrastructure;

import com.kakao.techcampus.wekiki.group.domain.Group;
import com.kakao.techcampus.wekiki.group.domain.OfficialGroup;
import com.kakao.techcampus.wekiki.group.domain.UnOfficialOpenedGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupJPARepository extends JpaRepository<Group, Long> {
    @Query("select g from OfficialGroup g")
    List<OfficialGroup> findAllOfficialGroup();
    @Query("select g from UnOfficialOpenedGroup g")
    List<UnOfficialOpenedGroup> findAllUnOfficialOpenGroup();
    @Query("SELECT g FROM OfficialGroup g WHERE g.groupName LIKE CONCAT('%', :keyword, '%')")
    Page<OfficialGroup> findOfficialGroupsByKeyword(@Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT g FROM UnOfficialOpenedGroup g WHERE g.groupName LIKE CONCAT('%', :keyword, '%')")
    Page<UnOfficialOpenedGroup> findUnOfficialOpenedGroupsByKeyword(@Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT g FROM UnOfficialOpenedGroup g WHERE g.id = :id")
    Optional<UnOfficialOpenedGroup> findUnOfficialOpenedGroupById(@Param("id") Long id);
}
