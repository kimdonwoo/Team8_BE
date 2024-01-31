package com.kakao.techcampus.wekiki.comment.service;

import com.kakao.techcampus.wekiki._core.error.exception.Exception400;
import com.kakao.techcampus.wekiki._core.error.exception.Exception404;
import com.kakao.techcampus.wekiki.comment.controller.response.CommentResponse;
import com.kakao.techcampus.wekiki.comment.domain.Comment;

import com.kakao.techcampus.wekiki.group.domain.Group;
import com.kakao.techcampus.wekiki.group.domain.GroupMember;
import com.kakao.techcampus.wekiki.member.domain.Member;
import com.kakao.techcampus.wekiki.member.infrastructure.Authority;
import com.kakao.techcampus.wekiki.mock.FakeCommentRepository;
import com.kakao.techcampus.wekiki.mock.FakeGroupMemberRepository;
import com.kakao.techcampus.wekiki.mock.FakePageInfoRepository;
import com.kakao.techcampus.wekiki.mock.FakePostRepository;
import com.kakao.techcampus.wekiki.pageInfo.domain.PageInfo;
import com.kakao.techcampus.wekiki.post.domain.Post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class CommentServiceTest {

    private CommentService commentService;
    private final LocalDateTime testTime = LocalDateTime.now();
    private FakeCommentRepository fakeCommentRepository;
    private FakeGroupMemberRepository fakeGroupMemberRepository;
    private FakePostRepository fakePostRepository;
    private FakePageInfoRepository fakePageInfoRepository;

    @BeforeEach
    void init(){
        fakeCommentRepository = new FakeCommentRepository();
        fakeGroupMemberRepository = new FakeGroupMemberRepository();
        fakePostRepository = new FakePostRepository();
        fakePageInfoRepository = new FakePageInfoRepository();

        this.commentService = CommentService.builder()
                .commentRepository(fakeCommentRepository)
                .postRepository(fakePostRepository)
                .groupMemberRepository(fakeGroupMemberRepository)
                .build();

        // 처음에 회원 2명, 그룹 1개, 그룹멤버 2개, 페이지 1개, 포스트 1개, 댓글 2개

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
                .created_at(testTime)
                //.groupMembers(groupMemberEntities.stream().map(GroupMemberEntity::toModel).toList())
                .authority(Authority.user)
                .build();
        Member member4 = Member.builder()
                .id(4L)
                .name("TestMember4")
                .email("test4@naver.com")
                .password("1111")
                .created_at(testTime)
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
                .created_at( testTime)
                .activeStatus(true)
                .build();
        GroupMember groupMember2 = GroupMember.builder()
                .member(member2)
                .group(group)
                .nickName("TestMember2의 groupMember")
                .memberLevel(0)
                .created_at( testTime)
                .activeStatus(false)
                .build();
        GroupMember groupMember3 = GroupMember.builder()
                .member(member4)
                .group(group)
                .nickName("TestMember4의 groupMember")
                .memberLevel(0)
                .created_at(testTime)
                .activeStatus(true)
                .build();

        GroupMember savedGroupMember1 = fakeGroupMemberRepository.save(groupMember1);
        fakeGroupMemberRepository.save(groupMember2);
        fakeGroupMemberRepository.save(groupMember3);

        PageInfo pageInfo = PageInfo.builder()
                .group(group)
                .pageName("Test Page")
                //.posts(postEntities.stream().map(PostEntity::toModel).toList())
                .goodCount(0)
                .badCount(0)
                .viewCount(0)
                .created_at( testTime)
                .updated_at( testTime)
                .build();

        PageInfo savedPageInfo1 = fakePageInfoRepository.save(pageInfo);

        Post post = Post.builder()
                //.parent(parent.toModel())
                .orders(0)
                .groupMember(savedGroupMember1)
                .pageInfo(savedPageInfo1)
                //.historys(historyEntities.stream().map(HistoryEntity::toModel).toList())
                //.comments(commentEntities.stream().map(CommentEntity::toModel).toList())
                .title("Test Post")
                .content("해당 포스트는 테스트용 포스트입니다.")
                .created_at( testTime)
                .build();

        Post savedPost = fakePostRepository.save(post);

        Comment comment1 = Comment.builder()
                .groupMember(savedGroupMember1)
                .post(savedPost)
                .content("TestMember1의 Test Comment 입니다.")
                .created_at( testTime)
                .build();
        Comment comment2 = Comment.builder()
                .groupMember(savedGroupMember1)
                .post(savedPost)
                .content("TestMember1의 Test Comment2 입니다.")
                .created_at( testTime)
                .build();

        fakeCommentRepository.save(comment1);
        fakeCommentRepository.save(comment2);
    }

    /**
     * [테스트 목록]
     *
     * - getComment
     *  1. getComment는_모든_조건을_만족하는_회원은_모든_댓글을_다_가져올_수_있다()
     *  2. getComment는_해당_그룹을_이미_탈퇴한_회원은_댓글조회가_불가능하다()
     *  3. getComment는_해당_회원이_가입한적없는_회원이면_댓글조회가_불가능하다()
     *  4. getComment는_존재하지_않는_게시글의_댓글은_조회가_불가능하다()
     *
     *  - createComment
     *  5. createComment는_모든_조건을_만족하는_회원은_댓글생성이_가능하다()
     *  6. createComment는_해당_그룹을_이미_탈퇴한_회원은_댓글생성이_불가능하다()
     *  7. createComment는_해당_회원이_가입한적없는_회원이면_댓글생성이_불가능하다()
     *  8. createComment는_존재하지_않는_게시글에는_댓글생성이_불가능하다()
     *
     *  - updateComment
     *  9. updateComment는_모든_조건을_만족하는_회원은_댓글수정이_가능하다()
     *  10. updateComment는_해당_그룹을_이미_탈퇴한_회원은_댓글수정이_불가능하다()
     *  11. updateComment는_해당_회원이_가입한적없는_회원이면_댓글수정이_불가능하다()
     *  12. updateComment는_존재하지_않는_댓글에_수정이_불가능하다()
     *  13. updateComment는_댓글작성자가_아니면_수정이_불가능하다()
     *  14. updateComment는_기존_댓글과_내용이_동일하도록_수정이_불가능하다()
     *
     * - deleteComment
     * 15. deleteComment는_모든_조건을_만족하는_회원은_댓글삭제가_가능하다()
     * 16. deleteComment는_해당_그룹을_이미_탈퇴한_회원은_댓글삭제가_불가능하다()
     * 17. deleteComment는_해당_회원이_가입한적없는_회원이면_댓글삭제가_불가능하다()
     * 18. deleteComment는_존재하지_않는_댓글삭제가_불가능하다()
     * 19. deleteComment는_댓글작성자가_아니면_삭제가_불가능하다()
     *
     */

    @Test
    void getComment는_모든_조건을_만족하는_회원은_모든_댓글을_다_가져올_수_있다(){
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Long postId = 1L;
        int pageNo = 0;

        //when
        CommentResponse.getCommentDTO getCommentDTO = commentService.getComment(memberId, groupId, postId, pageNo);

        //then
        assertThat(getCommentDTO.getPostId()).isEqualTo(1L);
        assertThat(getCommentDTO.getComments().get(0).getCommentId()).isEqualTo(1L);
        assertThat(getCommentDTO.getComments().get(0).getContent()).isEqualTo("TestMember1의 Test Comment 입니다.");
        assertThat(getCommentDTO.getComments().get(0).getNickName()).isEqualTo("TestMember1의 groupMember");
        assertThat(getCommentDTO.getComments().get(0).getCreatedAt()).isEqualTo(testTime);
        assertThat(getCommentDTO.getComments().get(1).getCommentId()).isEqualTo(2L);
        assertThat(getCommentDTO.getComments().get(1).getContent()).isEqualTo("TestMember1의 Test Comment2 입니다.");
        assertThat(getCommentDTO.getComments().get(1).getNickName()).isEqualTo("TestMember1의 groupMember");
        assertThat(getCommentDTO.getComments().get(1).getCreatedAt()).isEqualTo(testTime);

    }


    @Test
    void getComment는_해당_그룹을_이미_탈퇴한_회원은_댓글조회가_불가능하다(){
        //given
        Long memberId = 2L;
        Long groupId = 1L;
        Long postId = 1L;
        int pageNo = 0;

        //when
        //then
        assertThatThrownBy(()-> {
            commentService.getComment(memberId, groupId, postId, pageNo);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");

    }

    @Test
    void getComment는_해당_회원이_가입한적없는_회원이면_댓글조회가_불가능하다(){
        //given
        Long memberId = 3L;
        Long groupId = 1L;
        Long postId = 1L;
        int pageNo = 0;

        //when
        //then
        assertThatThrownBy(()-> {
            commentService.getComment(memberId, groupId, postId, pageNo);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");
    }

    @Test
    void getComment는_존재하지_않는_게시글의_댓글은_조회가_불가능하다(){
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Long postId = 100L;
        int pageNo = 0;

        //when
        //then
        assertThatThrownBy(()-> {
            commentService.getComment(memberId, groupId, postId, pageNo);
        }).isInstanceOf(Exception404.class).hasMessage("존재하지 않는 글 입니다.");
    }

    @Test
    void createComment는_모든_조건을_만족하는_회원은_댓글생성이_가능하다(){
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Long postId = 1L;
        String content = "새로운 Comment 생성";

        //when
        CommentResponse.createCommentDTO result = commentService.createComment(memberId, groupId, postId, content);

        //then
        assertThat(result.getCommentId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("새로운 Comment 생성");
        assertThat(result.getNickName()).isEqualTo("TestMember1의 groupMember");
    }

    @Test
    void createComment는_해당_그룹을_이미_탈퇴한_회원은_댓글생성이_불가능하다(){
        //given
        Long memberId = 2L;
        Long groupId = 1L;
        Long postId = 1L;
        String content = "새로운 Comment 생성";

        //when
        //then
        assertThatThrownBy(()-> {
            commentService.createComment(memberId, groupId, postId, content);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");

    }

    @Test
    void createComment는_해당_회원이_그룹에_가입한적없는_회원이면_댓글생성이_불가능하다(){
        //given
        Long memberId = 3L;
        Long groupId = 1L;
        Long postId = 1L;
        String content = "새로운 Comment 생성";

        //when
        //then
        assertThatThrownBy(()-> {
            commentService.createComment(memberId, groupId, postId, content);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");

    }

    @Test
    void createComment는_존재하지_않는_게시글에는_댓글생성이_불가능하다(){
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Long postId = 100L;
        String content = "새로운 Comment 생성";

        //when
        //then
        assertThatThrownBy(()-> {
            commentService.createComment(memberId, groupId, postId, content);
        }).isInstanceOf(Exception404.class).hasMessage("존재하지 않는 글 입니다.");

    }

    @Test
    void updateComment는_모든_조건을_만족하는_회원은_댓글수정이_가능하다(){
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Long commentId = 1L;
        String updateContent = "수정된 댓글입니다.";

        //when
        CommentResponse.updateCommentDTO result = commentService.updateComment(memberId, groupId, commentId, updateContent);

        //then
        assertThat(result.getCommentId()).isEqualTo(1L);
        assertThat(result.getNewContent()).isEqualTo("수정된 댓글입니다.");

    }

    @Test
    void updateComment는_해당_그룹을_이미_탈퇴한_회원은_댓글수정이_불가능하다(){
        //given
        Long memberId = 2L;
        Long groupId = 1L;
        Long commentId = 1L;
        String updateContent = "수정된 댓글입니다.";

        //when
        //then
        assertThatThrownBy(()-> {
            commentService.updateComment(memberId, groupId, commentId, updateContent);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");

    }

    @Test
    void updateComment는_해당_회원이_가입한적없는_회원이면_댓글수정이_불가능하다(){
        //given
        Long memberId = 3L;
        Long groupId = 1L;
        Long commentId = 1L;
        String updateContent = "수정된 댓글입니다.";

        //when
        //then
        assertThatThrownBy(()-> {
            commentService.updateComment(memberId, groupId, commentId, updateContent);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");

    }

    @Test
    void updateComment는_존재하지_않는_댓글에_수정이_불가능하다(){
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Long commentId = 100L;
        String updateContent = "수정된 댓글입니다.";

        //when
        //then
        assertThatThrownBy(()-> {
            commentService.updateComment(memberId, groupId, commentId, updateContent);
        }).isInstanceOf(Exception404.class).hasMessage("존재하지 않는 댓글 입니다.");

    }

    @Test
    void updateComment는_댓글작성자가_아니면_수정이_불가능하다(){
        //given
        Long memberId = 4L;
        Long groupId = 1L;
        Long commentId = 1L;
        String updateContent = "수정된 댓글입니다.";

        //when
        //then
        assertThatThrownBy(()-> {
            commentService.updateComment(memberId, groupId, commentId, updateContent);
        }).isInstanceOf(Exception400.class).hasMessage("본인이 쓴 댓글만 수정이 가능합니다.");

    }

    @Test
    void updateComment는_기존_댓글과_내용이_동일하도록_수정이_불가능하다(){
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Long commentId = 1L;
        String updateContent = "TestMember1의 Test Comment 입니다.";

        //when
        //then
        assertThatThrownBy(()-> {
            commentService.updateComment(memberId, groupId, commentId, updateContent);
        }).isInstanceOf(Exception400.class).hasMessage("기존 댓글과 동일한 내용입니다.");

    }

    @Test
    void deleteComment는_모든_조건을_만족하는_회원은_댓글삭제가_가능하다(){
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Long commentId = 1L;

        //when
        CommentResponse.deleteCommentDTO result = commentService.deleteComment(memberId, groupId, commentId);

        //then
        assertThat(fakeCommentRepository.findCommentWithGroupMember(1L)).isEmpty();

    }

    @Test
    void deleteComment는_해당_그룹을_이미_탈퇴한_회원은_댓글삭제가_불가능하다(){
        //given
        Long memberId = 2L;
        Long groupId = 1L;
        Long commentId = 1L;

        //when
        //then
        assertThatThrownBy(()-> {
            commentService.deleteComment(memberId, groupId, commentId);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");

    }

    @Test
    void deleteComment는_해당_회원이_가입한적없는_회원이면_댓글삭제가_불가능하다(){
        //given
        Long memberId = 3L;
        Long groupId = 1L;
        Long commentId = 1L;

        //when
        //then
        assertThatThrownBy(()-> {
            commentService.deleteComment(memberId, groupId, commentId);
        }).isInstanceOf(Exception404.class).hasMessage("해당 그룹에 속한 회원이 아닙니다.");

    }

    @Test
    void deleteComment는_존재하지_않는_댓글삭제가_불가능하다(){
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Long commentId = 100L;

        //when
        //then
        assertThatThrownBy(()-> {
            commentService.deleteComment(memberId, groupId, commentId);
        }).isInstanceOf(Exception404.class).hasMessage("존재하지 않는 댓글 입니다.");
    }

    @Test
    void deleteComment는_댓글작성자가_아니면_삭제가_불가능하다(){
        //given
        Long memberId = 4L;
        Long groupId = 1L;
        Long commentId = 1L;

        //when
        //then
        assertThatThrownBy(()-> {
            commentService.deleteComment(memberId, groupId, commentId);
        }).isInstanceOf(Exception400.class).hasMessage("본인이 쓴 댓글만 삭제가 가능합니다.");

    }




}
