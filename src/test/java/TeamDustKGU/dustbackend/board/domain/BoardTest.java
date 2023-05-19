package TeamDustKGU.dustbackend.board.domain;

import TeamDustKGU.dustbackend.comment.domain.Comment;
import TeamDustKGU.dustbackend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
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
    private final User[] writer = new User[2];
    private final Board[] board = new Board[2];

    @BeforeEach
    void setUp() {
        writer[0] = CHAERIN.toUser();
        writer[1] = SUNKYOUNG.toUser();
        board[0] = BOARD_1.toBoard(writer[0]);
        board[1] = BOARD_2.toBoard(writer[1]);
    }

    @Test
    @DisplayName("게시글을 생성한다")
    void createBoard() {
        assertAll(
                () -> assertThat(board[0].getTitle()).isEqualTo(BOARD_1.getTitle()),
                () -> assertThat(board[0].getContent()).isEqualTo(BOARD_1.getContent()),
                () -> assertThat(board[0].getView()).isEqualTo(0),
                () -> assertThat(board[0].getWriter()).isEqualTo(writer[0])
        );
    }

    @Test
    @DisplayName("게시글 제목을 수정한다")
    void updateTitle() {
        board[0].updateTitle("제목2");
        board[1].updateTitle("제목1");

        assertAll(
                () -> assertThat(board[0].getTitle()).isEqualTo("제목2"),
                () -> assertThat(board[1].getTitle()).isEqualTo("제목1")
        );
    }

    @Test
    @DisplayName("게시글 내용을 수정한다")
    void updateContent() {
        board[0].updateContent("내용2");
        board[1].updateContent("내용1");

        assertAll(
                () -> assertThat(board[0].getContent()).isEqualTo("내용2"),
                () -> assertThat(board[1].getContent()).isEqualTo("내용1")
        );
    }

    @Test
    @DisplayName("게시글에 부모 댓글을 추가한다")
    void addComment() {
        for(int i=1; i<=2; i++){
            board[0].addComment("댓글"+i, writer[0]);
        }
        for(int i=3; i<=5; i++){
            board[0].addComment("댓글"+i, writer[1]);
        }

        assertAll(
                () -> assertThat(board[0].getCommentList()).hasSize(5),
                () -> assertThat(board[0].getCommentList())
                        .map(Comment::getContent)
                        .containsExactlyInAnyOrder("댓글1", "댓글2", "댓글3", "댓글4", "댓글5"),
                () -> assertThat(board[0].getCommentList())
                        .map(Comment::getWriter)
                        .containsExactlyInAnyOrder(writer[0], writer[0], writer[1], writer[1], writer[1])
        );
    }
}
