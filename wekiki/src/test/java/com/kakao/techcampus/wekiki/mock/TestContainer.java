package com.kakao.techcampus.wekiki.mock;

import com.kakao.techcampus.wekiki.pageInfo.controller.port.*;
import com.kakao.techcampus.wekiki.pageInfo.facade.PageInfoUpdateRedissonLockFacadeImpl;
import com.kakao.techcampus.wekiki._core.utils.port.RedisUtils;
import com.kakao.techcampus.wekiki._core.utils.port.SecurityUtils;
import com.kakao.techcampus.wekiki.comment.controller.CommentController;
import com.kakao.techcampus.wekiki.comment.controller.port.CommentCreateService;
import com.kakao.techcampus.wekiki.comment.controller.port.CommentDeleteService;
import com.kakao.techcampus.wekiki.comment.controller.port.CommentReadService;
import com.kakao.techcampus.wekiki.comment.controller.port.CommentUpdateService;
import com.kakao.techcampus.wekiki.comment.domain.Comment;
import com.kakao.techcampus.wekiki.comment.service.CommentServiceImpl;
import com.kakao.techcampus.wekiki.comment.service.port.CommentRepository;
import com.kakao.techcampus.wekiki.group.domain.Group;
import com.kakao.techcampus.wekiki.group.domain.GroupMember;
import com.kakao.techcampus.wekiki.group.service.port.GroupMemberRepository;
import com.kakao.techcampus.wekiki.group.service.port.GroupRepository;
import com.kakao.techcampus.wekiki.history.domain.History;
import com.kakao.techcampus.wekiki.history.service.port.HistoryRepository;
import com.kakao.techcampus.wekiki.member.domain.Member;
import com.kakao.techcampus.wekiki.member.infrastructure.Authority;
import com.kakao.techcampus.wekiki.member.service.port.MemberRepository;
import com.kakao.techcampus.wekiki.pageInfo.controller.PageInfoController;
import com.kakao.techcampus.wekiki.pageInfo.domain.PageInfo;
import com.kakao.techcampus.wekiki.pageInfo.infrastructure.PageIndexGeneratorImpl;
import com.kakao.techcampus.wekiki.pageInfo.service.PageServiceImpl;
import com.kakao.techcampus.wekiki.pageInfo.service.port.PageIndexGenerator;
import com.kakao.techcampus.wekiki.pageInfo.service.port.PageRepository;
import com.kakao.techcampus.wekiki.post.controller.PostController;
import com.kakao.techcampus.wekiki.post.controller.port.PostCreateService;
import com.kakao.techcampus.wekiki.post.controller.port.PostDeleteService;
import com.kakao.techcampus.wekiki.post.controller.port.PostReadService;
import com.kakao.techcampus.wekiki.post.controller.port.PostUpdateService;
import com.kakao.techcampus.wekiki.post.domain.Post;
import com.kakao.techcampus.wekiki.post.service.PostServiceImpl;
import com.kakao.techcampus.wekiki.post.service.port.PostRepository;
import com.kakao.techcampus.wekiki.report.service.port.ReportRepository;
import lombok.Builder;
import lombok.Getter;
import org.redisson.api.RedissonClient;

import java.time.LocalDateTime;


public class TestContainer {

    public final PageInfoController pageInfoController;
    public final PageInfoCreateService pageInfoCreateService;
    public final PageInfoDeleteService pageInfoDeleteService;
    public final PageInfoReadService pageInfoReadService;
    public final PageInfoUpdateService pageInfoUpdateService;
    public final PageRepository pageRepository;
    public final PostRepository postRepository;
    public final PageIndexGenerator pageIndexGenerator;
    public final SecurityUtils securityUtils;
    public final RedisUtils redisUtils;
    public final MemberRepository memberRepository;
    public final GroupMemberRepository groupMemberRepository;
    public final GroupRepository groupRepository;

    public final PageInfoUpdateRedissonLockFacade pageInfoUpdateRedissonLockFacade;
    public final RedissonClient redissonClient;

    public final PostController postRestController;
    public final PostReadService postReadService;
    public final PostCreateService postCreateService;
    public final PostUpdateService postUpdateService;
    public final PostDeleteService postDeleteService;
    public final HistoryRepository historyRepository;
    public final ReportRepository reportRepository;

    public final CommentController commentController;
    private final CommentReadService commentReadService;
    private final CommentCreateService commentCreateService;
    private final CommentUpdateService commentUpdateService;
    private final CommentDeleteService commentDeleteService;

    private final CommentRepository commentRepository;
    @Getter
    private final LocalDateTime testTime = LocalDateTime.now();

