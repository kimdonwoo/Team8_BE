package com.kakao.techcampus.wekiki.group.service.port;

import com.kakao.techcampus.wekiki.group.domain.Group;
import com.kakao.techcampus.wekiki.group.domain.OfficialGroup;
import com.kakao.techcampus.wekiki.group.domain.UnOfficialOpenedGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository {

    List<OfficialGroup> findAllOfficialGroup();
    List<UnOfficialOpenedGroup> findAllUnOfficialOpenGroup();
    Page<OfficialGroup> findOfficialGroupsByKeyword( String keyword, Pageable pageable);
    Page<UnOfficialOpenedGroup> findUnOfficialOpenedGroupsByKeyword(String keyword, Pageable pageable);
    Optional<UnOfficialOpenedGroup> findUnOfficialOpenedGroupById(Long id);

    Optional<Group> findById(Long groupId);

    void save(Group group);

    void delete(Group group);
}
