package com.kakao.techcampus.wekiki.member.service.port;

import com.kakao.techcampus.wekiki.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findById(Long currentMember);

    Optional<Member> findByEmail(String email);

    Member save(Member member);

    void deleteModify(Member member);
}
