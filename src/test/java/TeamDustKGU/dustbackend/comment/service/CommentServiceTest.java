package TeamDustKGU.dustbackend.comment.service;

import TeamDustKGU.dustbackend.board.domain.Board;
import TeamDustKGU.dustbackend.comment.domain.Comment;
import TeamDustKGU.dustbackend.comment.exception.CommentErrorCode;
import TeamDustKGU.dustbackend.common.ServiceTest;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static TeamDustKGU.dustbackend.fixture.BoardFixture.BOARD_1;
import static TeamDustKGU.dustbackend.fixture.CommentFixture.*;
import static TeamDustKGU.dustbackend.fixture.UserFixture.CHAERIN;
import static TeamDustKGU.dustbackend.fixture.UserFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Comment [Service Layer] -> CommentService 테스트")
public class CommentServiceTest extends ServiceTest {
    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentFindService commentFindService;

    private User writer;
    private User not_writer;
    private Board board;
    private final Comment[] comments = new Comment[5];
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @BeforeEach
    void setup() {
        writer = userRepository.save(SUNKYOUNG.toUser());
        not_writer = userRepository.save(CHAERIN.toUser());
        board = boardRepository.save(BOARD_1.toBoard(writer));
        comments[0] = commentRepository.save(COMMENT_0.toComment(writer, board));
        comments[1] = commentRepository.save(COMMENT_1.toComment(writer, board));
        comments[2] = commentRepository.save(COMMENT_2.toComment(writer, board));
        comments[3] = commentRepository.save(COMMENT_3.toComment(writer, board));
        comments[4] = commentRepository.save(COMMENT_4.toComment(writer, board));
    }

    @Test
    @DisplayName("댓글 등록에 성공한다")
    void success() {
        // when - then
        Comment findComment = commentRepository.findById(comments[0].getId()).orElseThrow();
        assertAll(
                () -> assertThat(findComment.getWriter().getId()).isEqualTo(writer.getId()),
                () -> assertThat(findComment.getBoard().getId()).isEqualTo(board.getId()),
                () -> assertThat(findComment.getParent()).isEqualTo(null),
                () -> assertThat(findComment.getContent()).isEqualTo(COMMENT_0.getContent()),
                () -> assertThat(findComment.getCreatedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter)),
                () -> assertThat(findComment.getModifiedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter))
        );
    }

    @Nested
    @DisplayName("댓글 삭제")
    class delete {
        @Test
        @DisplayName("다른 사람의 댓글은 삭제할 수 없다")
        void throwExceptionByUserIsNotCommentWriter() {
            // when - then
            assertThatThrownBy(() -> commentService.delete(not_writer.getId(), comments[0].getId()))
                    .isInstanceOf(DustException.class)
                    .hasMessage(CommentErrorCode.USER_IS_NOT_COMMENT_WRITER.getMessage());
        }

        @Test
        @DisplayName("게시글의 특정 댓글을 삭제할 수 있다")
        void successDeleteSpecificComment() {
            // when
            commentService.delete(writer.getId(), comments[0].getId());
            commentService.delete(writer.getId(), comments[1].getId());

            // then
            assertAll(
                    () -> assertThat(commentRepository.countByBoard(board)).isEqualTo(3L),
                    () -> assertThat(commentRepository.existsById(comments[0].getId())).isFalse(),
                    () -> assertThat(commentRepository.existsById(comments[1].getId())).isFalse()
            );
        }

        @Test
        @DisplayName("댓글 삭제에 성공한다")
        void success() {
            // given
            commentService.delete(writer.getId(), comments[0].getId());

            // when - then
            assertThatThrownBy(() -> commentFindService.findById(comments[0].getId()))
                    .isInstanceOf(DustException.class)
                    .hasMessage(CommentErrorCode.COMMENT_NOT_FOUND.getMessage());
        }
    }
}
