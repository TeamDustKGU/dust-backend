package TeamDustKGU.dustbackend.board;

import TeamDustKGU.dustbackend.comment.Comment;
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
import static javax.persistence.CascadeType.PERSIST;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="board")
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name= "view", nullable = false)
    private int view;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="writer_id", referencedColumnName = "id", nullable = false)
    private User writer;

    //게시글 삭제시 달려있는 댓글 모두 삭제
    @OneToMany(mappedBy = "board", cascade = PERSIST, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public Board(String title, String content, User writer){
        this.title = title;
        this.content = content;
        this.view = 0;
        this.writer = writer;
    }

    public static Board createBoard(String title, String content, User writer){
        return new Board(title, content, writer);
    }

    public void updateTitle(String updateTitle){
        this.title = updateTitle;
    }

    public void updateContent(String updateContent){
        this.content = updateContent;
    }
}
