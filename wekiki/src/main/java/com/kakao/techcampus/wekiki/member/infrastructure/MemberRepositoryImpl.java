package com.kakao.techcampus.wekiki.member.infrastructure;

import com.kakao.techcampus.wekiki.group.infrastructure.GroupMemberEntity;
import com.kakao.techcampus.wekiki.member.domain.Member;
import com.kakao.techcampus.wekiki.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJPARepository memberJPARepository;

    @Override
    public Optional<Member> findById(Long currentMember) {
        return memberJPARepository.findById(currentMember).map(MemberEntity::toModel);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJPARepository.findByEmail(email).map(MemberEntity::toModel);
    }

    @Override
    public void save(Member member) {
        memberJPARepository.save(MemberEntity.fromModel(member));
    }

    @Override
    public void deleteModify(Member member) {
        MemberEntity memberEntity = MemberEntity.fromModel(member);
        for(GroupMemberEntity gme :memberEntity.getGroupMemberEntities()){
            gme.deleteGroupMember();
        }
        memberJPARepository.save(memberEntity);
    }
}
