package TeamDustKGU.dustbackend.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Nickname {
    @Column(name = "nickname", nullable = true, unique = true)
    private String value;

    private LocalDateTime modifiedDate;

    private Nickname(String value) {
        this.value = value;
        this.modifiedDate = LocalDateTime.now();
    }

    public static Nickname from(String value) {
        return new Nickname(value);
    }

    public void update(String updateValue) {
        this.value = updateValue;
        modifiedDate = LocalDateTime.now();
    }
}
