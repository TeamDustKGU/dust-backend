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
public class Password extends BaseTimeEntity {
    @Column(name = "password", nullable = true)
    private String value;

    private Password(String value) {
        this.value = value;
    }

    public static Password from(String value) {
        return new Password(value);
    }

    public void update(String updateValue) {
        this.value = updateValue;
    }
}
