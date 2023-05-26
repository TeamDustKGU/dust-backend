package TeamDustKGU.dustbackend.comment.service;

import TeamDustKGU.dustbackend.board.domain.Board;
import TeamDustKGU.dustbackend.board.service.BoardFindService;
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
import static TeamDustKGU.dustbackend.fixture.CommentFixture.COMMENT_1;
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

    @Autowired
    private BoardFindService boardFindService;

    private User writer;
    private User not_writer;
    private Board board;
    private Comment comment;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @BeforeEach
    void setup() {
        writer = userRepository.save(SUNKYOUNG.toUser());
        not_writer = userRepository.save(CHAERIN.toUser());
        board = boardRepository.save(BOARD_1.toBoard(writer));
        comment = commentRepository.save(COMMENT_1.toComment(writer, board));
    }

    @Test
    @DisplayName("댓글 등록에 성공한다")
    void success() {
        // given
        Long commentId = commentService.create(writer.getId(), board.getId(), "내용");

        // when - then
        Comment findComment = commentRepository.findById(commentId).orElseThrow();
        assertAll(
                () -> assertThat(findComment.getWriter().getId()).isEqualTo(writer.getId()),
                () -> assertThat(findComment.getBoard().getId()).isEqualTo(board.getId()),
                () -> assertThat(findComment.getParent()).isEqualTo(null),
                () -> assertThat(findComment.getContent()).isEqualTo("내용"),
                () -> assertThat(findComment.getCreatedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter)),
                () -> assertThat(findComment.getModifiedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter))
        );
    }

    @Nested
    @DisplayName("댓글 삭제")
    class delete {

        @Test
        @DisplayName("다른 사람의 댓글은 삭제할 수 없다")
        void throwExceptionByUserIsNotWriter() {
            // when - then
            assertThatThrownBy(() -> commentService.delete(not_writer.getId(),comment.getId()))
                    .isInstanceOf(DustException.class)
                    .hasMessage(CommentErrorCode.USER_NOT_COMMENT_WRITER.getMessage());
        }

        @Test
        @DisplayName("게시글의 특정 댓글을 삭제할 수 있다")
        void successDeleteSpecificComment() {
            Long[] commentId = new Long[5];
            // given
            for(int i=0; i<5; i++){ //댓글 5개 생성 -> 총 6개
                commentId[i] = commentService.create(writer.getId(), board.getId(), "댓글"+i);
            }
            flushAndClear();

            // when
            Board findBoard = boardFindService.findById(board.getId());
            commentService.delete(writer.getId(), commentId[0]);
            commentService.delete(writer.getId(), commentId[1]);

            // then
            assertAll(
                    () -> assertThat(commentRepository.count()).isEqualTo(4),
                    () -> assertThat(findBoard.getCommentList()).hasSize(4)
            );
        }

        @Test
        @DisplayName("댓글 삭제에 성공한다")
        void success() {
            // given
            commentService.delete(writer.getId(), comment.getId());

            // when - then
            assertThatThrownBy(() -> commentFindService.findById(comment.getId() + 100L))
                    .isInstanceOf(DustException.class)
                    .hasMessage(CommentErrorCode.COMMENT_NOT_FOUND.getMessage());
        }
    }
}
