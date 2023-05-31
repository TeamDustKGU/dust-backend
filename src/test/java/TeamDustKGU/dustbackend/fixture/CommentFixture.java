package TeamDustKGU.dustbackend.fixture;

import TeamDustKGU.dustbackend.board.domain.Board;
import TeamDustKGU.dustbackend.comment.domain.Comment;
import TeamDustKGU.dustbackend.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentFixture {
    COMMENT_0("댓글0", null),
    COMMENT_1("댓글1", null),
    COMMENT_2("댓글2", null),
    COMMENT_3("댓글3", null),
    COMMENT_4("댓글4", null)
    ;

    private final String content;
    private final Comment parent;

    public Comment toComment(User writer, Board board) {
        return Comment.createComment(writer, board, parent, content);
    }
}
