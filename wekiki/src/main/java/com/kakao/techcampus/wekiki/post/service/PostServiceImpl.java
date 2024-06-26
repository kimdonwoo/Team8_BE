package com.kakao.techcampus.wekiki.post.service;


import com.kakao.techcampus.wekiki._core.error.exception.Exception400;
import com.kakao.techcampus.wekiki._core.error.exception.Exception404;
import com.kakao.techcampus.wekiki.group.domain.GroupMember;
import com.kakao.techcampus.wekiki.group.service.port.GroupMemberRepository;
import com.kakao.techcampus.wekiki.history.domain.History;
import com.kakao.techcampus.wekiki.history.service.port.HistoryRepository;
import com.kakao.techcampus.wekiki.pageInfo.domain.PageInfo;
import com.kakao.techcampus.wekiki.pageInfo.service.port.PageRepository;
import com.kakao.techcampus.wekiki.post.controller.port.PostCreateService;
import com.kakao.techcampus.wekiki.post.controller.port.PostDeleteService;
import com.kakao.techcampus.wekiki.post.controller.port.PostReadService;
import com.kakao.techcampus.wekiki.post.controller.port.PostUpdateService;
import com.kakao.techcampus.wekiki.post.controller.request.PostRequest;
import com.kakao.techcampus.wekiki.post.controller.response.PostResponse;
import com.kakao.techcampus.wekiki.post.domain.Post;
import com.kakao.techcampus.wekiki.post.service.port.PostRepository;
import com.kakao.techcampus.wekiki.report.domain.Report;
import com.kakao.techcampus.wekiki.report.service.port.ReportRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Builder
public class PostServiceImpl implements PostReadService, PostCreateService, PostUpdateService, PostDeleteService {

    private final PageRepository pageRepository;
    private final PostRepository postRepository;
    private final HistoryRepository historyRepository;
    private final ReportRepository reportRepository;
    private final GroupMemberRepository groupMemberJPARepository;
    final int HISTORY_COUNT = 5;

    @Override
    @Transactional
    public PostResponse.createPostDTO createPost(Long memberId, Long groupId, PostRequest.createPostDTO request){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        GroupMember activeGroupMember = checkGroupMember(memberId, groupId);

        // 2. pageId로 PageInfo 객체 들고오기
        PageInfo pageInfo = checkPageFromPageId(request.getPageId());
        PageInfo updatedPageInfo = pageRepository.update(pageInfo.updatePage());

        // 3. parentPostId로 parentPost 가져오기
        Post parent = null;
        if(request.getParentPostId() != 0) {
            parent = postRepository.findById(request.getParentPostId()).orElseThrow(
                    () -> new Exception404("존재하지 않는 상위 글입니다."));
        }

        // 4. 같은 pageId를 가진 Post들 중에 입력받은 order보다 높은 모든 Post들의 order를 1씩 증가
        postRepository.findPostsByPageIdAndOrderGreaterThan(request.getPageId(), request.getOrder()).stream()
                .map(post -> post.plusOrder())
                .forEach(post -> postRepository.update(post));

        // 5. Post 엔티티 생성하고 저장하기
        Post savedPost = postRepository.save(Post.from(request,parent, activeGroupMember,updatedPageInfo));

        // 6. 히스토리 생성
        historyRepository.save(History.from(savedPost, activeGroupMember));

        // 7. return DTO
        log.info(memberId + " 님이 " + groupId + " 그룹의 "  + request.getPageId() + " 페이지에 "+ request.getTitle()+" 포스트를 생성하였습니다.");
        return new PostResponse.createPostDTO(savedPost);
    }

    @Override
    @Transactional
    public PostResponse.modifyPostDTO modifyPost(Long memberId , Long groupId, Long postId , String title, String content){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        GroupMember activeGroupMember = checkGroupMember(memberId, groupId);

        // 2. postId로 post 엔티티 가져오기
        Post post = checkPostFromPostIdWithPage(postId);

        // 3. page 최근 수정 시간 바꾸기
        pageRepository.update(post.getPageInfo().updatePage());

        // 4. 현재 Post랑 내용 같은지 확인
        if(post.getTitle().equals(title) && post.getContent().equals(content)){
            throw new Exception400("기존 글과 동일한 글입니다.");
        }

        // 5. 다르면 Post 수정후 히스토리 생성 저장
        Post updatedPost = postRepository.update(post.modifyPost(activeGroupMember, title, content));
        historyRepository.save(History.from(updatedPost, activeGroupMember));

        // 6. return DTO
        log.info(memberId + " 님이 " + groupId + " 그룹에 "+ title+" 포스트를 수정하였습니다.");
        return new PostResponse.modifyPostDTO(updatedPost);
    }

