package com.kakao.techcampus.wekiki.group.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupJPARepository extends JpaRepository<GroupEntity, Long> {
    @Query("select g from OfficialGroupEntity g")
    List<OfficialGroupEntity> findAllOfficialGroup();
    @Query("select g from UnOfficialOpenedGroupEntity g")
    List<UnOfficialOpenedGroupEntity> findAllUnOfficialOpenGroup();
    @Query("SELECT g FROM OfficialGroupEntity g WHERE g.groupName LIKE CONCAT('%', :keyword, '%')")
    Page<OfficialGroupEntity> findOfficialGroupsByKeyword(@Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT g FROM UnOfficialOpenedGroupEntity g WHERE g.groupName LIKE CONCAT('%', :keyword, '%')")
    Page<UnOfficialOpenedGroupEntity> findUnOfficialOpenedGroupsByKeyword(@Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT g FROM UnOfficialOpenedGroupEntity g WHERE g.id = :id")
    Optional<UnOfficialOpenedGroupEntity> findUnOfficialOpenedGroupById(@Param("id") Long id);
}
