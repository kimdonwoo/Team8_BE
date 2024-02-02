package com.kakao.techcampus.wekiki.post.service;

import com.kakao.techcampus.wekiki._core.error.exception.Exception400;
import com.kakao.techcampus.wekiki._core.error.exception.Exception404;
import com.kakao.techcampus.wekiki.group.domain.Group;
import com.kakao.techcampus.wekiki.group.domain.GroupMember;
import com.kakao.techcampus.wekiki.history.domain.History;
import com.kakao.techcampus.wekiki.member.domain.Member;
import com.kakao.techcampus.wekiki.member.infrastructure.Authority;
import com.kakao.techcampus.wekiki.mock.*;
import com.kakao.techcampus.wekiki.pageInfo.domain.PageInfo;
import com.kakao.techcampus.wekiki.post.controller.request.PostRequest;
import com.kakao.techcampus.wekiki.post.controller.response.PostResponse;
import com.kakao.techcampus.wekiki.post.domain.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class PostServiceTest {

    private PostServiceImpl postService;
    private FakePageInfoRepository fakePageInfoRepository;
    private FakePostRepository fakePostRepository;
    private FakeHistoryRepository fakeHistoryRepository;
    private FakeReportRepository fakeReportRepository;
    private FakeGroupMemberRepository fakeGroupMemberRepository;

    private final LocalDateTime testTime = LocalDateTime.now();

    @BeforeEach
    void init(){
        fakePageInfoRepository = new FakePageInfoRepository();
        fakePostRepository = new FakePostRepository();
        fakeHistoryRepository = new FakeHistoryRepository();
        fakeReportRepository = new FakeReportRepository();
        fakeGroupMemberRepository = new FakeGroupMemberRepository();

        postService = PostServiceImpl.builder()
                .pageRepository(fakePageInfoRepository)
                .postRepository(fakePostRepository)
                .historyRepository(fakeHistoryRepository)
                .reportRepository(fakeReportRepository)
                .groupMemberJPARepository(fakeGroupMemberRepository)
                .build();

        // 회원 3명 그룹 1개 그룹 멤버 2개
        // 페이지 1개 포스트 5개 ?

        Member member1 = Member.builder()
                .id(1L)
                .name("TestMember1")
                .email("test1@naver.com")
                .password("1111")
                .created_at( testTime)
                //.groupMembers(groupMemberEntities.stream().map(GroupMemberEntity::toModel).toList())
                .authority(Authority.user)
                .build();
        Member member2 = Member.builder()
                .id(2L)
                .name("TestMember2")
                .email("test2@naver.com")
                .password("1111")
                .created_at( testTime)
                //.groupMembers(groupMemberEntities.stream().map(GroupMemberEntity::toModel).toList())
                .authority(Authority.user)
                .build();
        Member member3 = Member.builder()
                .id(3L)
                .name("TestMember3")
                .email("test3@naver.com")
                .password("1111")
                .created_at( testTime)
                //.groupMembers(groupMemberEntities.stream().map(GroupMemberEntity::toModel).toList())
                .authority(Authority.user)
                .build();

        Group group = Group.builder()
                .id(1L)
                .groupName("TestGroup")
                .groupProfileImage("s3/url")
                //.groupMembers(this.groupMemberEntities.stream().map(GroupMemberEntity::toModel).toList())
                .memberCount(0)
                .created_at( testTime)
                .build();

        GroupMember groupMember1 = GroupMember.builder()
                .member(member1)
                .group(group)
                .nickName("TestMember1의 groupMember")
                .memberLevel(0)
                .created_at(testTime)
                .activeStatus(true)
                .build();
        GroupMember groupMember2 = GroupMember.builder()
                .member(member2)
                .group(group)
                .nickName("TestMember2의 groupMember")
                .memberLevel(0)
                .created_at(testTime)
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

        Post post1 = Post.builder()
                .parent(null)
                .orders(0)
                .groupMember(savedGroupMember1)
                .pageInfo(savedPageInfo1)
                .title("1. Test Post")
                .content("해당 포스트는 테스트용 포스트1입니다.")
                .created_at(testTime)
                .build();

        Post savedPost1 = fakePostRepository.save(post1);
        fakeHistoryRepository.save(History.from(savedPost1, savedGroupMember1));

        Post post2 = Post.builder()
                .parent(null)
                .orders(1)
                .groupMember(savedGroupMember1)
                .pageInfo(savedPageInfo1)
                .title("2. Test Post")
                .content("해당 포스트는 테스트용 포스트2입니다.")
                .created_at(testTime)
                .build();

        Post savedPost2 = fakePostRepository.save(post2);
        fakeHistoryRepository.save(History.from(savedPost2, savedGroupMember1));

        Post post21 = Post.builder()
                .parent(savedPost2)
                .orders(2)
                .groupMember(savedGroupMember1)
                .pageInfo(savedPageInfo1)
                .title("2-1. Test Post")
                .content("해당 포스트는 테스트용 포스트3입니다.")
                .created_at(testTime)
                .build();

        Post savedPost21 = fakePostRepository.save(post21);
        fakeHistoryRepository.save(History.from(savedPost21, savedGroupMember1));

        Post post22 = Post.builder()
                .parent(savedPost2)
                .orders(3)
                .groupMember(savedGroupMember1)
                .pageInfo(savedPageInfo1)
                .title("2-2. Test Post")
                .content("해당 포스트는 테스트용 포스트4입니다.")
                .created_at(testTime)
                .build();

        Post savedPost22 = fakePostRepository.save(post22);
        fakeHistoryRepository.save(History.from(savedPost22, savedGroupMember1));

        Post post3 = Post.builder()
                .parent(null)
                .orders(4)
                .groupMember(savedGroupMember1)
                .pageInfo(savedPageInfo1)
                .title("3. Test Post")
                .content("해당 포스트는 테스트용 포스트5입니다.")
                .created_at(testTime)
                .build();

        Post savedPost3 = fakePostRepository.save(post3);
        fakeHistoryRepository.save(History.from(savedPost3, savedGroupMember1));
    }

    /**
     * [테스트 목록]
     *
     * - createPost
     * 1. createPost는_모든_조건을_만족하는_회원은_Post_생성이_가능하다()
     * 2. createPost는_해당_그룹을_이미_탈퇴한_회원은_Post_생성이_불가능하다()
     * 3. createPost는_해당_회원이_가입한적없는_회원이면_Post_생성이_불가능하다()
     * 4. createPost는_존재하지않는_페이지에_대해_Post_생성이_불가능하다()
     * 5. createPost는_존재하지않는_상위_Post에_대한_새로운_하위_Post_생성이_불가능하다()
     *
     * - modifyPost
     * 6. modifyPost는_모든_조건을_만족하는_회원은_Post_수정이_가능하다()
     * 7. modifyPost는_해당_그룹을_이미_탈퇴한_회원은_Post_수정이_불가능하다()
     * 8. modifyPost는_해당_회원이_가입한적없는_회원이면_Post_수정이_불가능하다()
     * 9. modifyPost는_존재하지않는_Post에_대해_Post_수정이_불가능하다()
     * 10. modifyPost는_동일한_내용으로_수정이_불가능합니다()
     *
     * - deletePost
     * 11. deletePost는_모든_조건을_만족하는_회원은_Post_삭제가_가능하다()
     * 12. deletePost는_해당_그룹을_이미_탈퇴한_회원은_Post_삭제가_불가능하다()
     * 13. deletePost는_해당_회원이_가입한적없는_회원이면_Post_삭제가_불가능하다()
     * 14. deletePost는_존재하지않는_Post에_대해_Post_삭제가_불가능하다()
     * 15. deletePost는_하위_글이_존재하는_Post는_삭제가_불가능하다()
     *
     * - getPostHistory
     * 16. getPostHistory는_모든_조건을_만족하는_회원은_Post의_History_조회가_가능하다()
     * 17. getPostHistory는_해당_그룹을_이미_탈퇴한_회원은_Post의_History_조회가_불가능하다()
     * 18. getPostHistory는_해당_회원이_가입한적없는_회원이면_Post의_History_조회가_불가능하다()
     * 19. getPostHistory는_존재하지않는_Post에_대해_History_조회가_불가능하다()
     *
     * - createReport
     * 20. createReport는_모든_조건을_만족하는_회원은_Post에_대해_신고가_가능하다()
     * 21. createReport는_해당_그룹을_이미_탈퇴한_회원은_Post에_대해_신고가_불가능하다()
     * 22. createReport는_해당_회원이_가입한적없는_회원이면_Post에_대해_신고가_불가능하다()
     * 23. createReport는_존재하지않는_Post에_대해_신고가_불가능하다()
     *
     */

    @Test
    void createPost는_모든_조건을_만족하는_회원은_Post_생성이_가능하다(){
        // given
        Long groupId = 1L;
        Long memberId = 1L;
        PostRequest.createPostDTO request = PostRequest.createPostDTO.builder()
                .pageId(1L)
                .parentPostId(0L)
                .order(5)
                .title("새로운 Post의 제목입니다.")
                .content("새로운 Post의 내용입니다")
                .build();

        // when
        PostResponse.createPostDTO result = postService.createPost(memberId, groupId, request);

        // then
        assertThat(result.getPostId()).isEqualTo(6L);
        assertThat(result.getTitle()).isEqualTo("새로운 Post의 제목입니다.");
        assertThat(result.getContent()).isEqualTo("새로운 Post의 내용입니다");


    }

    @Test
    void createPost는_해당_그룹을_이미_탈퇴한_회원은_Post_생성이_불가능하다(){
        // given
        Long groupId = 1L;
        Long memberId = 2L;
        PostRequest.createPostDTO request = PostRequest.createPostDTO.builder()
                .pageId(1L)
                .parentPostId(0L)
                .order(5)
                .title("새로운 Post의 제목입니다.")
                .content("새로운 Post의 내용입니다")
                .build();

        // when
        // then
        assertThatThrownBy(()-> {
            postService.createPost(memberId,groupId,request);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }

    @Test
    void createPost는_해당_회원이_가입한적없는_회원이면_Post_생성이_불가능하다(){
        // given
        Long groupId = 1L;
        Long memberId = 3L;
        PostRequest.createPostDTO request = PostRequest.createPostDTO.builder()
                .pageId(1L)
                .parentPostId(0L)
                .order(5)
                .title("새로운 Post의 제목입니다.")
                .content("새로운 Post의 내용입니다")
                .build();

        // when
        // then
        assertThatThrownBy(()-> {
            postService.createPost(memberId,groupId,request);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }

    @Test
    void createPost는_존재하지않는_페이지에_대해_Post_생성이_불가능하다(){
        // given
        Long groupId = 1L;
        Long memberId = 1L;
        PostRequest.createPostDTO request = PostRequest.createPostDTO.builder()
                .pageId(100L)
                .parentPostId(0L)
                .order(5)
                .title("새로운 Post의 제목입니다.")
                .content("새로운 Post의 내용입니다")
                .build();

        // when
        // then
        assertThatThrownBy(()-> {
            postService.createPost(memberId,groupId,request);
        }).isInstanceOf(Exception404.class).hasMessage("존재하지 않는 페이지 입니다.");
    }

    @Test
    void createPost는_존재하지않는_상위_Post에_대한_새로운_하위_Post_생성이_불가능하다(){
        // given
        Long groupId = 1L;
        Long memberId = 1L;
        PostRequest.createPostDTO request = PostRequest.createPostDTO.builder()
                .pageId(1L)
                .parentPostId(100L)
                .order(5)
                .title("새로운 Post의 제목입니다.")
                .content("새로운 Post의 내용입니다")
                .build();

        // when
        // then
        assertThatThrownBy(()-> {
            postService.createPost(memberId,groupId,request);
        }).isInstanceOf(Exception404.class).hasMessage("존재하지 않는 상위 글입니다.");
    }

    @Test
    void modifyPost는_모든_조건을_만족하는_회원은_Post_수정이_가능하다(){
        // given
        Long groupId = 1L;
        Long memberId = 1L;
        Long postId = 1L;
        String title = "수정된 Post의 제목입니다.";
        String content = "수정된 Post의 내용입니다";

        // when
        PostResponse.modifyPostDTO result = postService.modifyPost(memberId, groupId, postId, title, content);

        // then
        assertThat(result.getPostId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("수정된 Post의 제목입니다.");
        assertThat(result.getContent()).isEqualTo("수정된 Post의 내용입니다");


    }

    @Test
    void modifyPost는_해당_그룹을_이미_탈퇴한_회원은_Post_수정이_불가능하다(){
        // given
        Long groupId = 1L;
        Long memberId = 2L;
        Long postId = 1L;
        String title = "수정된 Post의 제목입니다.";
        String content = "수정된 Post의 내용입니다";


        // when
        // then
        assertThatThrownBy(()-> {
            postService.modifyPost(memberId,groupId,postId,title,content);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }

    @Test
    void modifyPost는_해당_회원이_가입한적없는_회원이면_Post_수정이_불가능하다(){
        // given
        Long groupId = 1L;
        Long memberId = 3L;
        Long postId = 1L;
        String title = "수정된 Post의 제목입니다.";
        String content = "수정된 Post의 내용입니다";

        // when
        // then
        assertThatThrownBy(()-> {
            postService.modifyPost(memberId,groupId,postId,title,content);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }

    @Test
    void modifyPost는_존재하지않는_Post에_대해_Post_수정이_불가능하다(){
        // given
        Long groupId = 1L;
        Long memberId = 1L;
        Long postId = 100L;
        String title = "수정된 Post의 제목입니다.";
        String content = "수정된 Post의 내용입니다";

        // when
        // then
        assertThatThrownBy(()-> {
            postService.modifyPost(memberId,groupId,postId,title,content);
        }).isInstanceOf(Exception404.class).hasMessage("존재하지 않는 글 입니다.");
    }

    @Test
    void modifyPost는_동일한_내용으로_수정이_불가능합니다(){
        // given
        Long groupId = 1L;
        Long memberId = 1L;
        Long postId = 1L;
        String title = "1. Test Post";
        String content = "해당 포스트는 테스트용 포스트1입니다.";

        // when
        // then
        assertThatThrownBy(()-> {
            postService.modifyPost(memberId,groupId,postId,title,content);
        }).isInstanceOf(Exception400.class).hasMessage("기존 글과 동일한 글입니다.");
    }

    @Test
    void deletePost는_모든_조건을_만족하는_회원은_Post_삭제가_가능하다(){
        // given
        Long groupId = 1L;
        Long memberId = 1L;
        Long postId = 1L;

        // when
        PostResponse.deletePostDTO result = postService.deletePost(memberId, groupId, postId);

        // then
        assertThat(result.getPostId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("1. Test Post");

    }

    @Test
    void deletePost는_해당_그룹을_이미_탈퇴한_회원은_Post_삭제가_불가능하다(){
        // given
        Long groupId = 1L;
        Long memberId = 2L;
        Long postId = 5L;

        // when
        // then
        assertThatThrownBy(()-> {
            postService.deletePost(memberId,groupId,postId);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }

    @Test
    void deletePost는_해당_회원이_가입한적없는_회원이면_Post_삭제가_불가능하다(){
        // given
        Long groupId = 1L;
        Long memberId = 3L;
        Long postId = 5L;

        // when
        // then
        assertThatThrownBy(()-> {
            postService.deletePost(memberId,groupId,postId);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }

    @Test
    void deletePost는_존재하지않는_Post에_대해_Post_삭제가_불가능하다(){
        // given
        Long groupId = 1L;
        Long memberId = 1L;
        Long postId = 100L;

        // when
        // then
        assertThatThrownBy(()-> {
            postService.deletePost(memberId,groupId,postId);
        }).isInstanceOf(Exception404.class).hasMessage("존재하지 않는 글 입니다.");
    }

    @Test
    void deletePost는_하위_글이_존재하는_Post는_삭제가_불가능하다(){
        // given
        Long groupId = 1L;
        Long memberId = 1L;
        Long postId = 2L;

        // when
        // then
        assertThatThrownBy(()-> {
            postService.deletePost(memberId,groupId,postId);
        }).isInstanceOf(Exception400.class).hasMessage("하위 글이 존재하는 글은 삭제가 불가능합니다.");
    }

    @Test
    void getPostHistory는_모든_조건을_만족하는_회원은_Post의_History_조회가_가능하다(){
        // given
        Long groupId = 1L;
        Long memberId = 1L;
        Long postId = 1L;
        int pageNo = 0;

        // when
        PostResponse.getPostHistoryDTO result = postService.getPostHistory(memberId, groupId, postId, pageNo);

        // then
        assertThat(result.getPostId()).isEqualTo(1L);
        assertThat(result.getCurrentTitle()).isEqualTo("1. Test Post");
        assertThat(result.getHistoryList().get(0).getHistoryId()).isEqualTo(1L);
        assertThat(result.getHistoryList().get(0).getTitle()).isEqualTo("1. Test Post");
        assertThat(result.getHistoryList().get(0).getContent()).isEqualTo("해당 포스트는 테스트용 포스트1입니다.");
        assertThat(result.getHistoryList().get(0).getNickName()).isEqualTo("TestMember1의 groupMember");
        assertThat(result.getHistoryList().get(0).getMemberId()).isEqualTo(1L);
        assertThat(result.getHistoryList().get(0).getCreatedAt()).isEqualTo(testTime);

    }

    @Test
    void getPostHistory는_해당_그룹을_이미_탈퇴한_회원은_Post의_History_조회가_불가능하다(){
        // given
        Long groupId = 1L;
        Long memberId = 2L;
        Long postId = 5L;
        int pageNo = 0;

        // when
        // then
        assertThatThrownBy(()-> {
            postService.getPostHistory(memberId, groupId, postId, pageNo);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }

    @Test
    void getPostHistory는_해당_회원이_가입한적없는_회원이면_Post의_History_조회가_불가능하다(){
        // given
        Long groupId = 1L;
        Long memberId = 3L;
        Long postId = 5L;
        int pageNo = 0;

        // when
        // then
        assertThatThrownBy(()-> {
            postService.getPostHistory(memberId, groupId, postId, pageNo);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }

    @Test
    void getPostHistory는_존재하지않는_Post에_대해_History_조회가_불가능하다(){
        // given
        Long groupId = 1L;
        Long memberId = 1L;
        Long postId = 100L;
        int pageNo = 0;

        // when
        // then
        assertThatThrownBy(()-> {
            postService.getPostHistory(memberId, groupId, postId, pageNo);
        }).isInstanceOf(Exception404.class).hasMessage("존재하지 않는 글 입니다.");
    }

    @Test
    void createReport는_모든_조건을_만족하는_회원은_Post에_대해_신고가_가능하다(){
        // given
        Long memberId = 1L;
        Long groupId = 1L;
        Long postId = 1L;
        String content = "해당 Post를 신고합니다.";

        // when
        PostResponse.createReportDTO result = postService.createReport(memberId, groupId, postId, content);

        // then
        assertThat(result.getReportId()).isEqualTo(1L);

    }

    @Test
    void createReport는_해당_그룹을_이미_탈퇴한_회원은_Post에_대해_신고가_불가능하다(){
        // given
        Long memberId = 2L;
        Long groupId = 1L;
        Long postId = 1L;
        String content = "해당 Post를 신고합니다.";

        // when
        // then
        assertThatThrownBy(()-> {
            postService.createReport(memberId, groupId, postId, content);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }

    @Test
    void createReport는_해당_회원이_가입한적없는_회원이면_Post에_대해_신고가_불가능하다(){
        // given
        Long memberId = 3L;
        Long groupId = 1L;
        Long postId = 1L;
        String content = "해당 Post를 신고합니다.";

        // when
        // then
        assertThatThrownBy(()-> {
            postService.createReport(memberId, groupId, postId, content);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }

    @Test
    void createReport는_존재하지않는_Post에_대해_신고가_불가능하다(){
        // given
        Long memberId = 1L;
        Long groupId = 1L;
        Long postId = 100L;
        String content = "해당 Post를 신고합니다.";

        // when
        // then
        assertThatThrownBy(()-> {
            postService.createReport(memberId, groupId, postId, content);
        }).isInstanceOf(Exception404.class).hasMessage("존재하지 않는 글 입니다.");
    }
}
