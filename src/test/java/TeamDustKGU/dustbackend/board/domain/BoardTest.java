package TeamDustKGU.dustbackend.board.domain;

import TeamDustKGU.dustbackend.board.Board;
import TeamDustKGU.dustbackend.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static TeamDustKGU.dustbackend.fixture.BoardFixture.BOARD_1;
import static TeamDustKGU.dustbackend.fixture.BoardFixture.BOARD_2;
import static TeamDustKGU.dustbackend.fixture.UserFixture.CHAERIN;
import static TeamDustKGU.dustbackend.fixture.UserFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Board 도메인 테스트")
public class BoardTest {
    @Test
    @DisplayName("게시글을 생성한다")
    void createBoard() {
        User writer = CHAERIN.toUser();
        Board board = BOARD_1.toBoard(writer);

        assertAll(
                () -> assertThat(board.getTitle()).isEqualTo(BOARD_1.getTitle()),
                () -> assertThat(board.getContent()).isEqualTo(BOARD_1.getContent()),
                () -> assertThat(board.getView()).isEqualTo(0),
                () -> assertThat(board.getWriter()).isEqualTo(writer)
        );
    }

    @Test
    @DisplayName("게시글 제목을 수정한다")
    void updateTitle() {
        User writer1 = CHAERIN.toUser();
        User writer2 = SUNKYOUNG.toUser();
        Board board1 = BOARD_1.toBoard(writer1);
        Board board2 = BOARD_2.toBoard(writer2);

        board1.updateTitle("제목2");
        board2.updateTitle("제목1");

        assertAll(
                () -> assertThat(board1.getTitle()).isEqualTo("제목2"),
                () -> assertThat(board2.getTitle()).isEqualTo("제목1")
        );
    }

    @Test
    @DisplayName("게시글 내용을 수정한다")
    void updateContent() {
        User writer1 = CHAERIN.toUser();
        User writer2 = SUNKYOUNG.toUser();
        Board board1 = BOARD_1.toBoard(writer1);
        Board board2 = BOARD_2.toBoard(writer2);

        board1.updateContent("내용2");
        board2.updateContent("내용1");

        assertAll(
                () -> assertThat(board1.getContent()).isEqualTo("내용2"),
                () -> assertThat(board2.getContent()).isEqualTo("내용1")
        );
    }
}
