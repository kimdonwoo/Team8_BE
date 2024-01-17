package com.kakao.techcampus.wekiki.group.infrastructure;

import com.kakao.techcampus.wekiki.group.domain.Group;
import com.kakao.techcampus.wekiki.group.domain.OfficialGroup;
import com.kakao.techcampus.wekiki.group.domain.UnOfficialOpenedGroup;
import com.kakao.techcampus.wekiki.group.service.port.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GroupRepositoryImpl implements GroupRepository {

    private final GroupJPARepository groupJPARepository;


    @Override
    public List<OfficialGroup> findAllOfficialGroup() {
        return groupJPARepository.findAllOfficialGroup();
    }

    @Override
    public List<UnOfficialOpenedGroup> findAllUnOfficialOpenGroup() {
        return groupJPARepository.findAllUnOfficialOpenGroup();
    }

    @Override
    public Page<OfficialGroup> findOfficialGroupsByKeyword(String keyword, Pageable pageable) {
        return groupJPARepository.findOfficialGroupsByKeyword(keyword,pageable);
    }

    @Override
    public Page<UnOfficialOpenedGroup> findUnOfficialOpenedGroupsByKeyword(String keyword, Pageable pageable) {
        return groupJPARepository.findUnOfficialOpenedGroupsByKeyword(keyword,pageable);
    }

    @Override
    public Optional<UnOfficialOpenedGroup> findUnOfficialOpenedGroupById(Long id) {
        return groupJPARepository.findUnOfficialOpenedGroupById(id);
    }

    @Override
    public Optional<Group> findById(Long groupId) {
        return groupJPARepository.findById(groupId);
    }

    @Override
    public void save(Group group) {
        groupJPARepository.save(group);
    }

    @Override
    public void delete(Group group) {
        groupJPARepository.delete(group);
    }
}
