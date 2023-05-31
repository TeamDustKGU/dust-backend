package TeamDustKGU.dustbackend.comment.domain;

import TeamDustKGU.dustbackend.board.domain.Board;
import TeamDustKGU.dustbackend.global.BaseTimeEntity;
import TeamDustKGU.dustbackend.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "comment")
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, length = 200)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id") // 부모 댓글 없는 경우 null
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "writer_id", referencedColumnName = "id", nullable = false)
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", referencedColumnName = "id", nullable = false)
    private Board board;

    // 부모 댓글 삭제시 달려있는 자식 댓글 모두 삭제
    @OneToMany(mappedBy = "parent", cascade = PERSIST, orphanRemoval = true)
    private List<Comment> childList = new ArrayList<>();

    @Builder
    public Comment(String content, Comment parent, User writer, Board board){
        this.content = content;
        this.parent = parent;
        this.writer = writer;
        this.board = board;
    }

    public static Comment createComment(String content, Comment parent, User writer, Board board){
        return new Comment(content, parent, writer, board);
    }

    public void addChildComment(String content, User writer, Board board) { // 대댓글 추가
        childList.add(Comment.createComment(content, this, writer, board));
    }
}
