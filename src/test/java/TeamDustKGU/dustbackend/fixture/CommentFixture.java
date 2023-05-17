package TeamDustKGU.dustbackend.fixture;

import TeamDustKGU.dustbackend.board.Board;
import TeamDustKGU.dustbackend.comment.Comment;
import TeamDustKGU.dustbackend.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentFixture {
    Comment_1("댓글1", null)
    ;

    private final String comment;
    private final Comment parent;

    public Comment toComment(User writer, Board board) {
        return Comment.createComment(comment, parent, writer, board);
    }
}