    @Builder
    public TestContainer(){
        this.pageRepository = new FakePageInfoRepository();
        this.postRepository = new FakePostRepository();
        this.pageIndexGenerator = new PageIndexGeneratorImpl();
        this.redisUtils = new FakeRedisUtils();
        this.memberRepository = new FakeMemberRepository();
        this.groupMemberRepository = new FakeGroupMemberRepository();
        this.groupRepository = new FakeGroupRepository();
        this.historyRepository = new FakeHistoryRepository();
        this.reportRepository = new FakeReportRepository();
        this.commentRepository = new FakeCommentRepository();
        this.securityUtils = new FakeSecurityUtils();
        this.redissonClient = new FakeRedissonClient();

        PageServiceImpl pageService = PageServiceImpl.builder()
                .pageRepository(this.pageRepository)
                .postRepository(this.postRepository)
                .groupMemberRepository(this.groupMemberRepository)
                .pageIndexGenerator(this.pageIndexGenerator)
                .redisUtils(this.redisUtils)
                .memberRepository(this.memberRepository)
                .groupMemberRepository(this.groupMemberRepository)
                .groupRepository(this.groupRepository)
                .build();
        this.pageInfoCreateService = pageService;
        this.pageInfoDeleteService = pageService;
        this.pageInfoReadService = pageService;
        this.pageInfoUpdateService = pageService;

        PostServiceImpl postService = PostServiceImpl.builder()
                .pageRepository(this.pageRepository)
                .postRepository(this.postRepository)
                .historyRepository(this.historyRepository)
                .reportRepository(this.reportRepository)
                .groupMemberJPARepository(this.groupMemberRepository)
                .build();
        this.postReadService = postService;
        this.postCreateService = postService;
        this.postUpdateService = postService;
        this.postDeleteService = postService;

        CommentServiceImpl commentService = CommentServiceImpl.builder()
                .commentRepository(this.commentRepository)
                .postRepository(this.postRepository)
                .groupMemberRepository(this.groupMemberRepository)
                .build();
        this.commentReadService = commentService;
        this.commentCreateService = commentService;
        this.commentUpdateService = commentService;
        this.commentDeleteService = commentService;

        PageInfoUpdateRedissonLockFacadeImpl pageInfoUpdateRedissonLockFacade = PageInfoUpdateRedissonLockFacadeImpl.builder()
                .redissonClient(this.redissonClient)
                .pageInfoUpdateService(this.pageInfoUpdateService)
                .build();

        this.pageInfoUpdateRedissonLockFacade = pageInfoUpdateRedissonLockFacade;

        this.pageInfoController = PageInfoController.builder()
                .pageInfoCreateService(this.pageInfoCreateService)
                .pageInfoDeleteService(this.pageInfoDeleteService)
                .pageInfoReadService(this.pageInfoReadService)
                .pageInfoUpdateRedissonLockFacade(this.pageInfoUpdateRedissonLockFacade)
                //.pageInfoUpdateService(this.pageInfoUpdateService)
                .securityUtils(this.securityUtils)
                .build();
        this.postRestController = PostController.builder()
                .postReadService(this.postReadService)
                .postCreateService(this.postCreateService)
                .postDeleteService(this.postDeleteService)
                .postUpdateService(this.postUpdateService)
                .securityUtils(this.securityUtils)
                .build();
        this.commentController = CommentController.builder()
                .commentCreateService(this.commentCreateService)
                .commentDeleteService(this.commentDeleteService)
                .commentReadService(this.commentReadService)
                .commentUpdateService(this.commentUpdateService)
                .securityUtils(this.securityUtils)
                .build();

    }

    // CommentController 테스트를 위한
    // member, group , GroupMember, Page, Post, Comment 한개 씩 생성
    public void testCommentControllerSetting(){

        Member member1 = Member.builder()
                .id(1L)
                .name("TestMember1")
                .email("test1@naver.com")
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
                .created_at( testTime)
                .activeStatus(true)
                .build();

        GroupMember savedGroupMember1 = groupMemberRepository.save(groupMember1);

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

        PageInfo savedPageInfo1 = pageRepository.save(pageInfo);

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

        Post savedPost = postRepository.save(post);

        Comment comment1 = Comment.builder()
                .groupMember(savedGroupMember1)
                .post(savedPost)
                .content("TestMember1의 Test Comment1 입니다.")
                .created_at( testTime)
                .build();

        commentRepository.save(comment1);

        Comment comment2 = Comment.builder()
                .groupMember(savedGroupMember1)
                .post(savedPost)
                .content("TestMember1의 Test Comment2 입니다.")
                .created_at( testTime)
                .build();

        commentRepository.save(comment2);
    }

