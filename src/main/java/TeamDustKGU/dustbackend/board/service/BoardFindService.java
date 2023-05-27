package TeamDustKGU.dustbackend.board.service;

import TeamDustKGU.dustbackend.board.domain.Board;
import TeamDustKGU.dustbackend.board.domain.BoardRepository;
import TeamDustKGU.dustbackend.board.exception.BoardErrorCode;
import TeamDustKGU.dustbackend.global.exception.DustException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardFindService {
    private final BoardRepository boardRepository;

    @Transactional
    public Board findById(Long boardId){
        return boardRepository.findById(boardId)
                .orElseThrow(() -> DustException.type(BoardErrorCode.BOARD_NOT_FOUND));
    }
}
