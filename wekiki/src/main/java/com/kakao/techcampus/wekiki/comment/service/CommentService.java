package com.kakao.techcampus.wekiki.comment.service;

import com.kakao.techcampus.wekiki._core.error.exception.Exception400;
import com.kakao.techcampus.wekiki._core.error.exception.Exception404;
import com.kakao.techcampus.wekiki.comment.controller.response.CommentResponse;
import com.kakao.techcampus.wekiki.comment.domain.Comment;
import com.kakao.techcampus.wekiki.comment.infrastructure.CommentJPARepository;
import com.kakao.techcampus.wekiki.group.domain.GroupMember;
import com.kakao.techcampus.wekiki.group.infrastructure.GroupMemberJPARepository;
import com.kakao.techcampus.wekiki.post.domain.Post;
import com.kakao.techcampus.wekiki.post.infrastructure.PostJPARepository;
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
public class CommentService {

    private final CommentJPARepository commentJPARepository;
    private final PostJPARepository postJPARepository;
    private final GroupMemberJPARepository groupMemberJPARepository;
    final int COMMENT_COUNT = 10;

    @Transactional
    public CommentResponse.getCommentDTO getComment(Long memberId, Long groupId, Long postId, int pageNo){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        GroupMember activeGroupMember = checkGroupMember(memberId, groupId);

        // 2. post 존재하는지 예외처리
        Post post = checkPostFromPostId(postId);

        // 3. postId로 Comment 다 가져오기
        Page<Comment> comments = commentJPARepository.findCommentsByPostIdWithGroupMembers(postId, PageRequest.of(pageNo, COMMENT_COUNT));

        // 4. return DTO
        List<CommentResponse.getCommentDTO.commentDTO> commentDTOs = comments.getContent()
                .stream().map(c -> new CommentResponse.getCommentDTO.commentDTO(c,c.getGroupMember(), c.getGroupMember().getId() == activeGroupMember.getId()))
                .collect(Collectors.toList());

        log.info(memberId + " 님이 " + groupId + " 그룹에 "+ postId +" 포스트의 댓글을 조회합니다.");
        return new CommentResponse.getCommentDTO(post,commentDTOs);
    }

    @Transactional
    public CommentResponse.createCommentDTO createComment(Long memberId, Long groupId, Long postId, String content){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        GroupMember activeGroupMember = checkGroupMember(memberId, groupId);

        // 2. post 존재하는지 예외처리
        Post post = checkPostFromPostId(postId);

        // 3. comment 생성
        Comment comment = Comment.builder()
                .groupMember(activeGroupMember)
                .content(content)
                .created_at(LocalDateTime.now())
                .build();
        post.addComment(comment);
        Comment savedComment = commentJPARepository.save(comment);

        // 4. return DTO
        log.info(memberId + " 님이 " + groupId + " 그룹에 "+ postId +" 포스트의 댓글을 생성합니다.");
        return new CommentResponse.createCommentDTO(savedComment,activeGroupMember.getNickName());
    }

    @Transactional
    public CommentResponse.deleteCommentDTO deleteComment(Long memberId, Long groupId, Long commentId){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        GroupMember activeGroupMember = checkGroupMember(memberId, groupId);

        // 2. comment 존재하는지 예외처리
        Comment comment = checkCommentFromCommentId(commentId);

        // 3. comment 쓴 사람이 삭제하는 유저랑 일치하는지 확인
        if(comment.getGroupMember().getId() != activeGroupMember.getId()){
            throw new Exception400("본인이 쓴 댓글만 삭제가 가능합니다.");
        }

        // 4. comment 삭제
        CommentResponse.deleteCommentDTO response = new CommentResponse.deleteCommentDTO(comment);
        commentJPARepository.delete(comment);

        // 5. return DTO
        log.info(memberId + " 님이 " + groupId + " 그룹에 "+ commentId +" 댓글을 삭제합니다.");
        return response;
    }

    @Transactional
    public CommentResponse.updateCommentDTO updateComment(Long memberId, Long groupId, Long commentId, String updateContent){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        GroupMember activeGroupMember = checkGroupMember(memberId, groupId);

        // 2. comment 존재하는지 예외처리
        Comment comment = checkCommentFromCommentId(commentId);

        // 3. comment 쓴 사람이 수정하는 유저랑 일치하는지 확인
        if(comment.getGroupMember().getId() != activeGroupMember.getId()){
            throw new Exception400("본인이 쓴 댓글만 수정이 가능합니다.");
        }

        // 4. 내용 동일하면 exception
        if(comment.getContent().equals(updateContent)){
            throw new Exception400("기존 댓글과 동일한 내용입니다.");
        }

        // 5. 수정
        comment.updateContent(updateContent);

        // 6. return DTO
        log.info(memberId + " 님이 " + groupId + " 그룹에 "+ commentId +" 댓글을 수정합니다.");
        return new CommentResponse.updateCommentDTO(comment);
    }


    public GroupMember checkGroupMember(Long memberId, Long groupId){

        GroupMember activeGroupMember = groupMemberJPARepository.findGroupMemberByMemberIdAndGroupIdFetchJoin(memberId, groupId)
                .orElseThrow(() -> new Exception404("해당 그룹에 속한 회원이 아닙니다."));
        if(!activeGroupMember.isActiveStatus()) throw new Exception404("해당 그룹에 속한 회원이 아닙니다.");
        if(activeGroupMember.getMember() == null) throw new Exception404("존재하지 않는 회원입니다.");
        if(activeGroupMember.getGroup() == null) throw new Exception404("존재하지 않는 그룹입니다.");

        return activeGroupMember;
    }

    public Post checkPostFromPostId(Long postId){
        return postJPARepository.findById(postId)
                .orElseThrow(() -> new Exception404("존재하지 않는 글 입니다."));
    }

    public Comment checkCommentFromCommentId(Long commentId){
        return commentJPARepository.findCommentWithGroupMember(commentId).
                orElseThrow(() -> new Exception404("존재하지 않는 댓글 입니다."));

    }

}
