package com.kakao.techcampus.wekiki.comment.service.port;

import com.kakao.techcampus.wekiki.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface CommentRepository {
    Page<Comment> findCommentsByPostIdWithGroupMembers(Long postId, PageRequest pageRequest);
    Comment save(Comment comment);
    void delete(Comment comment);
    Optional<Comment> findCommentWithGroupMember(Long commentId);
    Comment update(Comment updatedComment);
}
