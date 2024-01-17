package com.kakao.techcampus.wekiki.member.infrastructure;

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
        return memberJPARepository.findById(currentMember);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJPARepository.findByEmail(email);
    }

    @Override
    public void save(Member member) {
        memberJPARepository.save(member);
    }

    @Override
    public void delete(Member member) {
        memberJPARepository.delete(member);
    }
}
