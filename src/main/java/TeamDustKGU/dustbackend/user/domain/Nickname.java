package TeamDustKGU.dustbackend.user.domain;

import TeamDustKGU.dustbackend.global.BaseTimeEntity;
import lombok.*;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Nickname extends BaseTimeEntity {
    @Column(name = "nickname", nullable = false, unique = true)
    private String value;

    private Nickname (String value){
        this.value = value;
    }

    public static Nickname createNickname(String value){
        return new Nickname(value);
    }

    public void changeNickname(String updateNickname) {
        this.value = updateNickname;
    }
}
