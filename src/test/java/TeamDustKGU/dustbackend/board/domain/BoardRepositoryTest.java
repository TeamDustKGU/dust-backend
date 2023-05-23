package TeamDustKGU.dustbackend.board.domain;

import TeamDustKGU.dustbackend.common.RepositoryTest;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static TeamDustKGU.dustbackend.fixture.BoardFixture.BOARD_1;
import static TeamDustKGU.dustbackend.fixture.UserFixture.CHAERIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Board [Repository Layer] -> BoardRepository 테스트")
public class BoardRepositoryTest extends RepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    private User writer;
    private Board board;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @BeforeEach
    void setup() {
        writer = userRepository.save(CHAERIN.toUser());
        board = boardRepository.save(BOARD_1.toBoard(writer));
    }

    @Test
    @DisplayName("게시글을 등록한다")
    void saveBoard() {
        assertAll(
                () -> assertThat(board.getTitle()).isEqualTo(BOARD_1.getTitle()),
                () -> assertThat(board.getContent()).isEqualTo(BOARD_1.getContent()),
                () -> assertThat(board.getView()).isEqualTo(0),
                () -> assertThat(board.getWriter()).isEqualTo(writer)
        );
    }

    @Test
    @DisplayName("게시글 BaseTimeEntity 등록한다")
    void saveBaseTimeEntity() {
        assertAll(
                () -> assertThat(board.getCreatedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter)),
                () -> assertThat(board.getCreatedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter))
        );
    }

    @Test
    @DisplayName("게시글을 삭제한다")
    void deleteBoard() {
        //when
        boardRepository.delete(board);

        //then
        assertThat(boardRepository.findById(board.getId())).isEqualTo(Optional.empty());
    }
}