    @Override
    @Transactional
    public PostResponse.getPostHistoryDTO getPostHistory(Long memberId, Long groupId, Long postId , int pageNo){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        checkGroupMember(memberId, groupId);

        // 2. postId로 post 엔티티 가져오기
        Post post = checkPostFromPostId(postId);

        // 3. 해당 PostId로 history 모두 가져오기 시간순 + 페이지네이션
        Page<History> historys = historyRepository.findHistoryWithMemberByPostId(postId, PageRequest.of(pageNo, HISTORY_COUNT));

        // 4. DTO로 return
        List<PostResponse.getPostHistoryDTO.historyDTO> historyDTOs = historys.getContent().stream().
                map(h -> new PostResponse.getPostHistoryDTO.historyDTO(h.getGroupMember(),h)).collect(Collectors.toList());

        log.info(memberId + " 님이 " + groupId + " 그룹에 "+ postId +" 포스트의 히스토리를 조회합니다.");
        return new PostResponse.getPostHistoryDTO(post,historyDTOs);

    }

    @Override
    @Transactional
    public PostResponse.deletePostDTO deletePost(Long memberId, Long groupId, Long postId){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        checkGroupMember(memberId, groupId);

        // 2. postId로 post 엔티티 가져오기
        Post post = checkPostFromPostIdWithPage(postId);

        // 3. parent로 해당 postId를 가지고 있는 post가 있는지 확인 -> 존재하면 Exception
        if(postRepository.existsByParentId(postId)){
            throw new Exception400("하위 글이 존재하는 글은 삭제가 불가능합니다.");
        }

        // 4. child post 존재 안하면 history + post 삭제 시키기
        PostResponse.deletePostDTO response = new PostResponse.deletePostDTO(post);
        postRepository.deleteById(postId);

        // 5. order값 앞으로 땡기기
        postRepository.findPostsByPageIdAndOrderGreaterThan(post.getPageInfo().getId(), post.getOrders()).stream()
                .map(p -> p.minusOrder())
                .forEach(p -> postRepository.update(p));

        // 6. return DTO;
        log.info(memberId + " 님이 " + groupId + " 그룹에 "+ postId +" 포스트를 삭제합니다.");
        return response;

    }

    @Override
    @Transactional
    public PostResponse.createReportDTO createReport(Long memberId, Long groupId, Long postId , String content){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        GroupMember activeGroupMember = checkGroupMember(memberId, groupId);

        // 2. postId로 post 엔티티 가져오기
        checkPostFromPostId(postId);

        // 3. postId의 최근 히스토리 가져오기
        List<History> HistoryByPostId = historyRepository.findHistoryByPostId(postId, PageRequest.of(0, 1));

        // 4. report 생성
        Report savedReport = reportRepository.save(Report.from(activeGroupMember,HistoryByPostId.get(0),content));

        // 5. return DTO
        log.info(memberId + " 님이 " + groupId + " 그룹에 "+ postId +" 포스트를 신고합니다.");
        return new PostResponse.createReportDTO(savedReport);

    }


    public GroupMember checkGroupMember(Long memberId, Long groupId){

        GroupMember activeGroupMember = groupMemberJPARepository.findGroupMemberByMemberIdAndGroupIdFetchJoin(memberId, groupId)
                .orElseThrow(() -> new Exception404("해당 그룹에 속한 회원이 아닙니다."));
        if(!activeGroupMember.isActiveStatus()) throw new Exception404("해당 그룹에 속한 회원이 아닙니다.");
        if(activeGroupMember.getMember() == null) throw new Exception404("존재하지 않는 회원입니다.");
        if(activeGroupMember.getGroup() == null) throw new Exception404("존재하지 않는 그룹입니다.");

        return activeGroupMember;
    }

    public PageInfo checkPageFromPageId(Long pageId){
        return pageRepository.findById(pageId)
                .orElseThrow(() -> new Exception404("존재하지 않는 페이지 입니다."));
    }

    public Post checkPostFromPostId(Long postId){
        return postRepository.findById(postId)
                .orElseThrow(() -> new Exception404("존재하지 않는 글 입니다."));
    }

    public Post checkPostFromPostIdWithPage(Long postId){
        return postRepository.findPostWithPageFromPostId(postId)
                .orElseThrow(() -> new Exception404("존재하지 않는 글 입니다."));
    }

}
