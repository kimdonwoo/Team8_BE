package com.kakao.techcampus.wekiki.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberJPARepository extends JpaRepository<MemberEntity, Long> {
    @Query("select m from MemberEntity m where m.email=:email")
    Optional<MemberEntity> findByEmail(@Param("email") String email);
}
