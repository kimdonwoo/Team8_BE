package com.kakao.techcampus.wekiki.comment.infrastructure;

import com.kakao.techcampus.wekiki.comment.domain.Comment;
import com.kakao.techcampus.wekiki.comment.service.port.CommentRepository;
import com.kakao.techcampus.wekiki.post.infrastructure.PostEntity;
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
        return commentJPARepository.findCommentsByPostIdWithGroupMembers(postId,pageRequest)
                .map(CommentEntity::toModelWithGroupMember);
    }

    @Override
    public Comment save(Comment comment) {
        CommentEntity newComment = CommentEntity.createFromModel(comment);
        newComment.getPostEntity().addCommentEntity(newComment);
        return commentJPARepository.save(newComment).toPureModel();
    }

    @Override
    public Comment update(Comment updatedComment){
        CommentEntity commentEntity = commentJPARepository.findById(updatedComment.getId()).get();
        commentEntity.update(updatedComment);
        return commentEntity.toPureModel();
    }


    @Override
    public void delete(Comment comment) {
        commentJPARepository.delete(CommentEntity.fromPureModel(comment));
    }

    @Override
    public Optional<Comment> findCommentWithGroupMember(Long commentId) {
        return commentJPARepository.findCommentWithGroupMember(commentId).map(CommentEntity::toModelWithGroupMember);
    }
}
