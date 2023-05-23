package TeamDustKGU.dustbackend.user.domain.suspension;

import TeamDustKGU.dustbackend.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_suspension")
public class Suspension {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime start_date;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime end_date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "suspended_id", nullable = false)
    private User suspended;

    private Suspension(String reason, LocalDateTime start_date, LocalDateTime end_date, User suspended) {
        this.reason = reason;
        this.start_date = start_date;
        this.end_date = end_date;
        this.suspended = suspended;
    }

    public static Suspension getSuspension(String reason, LocalDateTime start_date, LocalDateTime end_date, User suspended) {
        return new Suspension(reason, start_date, end_date, suspended);
    }
}
