package TeamDustKGU.dustbackend.comment.domain;

import TeamDustKGU.dustbackend.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Query Method
    int countByBoard(Board board);
    int countByParent(Comment parent);
}
