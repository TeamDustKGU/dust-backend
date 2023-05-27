package TeamDustKGU.dustbackend.fixture;

import TeamDustKGU.dustbackend.board.domain.Board;
import TeamDustKGU.dustbackend.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardFixture {
    BOARD_0("제목0", "내용0"),
    BOARD_1("제목1", "내용1"),
    BOARD_2("제목2", "내용2")
    ;

    private final String title;
    private final String content;

    public Board toBoard(User writer) {
        return Board.createBoard(title, content, writer);
    }
}
