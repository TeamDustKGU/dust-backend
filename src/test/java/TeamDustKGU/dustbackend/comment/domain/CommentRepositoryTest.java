package TeamDustKGU.dustbackend.comment.domain;

import TeamDustKGU.dustbackend.board.domain.Board;
import TeamDustKGU.dustbackend.board.domain.BoardRepository;
import TeamDustKGU.dustbackend.common.RepositoryTest;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static TeamDustKGU.dustbackend.fixture.BoardFixture.BOARD_1;
import static TeamDustKGU.dustbackend.fixture.CommentFixture.*;
import static TeamDustKGU.dustbackend.fixture.UserFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Comment [Repository Layer] -> CommentRepository 테스트")
class CommentRepositoryTest extends RepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Board board;
    private final Comment[] comments = new Comment[5];

    @BeforeEach
    void setup() {
        User writer = userRepository.save(SUNKYOUNG.toUser());
        board = boardRepository.save(BOARD_1.toBoard(writer));
        comments[0] = commentRepository.save(COMMENT_0.toComment(writer, board));
        comments[1] = commentRepository.save(COMMENT_1.toComment(writer, board));
        comments[2] = commentRepository.save(COMMENT_2.toComment(writer, board));
        comments[3] = commentRepository.save(COMMENT_3.toComment(writer, board));
        comments[4] = commentRepository.save(COMMENT_4.toComment(writer, board));
    }

    @Test
    void countByBoard() {
        assertThat(commentRepository.countByBoard(board)).isEqualTo(5L);
    }
}