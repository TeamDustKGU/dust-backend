package TeamDustKGU.dustbackend.user.domain.interest;

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
@Table(name = "user_interest")
public class Interest extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "interesting_id", nullable = false)
    private User interesting;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "interested_id", nullable = false)
    private User interested;

    @Column(name = "board_title", nullable = false)
    private String boardTitle;

    @Column(name = "board_createdDate", nullable = false)
    private LocalDateTime boardCreatedDate;

    private Interest(User interesting, User interested, String boardTitle, LocalDateTime boardCreatedDate) {
        this.interesting = interesting;
        this.interested = interested;
        this.boardTitle = boardTitle;
        this.boardCreatedDate = boardCreatedDate;
    }

    public static Interest registerInterest(User interesting, User interested, String boardTitle, LocalDateTime boardCreatedDate) {
        return new Interest(interesting, interested, boardTitle, boardCreatedDate);
    }
}
