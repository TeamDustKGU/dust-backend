package TeamDustKGU.dustbackend.fixture;

import TeamDustKGU.dustbackend.board.domain.Board;
import TeamDustKGU.dustbackend.comment.domain.Comment;
import TeamDustKGU.dustbackend.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChildCommentFixture {
    CHILD_COMMENT_0("자식댓글0"),
    CHILD_COMMENT_1("자식댓글1"),
    CHILD_COMMENT_2("자식댓글2"),
    CHILD_COMMENT_3("자식댓글3")
    ;

    private final String content;

    public Comment toChildComment(User writer, Board board, Comment parent) {
        return Comment.createComment(content, parent, writer, board);
    }
}
