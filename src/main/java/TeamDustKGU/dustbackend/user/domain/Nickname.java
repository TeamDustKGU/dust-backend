package TeamDustKGU.dustbackend.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Nickname {
    @Column(name = "nickname", unique = true)
    private String value;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    private Nickname(String value) {
        this.value = value;
    }

    public static Nickname from(String value) {
        return new Nickname(value);
    }

    public void update(String updateValue) {
        this.value = updateValue;
    }
}
