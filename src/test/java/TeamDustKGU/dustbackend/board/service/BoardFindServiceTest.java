package TeamDustKGU.dustbackend.board.service;

import TeamDustKGU.dustbackend.board.domain.Board;
import TeamDustKGU.dustbackend.board.exception.BoardErrorCode;
import TeamDustKGU.dustbackend.common.ServiceTest;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static TeamDustKGU.dustbackend.fixture.BoardFixture.BOARD_1;
import static TeamDustKGU.dustbackend.fixture.UserFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Board [Service Layer] -> BoardFindService 테스트")
public class BoardFindServiceTest extends ServiceTest {
    @Autowired
    private BoardFindService boardFindService;

    private Board board;

    @BeforeEach
    void setup() {
        User writer = userRepository.save(SUNKYOUNG.toUser());
        board = boardRepository.save(BOARD_1.toBoard(writer));
    }

    @Test
    @DisplayName("ID(PK)로 게시글을 조회한다")
    void findById() {
        // when
        Board findBoard = boardFindService.findById(board.getId());

        // then
        assertThatThrownBy(() -> boardFindService.findById(board.getId() + 100L))
                .isInstanceOf(DustException.class)
                .hasMessage(BoardErrorCode.BOARD_NOT_FOUND.getMessage());

        assertThat(findBoard).isEqualTo(board);
    }
}
