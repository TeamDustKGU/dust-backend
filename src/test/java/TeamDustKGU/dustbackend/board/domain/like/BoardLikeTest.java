package TeamDustKGU.dustbackend.board.domain.like;

import TeamDustKGU.dustbackend.board.domain.Board;
import TeamDustKGU.dustbackend.fixture.BoardFixture;
import TeamDustKGU.dustbackend.fixture.UserFixture;
import TeamDustKGU.dustbackend.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BoardLike 도메인 테스트")
public class BoardLikeTest {
    private static final User user = UserFixture.SUNKYOUNG.toUser();
    private static final Board board = BoardFixture.BOARD_0.toBoard(user);

    @Test
    @DisplayName("BoardLike를 생성한다")
    void registerFollow() {
        BoardLike boardLike = BoardLike.registerBoardLike(user, board);
        Assertions.assertAll(
                () -> assertThat(boardLike.getUser()).isEqualTo(user),
                () -> assertThat(boardLike.getBoard()).isEqualTo(board)
        );
    }
}

