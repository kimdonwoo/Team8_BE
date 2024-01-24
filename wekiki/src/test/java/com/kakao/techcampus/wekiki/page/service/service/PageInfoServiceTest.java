package com.kakao.techcampus.wekiki.page.service.service;


import com.kakao.techcampus.wekiki._core.error.exception.Exception400;
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
    private FakeRedisUtils fakeRedisUtils;
    private PageIndexGeneratorImpl pageIndexGeneratorImpl;
    private FakePageInfoRepository fakePageInfoRepository;
    private FakePostRepository fakePostRepository;
    private FakeMemberRepository fakeMemberRepository;
    private FakeGroupMemberRepository fakeGroupMemberRepository;
    private FakeGroupRepository fakeGroupRepository;



    @BeforeEach
    void init(){
        pageIndexGeneratorImpl = new PageIndexGeneratorImpl();
        fakePageInfoRepository = new FakePageInfoRepository();
        fakePostRepository = new FakePostRepository();
        fakeMemberRepository = new FakeMemberRepository();
        fakeGroupMemberRepository = new FakeGroupMemberRepository();
        fakeGroupRepository = new FakeGroupRepository();
        fakeRedisUtils = new FakeRedisUtils();

        pageService = PageService.builder()
                .pageRepository(fakePageInfoRepository)
                .pageIndexGenerator(pageIndexGeneratorImpl)
                .groupRepository(fakeGroupRepository)
                .memberRepository(fakeMemberRepository)
                .groupMemberRepository(fakeGroupMemberRepository)
                .postRepository(fakePostRepository)
                .redisUtils(fakeRedisUtils)
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
     * 5. getPageIndex는_Post가_없는_Page요청에_대해서는_빈_Post를_반환합니다()
     *
     * - getPageFromId
     * 6. getPageFromId는_모든_조건을_만족하는_회원은_페이지_조회가_가능하다()
     * 7. getPageFromId는_해당_그룹을_이미_탈퇴한_회원은_페이지_조회가_불가능하다()
     * 8. getPageFromId는_해당_회원이_가입한적없는_회원이면_페이지_조회가_불가능하다()
     * 9. getPageFromId는_존재하지않는_페이지에_대해_페이지_조회가_불가능하다()
     *
     * - createPage
     * 10. createPage는_모든_조건을_만족하는_회원은_페이지_생성이_가능하다()
     * 11. createPage는_해당_그룹을_이미_탈퇴한_회원은_페이지_생성이_불가능하다()
     * 12. createPage는_해당_회원이_가입한적없는_회원이면_페이지_생성이_불가능하다()
     * 13. createPage는_이미_존재하는_이름의_페이지랑_동일한_페이지-생성이_불가능하다()
     *
     * - deletePage
     * 14. deletePage는_모든_조건을_만족하는_회원은_페이지_삭제가_가능하다()
     * 15. deletePage는_해당_그룹을_이미_탈퇴한_회원은_페이지_삭제가_불가능하다()
     * 16. deletePage는_해당_회원이_가입한적없는_회원이면_페이지_삭제가_불가능하다()
     * 17. deletePage는_존재하지않는_페이지에_대해_페이지_삭제가_불가능하다()
     * 18. deletePage는 post가 존재하는 페이지는 삭제가 불가능하다()
     *
     * - likePage
     * 19. likePage는_모든_조건을_만족하는_회원은_페이지_좋아요가_가능하다()
     * 20. likePage는_해당_그룹을_이미_탈퇴한_회원은_페이지_좋아요가_불가능하다()
     * 21. likePage는_해당_회원이_가입한적없는_회원이면_페이지_좋아요가_불가능하다()
     * 22. likePage는_존재하지않는_페이지에_대해_페이지_좋아요가_불가능하다()
     *
     * - hatePage
     * 23. hatePage는_모든_조건을_만족하는_회원은_페이지_싫어요가_가능하다()
     * 24. hatePage는_해당_그룹을_이미_탈퇴한_회원은_페이지_싫어요_불가능하다()
     * 25. hatePage는_해당_회원이_가입한적없는_회원이면_페이지_싫어요가_불가능하다()
     * 26. hatePage는_존재하지않는_페이지에_대해_페이지_싫어요가_불가능하다()
     *
     * - searchPage
     * 27. searchPage는_모든_조건을_만족하는_회원은_페이지_검색이_가능하다()
     * 28. searchPage는_해당_그룹을_이미_탈퇴한_회원은_페이지_검색이_불가능하다()
     * 29. searchPage는_해당_회원이_가입한적없는_회원이면_페이지_검색이_불가능하다()
     * 30. searchPage는_Post가_존재하지_않는_Page는_content에_빈문자열을_반환한다()
     * 31. searchPage는_해당_keyword를_포함하는_페이지가_존재하지_않을_경우_빈페이지를_반환한다()
     *
     * - getRecentPage
     * 32. getRecentPage는_모든_조건을_만족하는_회원은_최근변경페이지_조회가_가능하다()
     * 33. getRecentPage는_해당_그룹을_이미_탈퇴한_회원은_최근변경페이지_조회가_불가능하다()
     * 34. getRecentPage는_해당_회원이_가입한적없는_회원이면_최근변경페이지_조회가_불가능하다()
     * 35. getRecentPage는_Page가_하나도_없을_때_빈_recentPageDTO배열을_반환한다()
     * 36. getRecentPage는_Page가_10개_이상있을때_10개의_최근변경페이지를_반환한다()
     *
     * - getPageFromTitle
     * 37. getPageFromTitle는_모든_조건을_만족하는_회원은_제목으로_페이지조회가_가능하다()
     * 38. getPageFromTitle는_해당_그룹을_이미_탈퇴한_회원은_제목으로_페이지조회가_불가능하다()
     * 39. getPageFromTitle는_해당_회원이_가입한적없는_회원이면_제목으로_페이지조회가_불가능하다()
     * 40. getPageFromTitle는_존재하지않는_페이지에_대해_제목으로_페이지조회가_불가능하다()
     * 41. getPageFromTitle는_Post가_없는_페이지에_대해_제목으로_페이지조회는_빈_Post를_반환합니다()
     *
     * - getPageLink
     * 42. getPageLink는_존재하는_페이지에_대해_페이지의_링크조회가_가능하다()
     * 43. getPageLink는_존재하지않는_페이지에_대해_페이지의_링크_조회가_불가능하다()
     * 44. getPageLink는_존재하지않는_그룹에_대해_페이지의_링크_조회가_불가능하다()
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

    @Test
    void createPage는_모든_조건을_만족하는_회원은_페이지_생성이_가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        String title = "새로운 페이지 생성";

        //when
        PageInfoResponse.createPageDTO result = pageService.createPage(title, groupId, memberId);

        //then
        assertThat(result.getPageId()).isEqualTo(3L);
        assertThat(result.getPageName()).isEqualTo("새로운 페이지 생성");
        assertThat(fakeRedisUtils.getHashValue(pageService.getGROUP_PREFIX()+groupId,title))
                .isEqualTo("3");

    }

    @Test
    void createPage_해당_그룹을_이미_탈퇴한_회원은_페이지_생성이_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 2L;
        String title = "새로운 페이지 생성";

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.createPage(title,groupId,memberId);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }
    @Test
    void createPage_해당_회원이_가입한적없는_회원이면_페이지_생성이_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 3L;
        String title = "새로운 페이지 생성";

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.createPage(title,groupId,memberId);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");

    }
    @Test
    void createPage_이미_존재하는_이름의_페이지랑_동일한_페이지_생성이_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        String title = "Test Page";

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.createPage(title,groupId,memberId);
        }).isInstanceOf(Exception400.class).hasMessage("이미 존재하는 페이지입니다.");
    }

    @Test
    void deletePage는_모든_조건을_만족하는_회원은_페이지_삭제가_가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        Long pageId = 2L;

        //when
        PageInfoResponse.deletePageDTO result = pageService.deletePage(memberId, groupId, pageId);

        //then
        assertThat(result.getPageId()).isEqualTo(2L);
        assertThat(result.getTitle()).isEqualTo("Test Page2");

    }

    @Test
    void deletePage_해당_그룹을_이미_탈퇴한_회원은_페이지_삭제가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 2L;
        Long pageId = 1L;

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.deletePage(memberId, groupId, pageId);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }
    @Test
    void deletePage_해당_회원이_가입한적없는_회원이면_페이지_삭제가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 3L;
        Long pageId = 1L;

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.deletePage(memberId, groupId, pageId);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");

    }
    @Test
    void deletePage는_존재하지않는_페이지에_대해_페이지_삭제가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        Long pageId = 100L;

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.deletePage(memberId, groupId, pageId);
        }).isInstanceOf(Exception404.class).hasMessage("존재하지 않는 페이지 입니다.");
    }

    @Test
    void deletePage는_post가_존재하는_페이지는_삭제가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        Long pageId = 1L;

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.deletePage(memberId, groupId, pageId);
        }).isInstanceOf(Exception400.class).hasMessage("글이 적혀있는 페이지는 삭제가 불가능합니다.");
    }

    @Test
    void likePage는_모든_조건을_만족하는_회원은_페이지_좋아요가_가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        Long pageId = 1L;

        //when
        PageInfoResponse.likePageDTO result = pageService.likePage(pageId, groupId, memberId);

        //then
        assertThat(result.getPageId()).isEqualTo(1L);
        assertThat(result.getPageName()).isEqualTo("Test Page");
        assertThat(result.getGoodCount()).isEqualTo(1);
    }

    @Test
    void likePage는_해당_그룹을_이미_탈퇴한_회원은_페이지_좋아요가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 2L;
        Long pageId = 1L;

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.likePage(pageId, groupId, memberId);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }
    @Test
    void likePage는_해당_회원이_가입한적없는_회원이면_페이지_좋아요가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 3L;
        Long pageId = 1L;

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.likePage(pageId, groupId, memberId);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");

    }
    @Test
    void likePage는_존재하지않는_페이지에_대해_페이지_좋아요가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        Long pageId = 100L;

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.likePage(pageId, groupId, memberId);
        }).isInstanceOf(Exception404.class).hasMessage("존재하지 않는 페이지 입니다.");
    }

    @Test
    void hatePage는_모든_조건을_만족하는_회원은_페이지_싫어요가_가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        Long pageId = 1L;

        //when
        PageInfoResponse.hatePageDTO result = pageService.hatePage(pageId, groupId, memberId);

        //then
        assertThat(result.getPageId()).isEqualTo(1L);
        assertThat(result.getPageName()).isEqualTo("Test Page");
        assertThat(result.getBadCount()).isEqualTo(1);
    }

    @Test
    void hatePage는_해당_그룹을_이미_탈퇴한_회원은_페이지_싫어요가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 2L;
        Long pageId = 1L;

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.hatePage(pageId, groupId, memberId);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }
    @Test
    void hatePage는_해당_회원이_가입한적없는_회원이면_페이지_싫어요가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 3L;
        Long pageId = 1L;

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.hatePage(pageId, groupId, memberId);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");

    }
    @Test
    void hatePage는_존재하지않는_페이지에_대해_페이지_싫어요가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        Long pageId = 100L;

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.hatePage(pageId, groupId, memberId);
        }).isInstanceOf(Exception404.class).hasMessage("존재하지 않는 페이지 입니다.");
    }

    @Test
    void searchPage는_모든_조건을_만족하는_회원은_페이지_검색이_가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        int pageNo = 0;
        String keyword = "Test";

        //when
        PageInfoResponse.searchPageDTO result = pageService.searchPage(groupId, memberId, pageNo, keyword);

        //then
        assertThat(result.getPages().get(0).getPageId()).isEqualTo(1L);
        assertThat(result.getPages().get(0).getPageName()).isEqualTo("Test Page");
        assertThat(result.getPages().get(0).getContent()).isEqualTo("해당 포스트는 테스트용 포스트1입니다.");
    }

    @Test
    void searchPage는_해당_그룹을_이미_탈퇴한_회원은_페이지_검색이_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 2L;
        int pageNo = 0;
        String keyword = "Test";

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.searchPage(groupId, memberId,pageNo,keyword);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }
    @Test
    void searchPage는_해당_회원이_가입한적없는_회원이면_페이지_검색이_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 2L;
        int pageNo = 0;
        String keyword = "Test";

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.searchPage(groupId, memberId,pageNo,keyword);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");

    }
    @Test
    void searchPage는_Post가_존재하지_않는_Page는_content에_빈문자열을_반환한다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        int pageNo = 0;
        String keyword = "Test";

        //when
        PageInfoResponse.searchPageDTO result = pageService.searchPage(groupId, memberId, pageNo, keyword);

        //then
        assertThat(result.getPages().get(1).getPageId()).isEqualTo(2L);
        assertThat(result.getPages().get(1).getPageName()).isEqualTo("Test Page2");
        assertThat(result.getPages().get(1).getContent()).isEqualTo("");
    }

    @Test
    void searchPage는_해당keyword를_포함하는_페이지가_존재하지_않을_경우_빈페이지를_반환한다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        int pageNo = 0;
        String keyword = "blank";

        //when
        PageInfoResponse.searchPageDTO result = pageService.searchPage(groupId, memberId, pageNo, keyword);

        //then
        assertThat(result.getPages()).isEmpty();
    }

    @Test
    void getRecentPage는_모든_조건을_만족하는_회원은_최근변경페이지_조회가_가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;

        //when
        PageInfoResponse.getRecentPageDTO result = pageService.getRecentPage(memberId, groupId);
        ;

        //then
        assertThat(result.getRecentPage().get(0).getPageId()).isEqualTo(1L);
        assertThat(result.getRecentPage().get(0).getPageName()).isEqualTo("Test Page");
        assertThat(result.getRecentPage().get(0).getUpdated_at()).isEqualTo(testTime);
        assertThat(result.getRecentPage().get(1).getPageId()).isEqualTo(2L);
        assertThat(result.getRecentPage().get(1).getPageName()).isEqualTo("Test Page2");
        assertThat(result.getRecentPage().get(1).getUpdated_at()).isEqualTo(testTime);
    }

    @Test
    void getRecentPage는_해당_그룹을_이미_탈퇴한_회원은_최근변경페이지_조회가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 2L;

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.getRecentPage(memberId,groupId);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }
    @Test
    void getRecentPage는_해당_회원이_가입한적없는_회원이면_최근변경페이지_조회가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 3L;

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.getRecentPage(memberId,groupId);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }
    @Test
    void getRecentPage는_Page가_하나도_없을_때_빈_recentPageDTO배열을_반환한다(){
        //given
        Member newMember = Member.builder()
                .name("TestMember4")
                .email("test4@naver.com")
                .password("1111")
                .created_at( testTime)
                .authority(Authority.user)
                .build();
        Member savedNewMember = fakeMemberRepository.save(newMember);
        Group newGroup = Group.builder()
                .id(2L)
                .groupName("TestGroup2")
                .groupProfileImage("s3/url")
                .memberCount(0)
                .created_at( testTime)
                .build();
        GroupMember newGroupMember = GroupMember.builder()
                .member(savedNewMember)
                .group(newGroup)
                .nickName("TestMember4의 groupMember")
                .memberLevel(0)
                .created_at(testTime)
                .activeStatus(true)
                .build();
        fakeGroupMemberRepository.save(newGroupMember);

        //when
        PageInfoResponse.getRecentPageDTO result = pageService.getRecentPage(savedNewMember.getId(), newGroup.getId());

        //then
        assertThat(result.getRecentPage()).isEmpty();
    }

    @Test
    void getRecentPage는_Page가_10개_이상있을때_10개의_최근변경페이지를_반환한다(){
        //given
        Member newMember = Member.builder()
                .name("TestMember4")
                .email("test4@naver.com")
                .password("1111")
                .created_at( testTime)
                .authority(Authority.user)
                .build();
        Member savedNewMember = fakeMemberRepository.save(newMember);
        Group newGroup = Group.builder()
                .id(2L)
                .groupName("TestGroup2")
                .groupProfileImage("s3/url")
                .memberCount(0)
                .created_at( testTime)
                .build();
        GroupMember newGroupMember = GroupMember.builder()
                .member(savedNewMember)
                .group(newGroup)
                .nickName("TestMember4의 groupMember")
                .memberLevel(0)
                .created_at(testTime)
                .activeStatus(true)
                .build();
        fakeGroupMemberRepository.save(newGroupMember);

        for(int i = 0 ; i < 15 ; i++){
            PageInfo pageInfo = PageInfo.builder()
                    .group(newGroup)
                    .pageName("Test Page"+i)
                    .goodCount(0)
                    .badCount(0)
                    .viewCount(0)
                    .created_at(testTime)
                    .updated_at(testTime.plusMinutes(15-i))
                    .build();
            fakePageInfoRepository.save(pageInfo);
        }

        //when
        PageInfoResponse.getRecentPageDTO result = pageService.getRecentPage(savedNewMember.getId(), newGroup.getId());

        //then
        assertThat(result.getRecentPage().size()).isEqualTo(10);
        assertThat(result.getRecentPage().get(0).getPageId()).isEqualTo(17);
        assertThat(result.getRecentPage().get(1).getPageId()).isEqualTo(16);
        assertThat(result.getRecentPage().get(2).getPageId()).isEqualTo(15);
        assertThat(result.getRecentPage().get(3).getPageId()).isEqualTo(14);
        assertThat(result.getRecentPage().get(4).getPageId()).isEqualTo(13);
        assertThat(result.getRecentPage().get(5).getPageId()).isEqualTo(12);
        assertThat(result.getRecentPage().get(6).getPageId()).isEqualTo(11);
        assertThat(result.getRecentPage().get(7).getPageId()).isEqualTo(10);
        assertThat(result.getRecentPage().get(8).getPageId()).isEqualTo(9);
        assertThat(result.getRecentPage().get(9).getPageId()).isEqualTo(8);
    }

    @Test
    void getPageFromTitle는_모든_조건을_만족하는_회원은_제목으로_페이지조회가_가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        String title = "Test Page";

        //when
        PageInfoResponse.getPageFromIdDTO result = pageService.getPageFromTitle(memberId, groupId, title);

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
    void getPageFromTitle는_해당_그룹을_이미_탈퇴한_회원은_제목으로_페이지조회가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 2L;
        String title = "Test Page";

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.getPageFromTitle(memberId,groupId,title);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }
    @Test
    void getPageFromTitle는_해당_회원이_가입한적없는_회원이면_제목으로_페이지조회가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 3L;
        String title = "Test Page";

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.getPageFromTitle(memberId,groupId,title);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");

    }
    @Test
    void getPageFromTitle는_존재하지않는_페이지에_대해_제목으로_페이지조회가_불가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        String title = "Null Page";

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.getPageFromTitle(memberId,groupId,title);
        }).isInstanceOf(Exception404.class).hasMessage("존재하지 않는 페이지 입니다.");
    }

    @Test
    void getPageFromTitle는_Post가_없는_페이지에_대해_제목으로_페이지조회는_빈_Post를_반환합니다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        String title = "Test Page2";

        //when
        PageInfoResponse.getPageFromIdDTO result = pageService.getPageFromTitle(memberId, groupId, title);

        //then
        assertThat(result.getPageName()).isEqualTo("Test Page2");
        assertThat(result.getPostList()).isEmpty();

    }

    @Test
    void getPageLink는_존재하는_페이지에_대해_페이지의_링크조회가_가능하다(){
        //given
        Long groupId = 1L;
        Long memberId = 1L;
        String title = "Link Test Page";
        pageService.createPage(title, groupId, memberId);

        //when
        PageInfoResponse.getPageLinkDTO result = pageService.getPageLink(groupId, title);

        //then
        assertThat(result.getPageId()).isEqualTo(3L);
    }

    @Test
    void getPageLink는_존재하지않는_페이지에_대해_페이지의_링크_조회가_불가능하다(){
        //given
        Long groupId = 1L;
        String title = "Null Page";

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.getPageLink(groupId, title);
        }).isInstanceOf(Exception404.class).hasMessage("존재하지 않는 페이지 입니다.");
    }

    @Test
    void getPageLink는_존재하지않는_그룹에_대해_페이지의_링크_조회가_불가능하다(){
        //given
        Long groupId = 100L;
        String title = "Null Page";

        //when
        //then
        assertThatThrownBy(()-> {
            pageService.getPageLink(groupId, title);
        }).isInstanceOf(Exception404.class).hasMessage("존재하지 않는 페이지 입니다.");
    }

}
