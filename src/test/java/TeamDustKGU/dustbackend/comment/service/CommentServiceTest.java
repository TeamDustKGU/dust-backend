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
import static TeamDustKGU.dustbackend.fixture.ChildCommentFixture.*;
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
    private final Comment[] parentComments = new Comment[5];
    private final Comment[] childComments = new Comment[4];
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @BeforeEach
    void setup() {
        writer = userRepository.save(SUNKYOUNG.toUser());
        not_writer = userRepository.save(CHAERIN.toUser());
        board = boardRepository.save(BOARD_1.toBoard(writer));
        parentComments[0] = commentRepository.save(COMMENT_0.toComment(writer, board));
        parentComments[1] = commentRepository.save(COMMENT_1.toComment(writer, board));
        parentComments[2] = commentRepository.save(COMMENT_2.toComment(writer, board));
        parentComments[3] = commentRepository.save(COMMENT_3.toComment(writer, board));
        parentComments[4] = commentRepository.save(COMMENT_4.toComment(writer, board));
        childComments[0] = commentRepository.save(CHILD_COMMENT_0.toChildComment(writer, board, parentComments[0]));
        childComments[1] = commentRepository.save(CHILD_COMMENT_1.toChildComment(writer, board, parentComments[0]));
        childComments[2] = commentRepository.save(CHILD_COMMENT_2.toChildComment(writer, board, parentComments[0]));
        childComments[3] = commentRepository.save(CHILD_COMMENT_3.toChildComment(writer, board, parentComments[0]));
    }

    @Nested
    @DisplayName("댓글 등록")
    class createComment {
        @Test
        @DisplayName("댓글 등록에 성공한다")
        void success() {
            // when - then
            Comment findComment = commentRepository.findById(parentComments[0].getId()).orElseThrow();
            assertAll(
                    () -> assertThat(findComment.getWriter().getId()).isEqualTo(writer.getId()),
                    () -> assertThat(findComment.getBoard().getId()).isEqualTo(board.getId()),
                    () -> assertThat(findComment.getParent()).isEqualTo(null),
                    () -> assertThat(findComment.getContent()).isEqualTo(COMMENT_0.getContent()),
                    () -> assertThat(findComment.getCreatedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter)),
                    () -> assertThat(findComment.getModifiedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter))
            );
        }
    }

    @Nested
    @DisplayName("댓글 삭제")
    class delete {
        @Test
        @DisplayName("다른 사람의 댓글은 삭제할 수 없다")
        void throwExceptionByUserIsNotCommentWriter() {
            // when - then
            assertThatThrownBy(() -> commentService.delete(not_writer.getId(), parentComments[0].getId()))
                    .isInstanceOf(DustException.class)
                    .hasMessage(CommentErrorCode.USER_IS_NOT_COMMENT_WRITER.getMessage());
        }

        @Test
        @DisplayName("게시글의 특정 댓글을 삭제할 수 있다")
        void successDeleteSpecificComment() {
            // when
            flushAndClear();
            commentService.delete(writer.getId(), parentComments[0].getId());
            commentService.delete(writer.getId(), parentComments[1].getId());

            // then
            assertAll(
                    () -> assertThat(commentRepository.countByBoard(board)).isEqualTo(3L),
                    () -> assertThat(commentRepository.existsById(parentComments[0].getId())).isFalse(),
                    () -> assertThat(commentRepository.existsById(parentComments[1].getId())).isFalse()
            );
        }

        @Test
        @DisplayName("댓글이 삭제되면 달린 대댓글도 삭제되어야 한다.")
        void successDeleteAllChildComment() {
            // when
            flushAndClear();
            commentService.delete(writer.getId(), parentComments[0].getId());

            // then
            assertThat(commentRepository.countByParent(parentComments[0])).isEqualTo(0L);
        }

        @Test
        @DisplayName("댓글 삭제에 성공한다")
        void success() {
            // when
            flushAndClear();
            commentService.delete(writer.getId(), parentComments[0].getId());

            // then
            assertThatThrownBy(() -> commentFindService.findById(parentComments[0].getId()))
                    .isInstanceOf(DustException.class)
                    .hasMessage(CommentErrorCode.COMMENT_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("대댓글 등록")
    class createChildComment {
        @Test
        @DisplayName("대댓글 등록에 성공한다")
        void successChild() {
            // when - then
            Comment findComment = commentRepository.findById(childComments[0].getId()).orElseThrow();

            assertAll(
                    () -> assertThat(findComment.getWriter().getId()).isEqualTo(writer.getId()),
                    () -> assertThat(findComment.getBoard().getId()).isEqualTo(board.getId()),
                    () -> assertThat(findComment.getParent().getId()).isEqualTo(parentComments[0].getId()),
                    () -> assertThat(findComment.getContent()).isEqualTo(CHILD_COMMENT_0.getContent()),
                    () -> assertThat(findComment.getCreatedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter)),
                    () -> assertThat(findComment.getModifiedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter))
            );
        }
    }

    @Nested
    @DisplayName("대댓글 삭제")
    class deleteChildComment {
        @Test
        @DisplayName("댓글의 특정 대댓글을 삭제할 수 있다")
        void successDeleteSpecificChildComment() {
            // when
            commentService.delete(writer.getId(), childComments[0].getId());
            commentService.delete(writer.getId(), childComments[1].getId());

            // then
            assertAll(
                    () -> assertThat(commentRepository.countByParent(parentComments[0])).isEqualTo(2L),
                    () -> assertThat(commentRepository.existsById(childComments[0].getId())).isFalse(),
                    () -> assertThat(commentRepository.existsById(childComments[1].getId())).isFalse()
            );
        }

        @Test
        @DisplayName("대댓글 삭제에 성공한다")
        void childSuccess() {
            // when
            commentService.delete(writer.getId(), childComments[0].getId());

            // then
            assertThatThrownBy(() -> commentFindService.findById(childComments[0].getId()))
                    .isInstanceOf(DustException.class)
                    .hasMessage(CommentErrorCode.COMMENT_NOT_FOUND.getMessage());
        }
    }
}
