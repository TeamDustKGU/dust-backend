package TeamDustKGU.dustbackend.board.service;

import TeamDustKGU.dustbackend.board.domain.Board;
import TeamDustKGU.dustbackend.board.domain.BoardRepository;
import TeamDustKGU.dustbackend.board.exception.BoardErrorCode;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.service.UserFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final UserFindService userFindService;
    private final BoardFindService boardFindService;
    private final BoardRepository boardRepository;
    
    @Transactional
    public Long create(Long writerId, String title, String content){
        User writer = userFindService.findById(writerId);
        Board board = Board.createBoard(title, content, writer);

        return boardRepository.save(board).getId();
    }

    @Transactional
    public void update(Long writerId, Long boardId, String title, String content){
        Board board = boardFindService.findById(boardId);
        validateUpdate(writerId, boardId);

        board.updateTitle(title);
        board.updateContent(content);
    }

    @Transactional
    public void delete(Long writerId, Long boardId){
        Board board = boardFindService.findById(boardId);
        validateDelete(writerId, boardId);

        boardRepository.delete(board);
    }

    private void validateUpdate(Long writerId, Long boardId) {
        Board board = boardFindService.findById(boardId);
        if (!board.getWriter().getId().equals(writerId)) {
            throw DustException.type(BoardErrorCode.UPDATE_BOARD_ONLY_WRITER);
        }
    }

    private void validateDelete(Long writerId, Long boardId) {
        Board board = boardFindService.findById(boardId);
        if (!board.getWriter().getId().equals(writerId)) {
            throw DustException.type(BoardErrorCode.DELETE_BOARD_ONLY_WRITER);
        }
    }
}
