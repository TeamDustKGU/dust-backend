package TeamDustKGU.dustbackend.comment.service;

import TeamDustKGU.dustbackend.comment.domain.Comment;
import TeamDustKGU.dustbackend.comment.domain.CommentRepository;
import TeamDustKGU.dustbackend.comment.exception.CommentErrorCode;
import TeamDustKGU.dustbackend.global.exception.DustException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentFindService {
    private final CommentRepository commentRepository;

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> DustException.type(CommentErrorCode.COMMENT_NOT_FOUND));
    }
}
