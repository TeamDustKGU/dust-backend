package TeamDustKGU.dustbackend.board.domain.like;

import TeamDustKGU.dustbackend.board.domain.Board;
import TeamDustKGU.dustbackend.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "board_like")
public class BoardLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    private BoardLike(User user, Board board) {
        this.user = user;
        this.board = board;
    }

    public static BoardLike registerBoardLike(User user, Board board) {
        return new BoardLike(user, board);
    }
}
