package com.kakao.techcampus.wekiki.post.infrastructure;

import com.kakao.techcampus.wekiki.pageInfo.infrastructure.PageInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostJPARepository extends JpaRepository<PostEntity, Long> {

    @Query("SELECT po FROM PostEntity po WHERE po.pageInfoEntity.id = :pageId AND po.orders >= :orders")
    List<PostEntity> findPostsByPageIdAndOrderGreaterThan(
            @Param("pageId") Long pageId,
            @Param("orders") int orders
    );

    @Query("SELECT po FROM PostEntity po JOIN FETCH po.pageInfoEntity WHERE po.id = :postId ")
    Optional<PostEntity> findPostWithPageFromPostId(@Param("postId") Long postId);

    boolean existsByPageInfoEntityId(Long pageInfoId);

    boolean existsByParentId(Long parentId);


    @Query("SELECT po FROM PostEntity po WHERE po.pageInfoEntity IN (:pages) AND po.orders = 1")
    List<PostEntity> findPostInPages(@Param("pages") List<PageInfoEntity> pages);
}
