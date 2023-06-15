package TeamDustKGU.dustbackend.board.service.like;

import TeamDustKGU.dustbackend.board.domain.Board;
import TeamDustKGU.dustbackend.board.domain.like.BoardLike;
import TeamDustKGU.dustbackend.board.domain.like.BoardLikeRepository;
import TeamDustKGU.dustbackend.board.exception.BoardErrorCode;
import TeamDustKGU.dustbackend.board.service.BoardFindService;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.service.UserFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardLikeService {
    private final UserFindService userFindService;
    private final BoardFindService boardFindService;
    private final BoardLikeRepository boardLikeRepository;

    @Transactional
    public Long register(Long userId, Long boardId){
        validateSelfBoardLike(userId, boardId);
        validateAlreadyBoardLike(userId, boardId);

        User user = userFindService.findById(userId);
        Board board = boardFindService.findById(boardId);
        BoardLike likeBoard = BoardLike.registerBoardLike(user, board);

        return boardLikeRepository.save(likeBoard).getId();
    }

    private void validateSelfBoardLike(Long userId, Long boardId) {
        Board board = boardFindService.findById(boardId);
        if (board.getWriter().getId().equals(userId)) {
            throw DustException.type(BoardErrorCode.SELF_BOARD_LIKE_NOT_ALLOWED);
        }
    }

    private void validateAlreadyBoardLike(Long userId, Long boardId) {
        if (boardLikeRepository.existsByUserIdAndBoardId(userId, boardId)) {
            throw DustException.type(BoardErrorCode.ALREADY_BOARD_LIKE);
        }
    }

    @Transactional
    public void cancel(Long userId, Long boardId){
        validateCancel(userId, boardId);
        boardLikeRepository.deleteByUserIdAndBoardId(userId, boardId);
    }

    private void validateCancel(Long userId, Long boardId) {
        if (!boardLikeRepository.existsByUserIdAndBoardId(userId, boardId)) {
            throw DustException.type(BoardErrorCode.BOARD_LIKE_NOT_FOUND);
        }
    }
}
