package TeamDustKGU.dustbackend.comment;

import TeamDustKGU.dustbackend.board.Board;
import TeamDustKGU.dustbackend.global.BaseTimeEntity;
import TeamDustKGU.dustbackend.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="comment")
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment", nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent") //부모 댓글 없는 경우 null
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="writer_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id")
    private Board board;

    //부모 댓글 삭제시 달려있는 자식 댓글 모두 삭제
    @OneToMany(mappedBy = "parent", cascade = ALL, orphanRemoval = true)
    private List<Comment> childList = new ArrayList<>();

    @Builder
    public Comment(String comment, Comment parent, User writer, Board board){
        this.comment = comment;
        this.parent = parent;
        this.writer = writer;
        this.board = board;
    }

    public static Comment createComment(String comment, Comment parent, User writer, Board board){
        return new Comment(comment, parent, writer, board);
    }
}