    // PageInfoController 테스트를 위한
    // member, group , GroupMember, Page, Post 한개 씩 생성
    public void testPageInfoControllerSetting(){

        Member member1 = Member.builder()
                .id(1L)
                .name("TestMember1")
                .email("test1@naver.com")
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
                .created_at( testTime)
                .activeStatus(true)
                .build();

        GroupMember savedGroupMember1 = groupMemberRepository.save(groupMember1);

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

        PageInfo savedPageInfo1 = pageRepository.save(pageInfo);
        redisUtils.saveKeyAndHashValue("GROUP_"+savedPageInfo1.getGroup().getId(),savedPageInfo1.getPageName(), savedPageInfo1.getId().toString());

        Post post = Post.builder()
                //.parent(parent.toModel())
                .orders(0)
                .groupMember(savedGroupMember1)
                .pageInfo(savedPageInfo1)
                //.historys(historyEntities.stream().map(HistoryEntity::toModel).toList())
                //.comments(commentEntities.stream().map(CommentEntity::toModel).toList())
                .title("Test Post1")
                .content("해당 포스트는 테스트용 포스트1입니다.")
                .created_at( testTime)
                .build();

        Post savedPost = postRepository.save(post);

        Post post2 = Post.builder()
                //.parent(parent.toModel())
                .orders(1)
                .groupMember(savedGroupMember1)
                .pageInfo(savedPageInfo1)
                //.historys(historyEntities.stream().map(HistoryEntity::toModel).toList())
                //.comments(commentEntities.stream().map(CommentEntity::toModel).toList())
                .title("Test Post2")
                .content("해당 포스트는 테스트용 포스트2입니다.")
                .created_at( testTime)
                .build();

        Post savedPost2 = postRepository.save(post2);

        PageInfo pageInfo2 = PageInfo.builder()
                .group(group)
                .pageName("Test Page2")
                //.posts(postEntities.stream().map(PostEntity::toModel).toList())
                .goodCount(0)
                .badCount(0)
                .viewCount(0)
                .created_at( testTime)
                .updated_at( testTime)
                .build();

        PageInfo savedPageInfo2 = pageRepository.save(pageInfo2);
        redisUtils.saveKeyAndHashValue("GROUP_"+savedPageInfo2.getGroup().getId(),savedPageInfo2.getPageName(), savedPageInfo2.getId().toString());



    }

    // PostController 테스트를 위한
    // member, group , GroupMember, Page, Post 한개 씩 생성
    public void testPostControllerSetting(){

        Member member1 = Member.builder()
                .id(1L)
                .name("TestMember1")
                .email("test1@naver.com")
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
                .created_at( testTime)
                .activeStatus(true)
                .build();

        GroupMember savedGroupMember1 = groupMemberRepository.save(groupMember1);

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

        PageInfo savedPageInfo1 = pageRepository.save(pageInfo);
        redisUtils.saveKeyAndHashValue("GROUP_"+savedPageInfo1.getGroup().getId(),savedPageInfo1.getPageName(), savedPageInfo1.getId().toString());

        Post post = Post.builder()
                //.parent(parent.toModel())
                .orders(0)
                .groupMember(savedGroupMember1)
                .pageInfo(savedPageInfo1)
                //.historys(historyEntities.stream().map(HistoryEntity::toModel).toList())
                //.comments(commentEntities.stream().map(CommentEntity::toModel).toList())
                .title("Test Post1")
                .content("해당 포스트는 테스트용 포스트1입니다.")
                .created_at( testTime)
                .build();

        Post savedPost = postRepository.save(post);
        historyRepository.save(History.from(savedPost, savedGroupMember1));

        Post post2 = Post.builder()
                //.parent(parent.toModel())
                .orders(1)
                .groupMember(savedGroupMember1)
                .pageInfo(savedPageInfo1)
                //.historys(historyEntities.stream().map(HistoryEntity::toModel).toList())
                //.comments(commentEntities.stream().map(CommentEntity::toModel).toList())
                .title("Test Post2")
                .content("해당 포스트는 테스트용 포스트2입니다.")
                .created_at( testTime)
                .build();

        Post savedPost2 = postRepository.save(post2);
        historyRepository.save(History.from(savedPost2, savedGroupMember1));

        PageInfo pageInfo2 = PageInfo.builder()
                .group(group)
                .pageName("Test Page2")
                //.posts(postEntities.stream().map(PostEntity::toModel).toList())
                .goodCount(0)
                .badCount(0)
                .viewCount(0)
                .created_at( testTime)
                .updated_at( testTime)
                .build();

        PageInfo savedPageInfo2 = pageRepository.save(pageInfo2);
        redisUtils.saveKeyAndHashValue("GROUP_"+savedPageInfo2.getGroup().getId(),savedPageInfo2.getPageName(), savedPageInfo2.getId().toString());



    }


}
