package TeamDustKGU.dustbackend.comment.service;

import TeamDustKGU.dustbackend.board.domain.Board;
import TeamDustKGU.dustbackend.comment.domain.Comment;
import TeamDustKGU.dustbackend.comment.exception.CommentErrorCode;
import TeamDustKGU.dustbackend.common.ServiceTest;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static TeamDustKGU.dustbackend.fixture.BoardFixture.BOARD_1;
import static TeamDustKGU.dustbackend.fixture.CommentFixture.COMMENT_1;
import static TeamDustKGU.dustbackend.fixture.UserFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Comment [Service Layer] -> CommentFindService 테스트")
public class CommentFindServiceTest extends ServiceTest {
    @Autowired
    private CommentFindService commentFindService;

    private User writer;
    private Board board;
    private Comment comment;

    @BeforeEach
    void setup() {
        writer = userRepository.save(SUNKYOUNG.toUser());
        board = boardRepository.save(BOARD_1.toBoard(writer));
        comment = commentRepository.save(COMMENT_1.toComment(writer, board));
    }

    @Test
    @DisplayName("ID(PK)로 댓글을 조회한다")
    void findById() {
        // when
        Comment findComment = commentFindService.findById(comment.getId());

        // then
        assertThatThrownBy(() -> commentFindService.findById(comment.getId() + 100L))
                .isInstanceOf(DustException.class)
                .hasMessage(CommentErrorCode.COMMENT_NOT_FOUND.getMessage());

        assertThat(findComment).isEqualTo(comment);
    }
}
