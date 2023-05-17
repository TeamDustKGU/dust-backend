package TeamDustKGU.dustbackend.comment.domain;

import TeamDustKGU.dustbackend.board.Board;
import TeamDustKGU.dustbackend.comment.Comment;
import TeamDustKGU.dustbackend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static TeamDustKGU.dustbackend.fixture.BoardFixture.BOARD_1;
import static TeamDustKGU.dustbackend.fixture.CommentFixture.Comment_1;
import static TeamDustKGU.dustbackend.fixture.UserFixture.CHAERIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Comment 도메인 테스트")
public class CommentTest {
    private User writer;
    private Board board;
    private Comment comment;

    @BeforeEach
    void setUp() {
        writer = CHAERIN.toUser();
        board = BOARD_1.toBoard(writer);
        comment = Comment_1.toComment(writer,board);
    }

    @Test
    @DisplayName("댓글을 생성한다")
    void createComment() {
        assertAll(
                () -> assertThat(comment.getContent()).isEqualTo(Comment_1.getComment()),
                () -> assertThat(comment.getParent()).isEqualTo(Comment_1.getParent()),
                () -> assertThat(comment.getWriter()).isEqualTo(writer),
                () -> assertThat(comment.getBoard()).isEqualTo(board)
        );
    }

    @Test
    @DisplayName("자식 댓글을 생성한다")
    void createChildComment() {
        Comment parentComment = comment;
        Comment childComment = new Comment("자식 댓글1",parentComment,writer,board);

        assertAll(
                () -> assertThat(childComment.getContent()).isEqualTo("자식 댓글1"),
                () -> assertThat(childComment.getParent()).isEqualTo(parentComment),
                () -> assertThat(childComment.getWriter()).isEqualTo(writer),
                () -> assertThat(childComment.getBoard()).isEqualTo(board)
        );
    }
}
