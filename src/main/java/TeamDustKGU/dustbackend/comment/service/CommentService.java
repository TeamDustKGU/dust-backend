package TeamDustKGU.dustbackend.comment.service;

import TeamDustKGU.dustbackend.board.domain.Board;
import TeamDustKGU.dustbackend.board.service.BoardFindService;
import TeamDustKGU.dustbackend.comment.domain.Comment;
import TeamDustKGU.dustbackend.comment.domain.CommentRepository;
import TeamDustKGU.dustbackend.comment.exception.CommentErrorCode;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.service.UserFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final UserFindService userFindService;
    private final BoardFindService boardFindService;
    private final CommentFindService commentFindService;
    private final CommentRepository commentRepository;

    @Transactional
    public Long create(Long writerId, Long boardId, String content){
        User writer = userFindService.findById(writerId);
        Board board = boardFindService.findById(boardId);
        Comment comment = Comment.createComment(writer, board, null, content);

        return commentRepository.save(comment).getId();
    }

    @Transactional
    public void delete(Long writerId, Long commentId){
        validateWriter(commentId, writerId);
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public Long createChild(Long writerId, Long boardId, Long parentCommentId, String content){
        User writer = userFindService.findById(writerId);
        Board board = boardFindService.findById(boardId);
        Comment parentComment = commentFindService.findById(parentCommentId);
        Comment childComment = Comment.createComment(writer, board, parentComment, content);

        return commentRepository.save(childComment).getId();
    }

    private void validateWriter(Long commentId, Long writerId) {
        Comment comment = commentFindService.findById(commentId);
        if (!comment.getWriter().getId().equals(writerId)) {
            throw DustException.type(CommentErrorCode.USER_IS_NOT_COMMENT_WRITER);
        }
    }
}
