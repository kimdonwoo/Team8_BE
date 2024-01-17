package com.kakao.techcampus.wekiki.comment.infrastructure;

import com.kakao.techcampus.wekiki.comment.domain.Comment;
import com.kakao.techcampus.wekiki.comment.service.port.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJPARepository commentJPARepository;

    @Override
    public Page<Comment> findCommentsByPostIdWithGroupMembers(Long postId, PageRequest pageRequest) {
        return commentJPARepository.findCommentsByPostIdWithGroupMembers(postId,pageRequest);
    }

    @Override
    public Comment save(Comment comment) {
        return commentJPARepository.save(comment);
    }

    @Override
    public void delete(Comment comment) {
        commentJPARepository.delete(comment);
    }

    @Override
    public Optional<Comment> findCommentWithGroupMember(Long commentId) {
        return commentJPARepository.findCommentWithGroupMember(commentId);
    }
}
