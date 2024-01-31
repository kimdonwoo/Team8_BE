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
        return groupJPARepository.findAllOfficialGroup().stream().map(OfficialGroupEntity::toModel).toList();
    }

    @Override
    public List<UnOfficialOpenedGroup> findAllUnOfficialOpenGroup() {
        return groupJPARepository.findAllUnOfficialOpenGroup().stream().map(UnOfficialOpenedGroupEntity::toModel).toList();
    }

    @Override
    public Page<OfficialGroup> findOfficialGroupsByKeyword(String keyword, Pageable pageable) {
        return groupJPARepository.findOfficialGroupsByKeyword(keyword,pageable).map(OfficialGroupEntity::toModel);
    }

    @Override
    public Page<UnOfficialOpenedGroup> findUnOfficialOpenedGroupsByKeyword(String keyword, Pageable pageable) {
        return groupJPARepository.findUnOfficialOpenedGroupsByKeyword(keyword,pageable).map(UnOfficialOpenedGroupEntity::toModel);
    }

    @Override
    public Optional<UnOfficialOpenedGroup> findUnOfficialOpenedGroupById(Long id) {
        return groupJPARepository.findUnOfficialOpenedGroupById(id).map(UnOfficialOpenedGroupEntity::toModel);
    }

    @Override
    public Optional<Group> findById(Long groupId) {
        return groupJPARepository.findById(groupId).map(GroupEntity::toModel);
    }

    @Override
    public Group save(Group group) {
        return groupJPARepository.save(GroupEntity.fromModel(group)).toModel();
    }

    @Override
    public void delete(Group group) {
        groupJPARepository.delete(GroupEntity.fromModel(group));
    }
}
