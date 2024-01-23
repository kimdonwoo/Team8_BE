package com.kakao.techcampus.wekiki.page.service.service;


import com.kakao.techcampus.wekiki._core.error.exception.Exception404;
import com.kakao.techcampus.wekiki.group.domain.Group;
import com.kakao.techcampus.wekiki.group.domain.GroupMember;
import com.kakao.techcampus.wekiki.member.domain.Member;
import com.kakao.techcampus.wekiki.member.infrastructure.Authority;
import com.kakao.techcampus.wekiki.mock.*;
import com.kakao.techcampus.wekiki.pageInfo.controller.response.PageInfoResponse;
import com.kakao.techcampus.wekiki.pageInfo.domain.PageInfo;
import com.kakao.techcampus.wekiki.pageInfo.infrastructure.PageIndexGeneratorImpl;
import com.kakao.techcampus.wekiki.pageInfo.service.PageService;
import com.kakao.techcampus.wekiki.post.domain.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PageInfoServiceTest {

    private PageService pageService;
    private final LocalDateTime testTime = LocalDateTime.now();

    @BeforeEach
    void init(){
        PageIndexGeneratorImpl pageIndexGeneratorImpl = new PageIndexGeneratorImpl();
        FakePageInfoRepository fakePageInfoRepository = new FakePageInfoRepository();
        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeMemberRepository fakeMemberRepository = new FakeMemberRepository();
        FakeGroupMemberRepository fakeGroupMemberRepository = new FakeGroupMemberRepository();
        FakeGroupRepository fakeGroupRepository = new FakeGroupRepository();

        pageService = PageService.builder()
                .pageRepository(fakePageInfoRepository)
                .pageIndexGenerator(pageIndexGeneratorImpl)
                .groupRepository(fakeGroupRepository)
                .memberRepository(fakeMemberRepository)
                .groupMemberRepository(fakeGroupMemberRepository)
                .postRepository(fakePostRepository)
                .redisUtils(new FakeRedisUtils())
                .build();

        // 회원 2명, 그룹 1개, 그룹멤버 2개(둘다 가입되어 있는 상황)
        // 페이지 1개, 포스트 5개

        Member member1 = Member.builder()
                .name("TestMember1")
                .email("test1@naver.com")
                .password("1111")
                .created_at( testTime)
                //.groupMembers(groupMemberEntities.stream().map(GroupMemberEntity::toModel).toList())
                .authority(Authority.user)
                .build();
        Member member2 = Member.builder()
                .name("TestMember2")
                .email("test2@naver.com")
                .password("1111")
                .created_at( testTime)
                //.groupMembers(groupMemberEntities.stream().map(GroupMemberEntity::toModel).toList())
                .authority(Authority.user)
                .build();
        Member member3 = Member.builder()
                .name("TestMember3")
                .email("test3@naver.com")
                .password("1111")
                .created_at( testTime)
                //.groupMembers(groupMemberEntities.stream().map(GroupMemberEntity::toModel).toList())
                .authority(Authority.user)
                .build();

        Member savedMember1 = fakeMemberRepository.save(member1);
        Member savedMember2 = fakeMemberRepository.save(member2);
        Member savedMember3 = fakeMemberRepository.save(member3);

        Group group = Group.builder()
                .id(1L)
                .groupName("TestGroup")
                .groupProfileImage("s3/url")
                //.groupMembers(this.groupMemberEntities.stream().map(GroupMemberEntity::toModel).toList())
                .memberCount(0)
                .created_at( testTime)
                .build();

        GroupMember groupMember1 = GroupMember.builder()
                .member(savedMember1)
                .group(group)
                .nickName("TestMember1의 groupMember")
                .memberLevel(0)
                .created_at(testTime)
                .activeStatus(true)
                .build();
        GroupMember groupMember2 = GroupMember.builder()
                .member(savedMember2)
                .group(group)
                .nickName("TestMember2의 groupMember")
                .memberLevel(0)
                .created_at( testTime)
                .activeStatus(false)
                .build();

        GroupMember savedGroupMember1 = fakeGroupMemberRepository.save(groupMember1);
        GroupMember savedGroupMember2 = fakeGroupMemberRepository.save(groupMember2);

        PageInfo pageInfo1 = PageInfo.builder()
                .group(group)
                .pageName("Test Page")
                //.posts(postEntities.stream().map(PostEntity::toModel).toList())
                .goodCount(0)
                .badCount(0)
                .viewCount(0)
                .created_at( testTime)
                .updated_at( testTime)
                .build();

        PageInfo savedPageInfo1 = fakePageInfoRepository.save(pageInfo1);

        PageInfo pageInfo2 = PageInfo.builder()
                .group(group)
                .pageName("Test Page2")
                //.posts(postEntities.stream().map(PostEntity::toModel).toList())
                .goodCount(0)
                .badCount(0)
                .viewCount(0)
                .created_at(testTime)
                .updated_at(testTime)
                .build();

        PageInfo savedPageInfo2 = fakePageInfoRepository.save(pageInfo2);

        Post post1 = Post.builder()
                //.parent(parent.toModel())
                .orders(0)
                .groupMember(savedGroupMember1)
                .pageInfo(savedPageInfo1)
                //.historys(historyEntities.stream().map(HistoryEntity::toModel).toList())
                //.comments(commentEntities.stream().map(CommentEntity::toModel).toList())
                .title("1. Test Post")
                .content("해당 포스트는 테스트용 포스트1입니다.")
                .created_at(testTime)
                .build();

        Post savedPost1 = fakePostRepository.save(post1);

        Post post2 = Post.builder()
                //.parent(parent.toModel())
                .orders(1)
                .groupMember(savedGroupMember1)
                .pageInfo(savedPageInfo1)
                //.historys(historyEntities.stream().map(HistoryEntity::toModel).toList())
                //.comments(commentEntities.stream().map(CommentEntity::toModel).toList())
                .title("2. Test Post")
                .content("해당 포스트는 테스트용 포스트2입니다.")
                .created_at(testTime)
                .build();

        Post savedPost2 = fakePostRepository.save(post2);

        Post post21 = Post.builder()
                .parent(savedPost2)
                .orders(2)
                .groupMember(savedGroupMember1)
                .pageInfo(savedPageInfo1)
                //.historys(historyEntities.stream().map(HistoryEntity::toModel).toList())
                //.comments(commentEntities.stream().map(CommentEntity::toModel).toList())
                .title("2-1. Test Post")
                .content("해당 포스트는 테스트용 포스트3입니다.")
                .created_at(testTime)
                .build();

        Post savedPost21 = fakePostRepository.save(post21);

        Post post22 = Post.builder()
                .parent(savedPost2)
                .orders(3)
                .groupMember(savedGroupMember1)
                .pageInfo(savedPageInfo1)
                //.historys(historyEntities.stream().map(HistoryEntity::toModel).toList())
                //.comments(commentEntities.stream().map(CommentEntity::toModel).toList())
                .title("2-2. Test Post")
                .content("해당 포스트는 테스트용 포스트4입니다.")
                .created_at(testTime)
                .build();

        Post savedPost22 = fakePostRepository.save(post22);

        Post post3 = Post.builder()
                //.parent(parent.toModel())
                .orders(4)
                .groupMember(savedGroupMember1)
                .pageInfo(savedPageInfo1)
                //.historys(historyEntities.stream().map(HistoryEntity::toModel).toList())
                //.comments(commentEntities.stream().map(CommentEntity::toModel).toList())
                .title("3. Test Post")
                .content("해당 포스트는 테스트용 포스트5입니다.")
                .created_at(testTime)
                .build();

        Post savedPost3 = fakePostRepository.save(post3);

        fakePageInfoRepository.addPost(savedPageInfo1,savedPost1);
        fakePageInfoRepository.addPost(savedPageInfo1,savedPost2);
        fakePageInfoRepository.addPost(savedPageInfo1,savedPost21);
        fakePageInfoRepository.addPost(savedPageInfo1,savedPost22);
        fakePageInfoRepository.addPost(savedPageInfo1,savedPost3);

    }

    /**
     * [테스트 목록]
     *
     * - getPageIndex
     * 1. getPageIndex는_모든_조건을_만족하는_회원은_페이지인덱스_조회가_가능하다()
     * 2. getPageIndex는_해당_그룹을_이미_탈퇴한_회원은_페이지인덱스_조회가_불가능하다()
     * 3. getPageIndex는_존재하지않는_페이지에_대해_페이지인덱스_조회가_불가능하다()
     * 4. getPageIndex는_해당_회원이_가입한적없는_회원이면_페이지인덱스_조회가_불가능하다()
     * 5. getPageIndex는_Post가_없는_Page요청에_대해서는_빈Post를_반환합니다()
     *
     * - getPageFromId
     * 6. getPageFromId는_모든_조건을_만족하는_회원은_페이지_조회가_가능하다()
     * 7. getPageFromId는_해당_그룹을_이미_탈퇴한_회원은_페이지_조회가_불가능하다()
     * 8. getPageFromId는_해당_회원이_가입한적없는_회원이면_페이지_조회가_불가능하다()
     * 9. getPageFromId는_존재하지않는_페이지에_대해_페이지_조회가_불가능하다()
     *
     *
     *
     *
     *
     * - createPage
     *
     * - deletePage
     * - likePage
     * - hatePage
     * - searchPage
     * - getRecentPage
     * - getPageFromTitle
     * - getPageLink
     */



    @Test
    void getPageIndex는_모든_조건을_만족하는_회원은_페이지인덱스_조회가_가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        Long pageId = 1L;

        //when
        PageInfoResponse.getPageIndexDTO result = pageService.getPageIndex(groupId, memberId, pageId);

        //then
        assertThat(result.getPageName()).isEqualTo("Test Page");
        assertThat(result.getPostList().get(0).getPostId()).isEqualTo(1L);
        assertThat(result.getPostList().get(0).getPostTitle()).isEqualTo("1. Test Post");
        assertThat(result.getPostList().get(0).getIndex()).isEqualTo("1");
        assertThat(result.getPostList().get(1).getPostId()).isEqualTo(2L);
        assertThat(result.getPostList().get(1).getPostTitle()).isEqualTo("2. Test Post");
        assertThat(result.getPostList().get(1).getIndex()).isEqualTo("2");
        assertThat(result.getPostList().get(2).getPostId()).isEqualTo(3L);
        assertThat(result.getPostList().get(2).getPostTitle()).isEqualTo("2-1. Test Post");
        assertThat(result.getPostList().get(2).getIndex()).isEqualTo("2.1");
        assertThat(result.getPostList().get(3).getPostId()).isEqualTo(4L);
        assertThat(result.getPostList().get(3).getPostTitle()).isEqualTo("2-2. Test Post");
        assertThat(result.getPostList().get(3).getIndex()).isEqualTo("2.2");
        assertThat(result.getPostList().get(4).getPostId()).isEqualTo(5L);
        assertThat(result.getPostList().get(4).getPostTitle()).isEqualTo("3. Test Post");
        assertThat(result.getPostList().get(4).getIndex()).isEqualTo("3");


    }

    @Test
    void getPageIndex는_해당_그룹을_이미_탈퇴한_회원은_페이지인덱스_조회가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 2L;
        Long pageId = 1L;

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.getPageIndex(groupId,memberId,pageId);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }
    @Test
    void getPageIndex는_존재하지않는_페이지에_대해_페이지인덱스_조회가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        Long pageId = 100L;

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.getPageIndex(groupId,memberId,pageId);
        }).isInstanceOf(Exception404.class).hasMessage("존재하지 않는 페이지 입니다.");

    }
    @Test
    void getPageIndex는_해당_회원이_가입한적없는_회원이면_페이지인덱스_조회가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 3L;
        Long pageId = 1L;

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.getPageIndex(groupId,memberId,pageId);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }

    @Test
    void getPageIndex는_Post가_없는_Page요청에_대해서는_빈Post를_반환합니다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        Long pageId = 2L;

        //when
        PageInfoResponse.getPageIndexDTO result = pageService.getPageIndex(groupId, memberId, pageId);

        //then
        assertThat(result.getPageName()).isEqualTo("Test Page2");
        assertThat(result.getPostList()).isEmpty();

    }

    @Test
    void getPageFromId는_모든_조건을_만족하는_회원은_페이지_조회가_가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        Long pageId = 1L;

        //when
        PageInfoResponse.getPageFromIdDTO result = pageService.getPageFromId(memberId, groupId, pageId);

        //then
        assertThat(result.getPageId()).isEqualTo(1L);
        assertThat(result.getPageName()).isEqualTo("Test Page");
        assertThat(result.getPostList().get(0).getPostId()).isEqualTo(1L);
        assertThat(result.getPostList().get(0).getPostTitle()).isEqualTo("1. Test Post");
        assertThat(result.getPostList().get(0).getIndex()).isEqualTo("1");
        assertThat(result.getPostList().get(0).getContent()).isEqualTo("해당 포스트는 테스트용 포스트1입니다.");

        assertThat(result.getPostList().get(1).getPostId()).isEqualTo(2L);
        assertThat(result.getPostList().get(1).getPostTitle()).isEqualTo("2. Test Post");
        assertThat(result.getPostList().get(1).getIndex()).isEqualTo("2");
        assertThat(result.getPostList().get(1).getContent()).isEqualTo("해당 포스트는 테스트용 포스트2입니다.");

        assertThat(result.getPostList().get(2).getPostId()).isEqualTo(3L);
        assertThat(result.getPostList().get(2).getPostTitle()).isEqualTo("2-1. Test Post");
        assertThat(result.getPostList().get(2).getIndex()).isEqualTo("2.1");
        assertThat(result.getPostList().get(2).getContent()).isEqualTo("해당 포스트는 테스트용 포스트3입니다.");

        assertThat(result.getPostList().get(3).getPostId()).isEqualTo(4L);
        assertThat(result.getPostList().get(3).getPostTitle()).isEqualTo("2-2. Test Post");
        assertThat(result.getPostList().get(3).getIndex()).isEqualTo("2.2");
        assertThat(result.getPostList().get(3).getContent()).isEqualTo("해당 포스트는 테스트용 포스트4입니다.");

        assertThat(result.getPostList().get(4).getPostId()).isEqualTo(5L);
        assertThat(result.getPostList().get(4).getPostTitle()).isEqualTo("3. Test Post");
        assertThat(result.getPostList().get(4).getIndex()).isEqualTo("3");
        assertThat(result.getPostList().get(4).getContent()).isEqualTo("해당 포스트는 테스트용 포스트5입니다.");

    }

    @Test
    void getPageFromId는_해당_그룹을_이미_탈퇴한_회원은_페이지_조회가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 2L;
        Long pageId = 1L;

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.getPageFromId(memberId,groupId,pageId);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }
    @Test
    void getPageFromId는_해당_회원이_가입한적없는_회원이면_페이지_조회가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 3L;
        Long pageId = 1L;

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.getPageFromId(memberId,groupId,pageId);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");

    }
    @Test
    void getPageFromId는_존재하지않는_페이지에_대해_페이지_조회가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        Long pageId = 100L;

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.getPageFromId(memberId,groupId,pageId);
        }).isInstanceOf(Exception404.class).hasMessage("존재하지 않는 페이지 입니다.");
    }


}
