package TeamDustKGU.dustbackend.board.service.like;

import TeamDustKGU.dustbackend.board.domain.Board;
import TeamDustKGU.dustbackend.board.domain.like.BoardLike;
import TeamDustKGU.dustbackend.board.exception.BoardErrorCode;
import TeamDustKGU.dustbackend.common.ServiceTest;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static TeamDustKGU.dustbackend.fixture.BoardFixture.BOARD_0;
import static TeamDustKGU.dustbackend.fixture.UserFixture.CHAERIN;
import static TeamDustKGU.dustbackend.fixture.UserFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Board [Service Layer] -> BoardLikeService 테스트")
public class BoardLikeServiceTest extends ServiceTest {
    @Autowired
    private BoardLikeService boardLikeService;

    private User userA;
    private User userB;
    private Board board;

    @BeforeEach
    void setup() {
        userA = userRepository.save(SUNKYOUNG.toUser());
        userB = userRepository.save(CHAERIN.toUser());
        board = boardRepository.save(BOARD_0.toBoard(userA));
    }

    @Nested
    @DisplayName("게시글좋아요 등록")
    class register {
        @Test
        @DisplayName("본인의 게시글은 좋아요를 누를 수 없다")
        void throwExceptionBySelfBoardLikeNotAllowed() {
            assertThatThrownBy(() -> boardLikeService.register(userA.getId(), board.getId()))
                    .isInstanceOf(DustException.class)
                    .hasMessage(BoardErrorCode.SELF_BOARD_LIKE_NOT_ALLOWED.getMessage());
        }

        @Test
        @DisplayName("한 게시글에 두 번 이상 좋아요를 누를 수 없다")
        void throwExceptionByAlreadyBoardLike() {
            // given
            boardLikeService.register(userB.getId(), board.getId());

            // when - then
            assertThatThrownBy(() -> boardLikeService.register(userB.getId(), board.getId()))
                    .isInstanceOf(DustException.class)
                    .hasMessage(BoardErrorCode.ALREADY_BOARD_LIKE.getMessage());
        }

        @Test
        @DisplayName("게시글좋아요 등록에 성공한다")
        void success() {
            // when
            Long likeBoardId = boardLikeService.register(userB.getId(), board.getId());

            // then
            BoardLike findBoardLike = boardLikeRepository.findById(likeBoardId).orElseThrow();
            assertAll(
                    () -> assertThat(boardLikeRepository.countByBoard(board)).isEqualTo(1),
                    () -> assertThat(findBoardLike.getUser().getId()).isEqualTo(userB.getId()),
                    () -> assertThat(findBoardLike.getBoard().getId()).isEqualTo(board.getId())
            );
        }
    }

    @Nested
    @DisplayName("게시글좋아요 취소")
    class cancel {
        @Test
        @DisplayName("좋아요를 누르지 않은 게시글의 좋아요는 취소할 수 없다")
        void throwExceptionByBoardLikeNotFound() {
            // when - then
            assertThatThrownBy(() -> boardLikeService.cancel(userB.getId(), board.getId()))
                    .isInstanceOf(DustException.class)
                    .hasMessage(BoardErrorCode.BOARD_LIKE_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("게시글좋아요 취소에 성공한다")
        void success() {
            // given
            boardLikeService.register(userB.getId(), board.getId());

            // when
            boardLikeService.cancel(userB.getId(), board.getId());

            // then
            assertThat(boardLikeRepository.existsByUserIdAndBoardId(userB.getId(), board.getId())).isFalse();
        }
    }
}