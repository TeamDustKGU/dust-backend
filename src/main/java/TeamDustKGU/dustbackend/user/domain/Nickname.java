package TeamDustKGU.dustbackend.user.domain;

import TeamDustKGU.dustbackend.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Nickname extends BaseTimeEntity {
    @Column(name = "nickname", nullable = true, unique = true)
    private String value;

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
