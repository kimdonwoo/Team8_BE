package com.kakao.techcampus.wekiki.comment.service;

import com.kakao.techcampus.wekiki._core.error.exception.Exception400;
import com.kakao.techcampus.wekiki._core.error.exception.Exception404;
import com.kakao.techcampus.wekiki.comment.controller.port.CommentCreateService;
import com.kakao.techcampus.wekiki.comment.controller.port.CommentDeleteService;
import com.kakao.techcampus.wekiki.comment.controller.port.CommentReadService;
import com.kakao.techcampus.wekiki.comment.controller.port.CommentUpdateService;
import com.kakao.techcampus.wekiki.comment.controller.response.CommentResponse;
import com.kakao.techcampus.wekiki.comment.domain.Comment;
import com.kakao.techcampus.wekiki.comment.service.port.CommentRepository;
import com.kakao.techcampus.wekiki.group.domain.GroupMember;
import com.kakao.techcampus.wekiki.group.service.port.GroupMemberRepository;
import com.kakao.techcampus.wekiki.post.domain.Post;
import com.kakao.techcampus.wekiki.post.service.port.PostRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Builder
public class CommentServiceImpl implements CommentReadService, CommentUpdateService, CommentDeleteService, CommentCreateService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final GroupMemberRepository groupMemberRepository;
    final int COMMENT_COUNT = 10;

    @Override
    @Transactional
    public CommentResponse.getCommentDTO getComment(Long memberId, Long groupId, Long postId, int pageNo){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        GroupMember activeGroupMember = checkGroupMember(memberId, groupId);

        // 2. post 존재하는지 예외처리
        Post post = checkPostFromPostId(postId);

        // 3. postId로 Comment 다 가져오기
        Page<Comment> comments = commentRepository.findCommentsByPostIdWithGroupMembers(postId, PageRequest.of(pageNo, COMMENT_COUNT));

        // 4. return DTO
        List<CommentResponse.getCommentDTO.commentDTO> commentDTOs = comments.getContent()
                .stream().map(c -> new CommentResponse.getCommentDTO.commentDTO(c,c.getGroupMember(), c.getGroupMember().getId() == activeGroupMember.getId()))
                .collect(Collectors.toList());

        log.info(memberId + " 님이 " + groupId + " 그룹에 "+ postId +" 포스트의 댓글을 조회합니다.");
        return new CommentResponse.getCommentDTO(post,commentDTOs);
    }

    @Override
    @Transactional
    public CommentResponse.createCommentDTO createComment(Long memberId, Long groupId, Long postId, String content){

        // 1. 존재하는 Member, Group, GroupMember 인지 fetch join으로 하나의 쿼리로 확인
        GroupMember activeGroupMember = checkGroupMember(memberId, groupId);

        // 2. post 존재하는지 예외처리
        Post post = checkPostFromPostId(postId);

        // 3. comment 불변 객체 생성 + 저장
        Comment comment = Comment.from(activeGroupMember,post,content);
        Comment savedComment = commentRepository.save(comment);

        // 4. return DTO
        log.info(memberId + " 님이 " + groupId + " 그룹에 "+ postId +" 포스트의 댓글을 생성합니다.");
        return new CommentResponse.createCommentDTO(savedComment, activeGroupMember.getNickName());
    }

    @Override
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
        Comment updatedComment = commentRepository.save(comment.updateContent(updateContent));

        // 6. return DTO
        log.info(memberId + " 님이 " + groupId + " 그룹에 "+ commentId +" 댓글을 수정합니다.");
        return new CommentResponse.updateCommentDTO(updatedComment);
    }

    @Override
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
        commentRepository.delete(comment);

        // 5. return DTO
        log.info(memberId + " 님이 " + groupId + " 그룹에 "+ commentId +" 댓글을 삭제합니다.");
        return response;
    }


    private GroupMember checkGroupMember(Long memberId, Long groupId){

        GroupMember activeGroupMember = groupMemberRepository.findGroupMemberByMemberIdAndGroupIdFetchJoin(memberId, groupId)
                .orElseThrow(() -> new Exception404("해당 그룹에 속한 회원이 아닙니다."));
        if(!activeGroupMember.isActiveStatus()) throw new Exception404("해당 그룹에 속한 회원이 아닙니다.");
        if(activeGroupMember.getMember() == null) throw new Exception404("존재하지 않는 회원입니다.");
        if(activeGroupMember.getGroup() == null) throw new Exception404("존재하지 않는 그룹입니다.");

        return activeGroupMember;
    }

    private Post checkPostFromPostId(Long postId){
        return postRepository.findById(postId)
                .orElseThrow(() -> new Exception404("존재하지 않는 글 입니다."));
    }

    private Comment checkCommentFromCommentId(Long commentId){
        return commentRepository.findCommentWithGroupMember(commentId).
                orElseThrow(() -> new Exception404("존재하지 않는 댓글 입니다."));

    }

}
