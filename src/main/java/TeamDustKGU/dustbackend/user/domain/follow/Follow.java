package TeamDustKGU.dustbackend.user.domain.follow;

import TeamDustKGU.dustbackend.global.BaseTimeEntity;
import TeamDustKGU.dustbackend.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "follow")
public class Follow extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @Column(name = "board_title", nullable = false)
    private String boardTitle;

    @Column(name = "board_createdDate", nullable = false)
    private LocalDateTime boardCreatedDate;

    private Follow(User following, User follower, String boardTitle, LocalDateTime boardCreatedDate) {
        this.following = following;
        this.follower = follower;
        this.boardTitle = boardTitle;
        this.boardCreatedDate = boardCreatedDate;
    }

    public static Follow registerFollow(User following, User follower, String boardTitle, LocalDateTime boardCreatedDate) {
        return new Follow(following, follower, boardTitle, boardCreatedDate);
    }
}
