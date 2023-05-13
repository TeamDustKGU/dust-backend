package TeamDustKGU.dustbackend.user.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Nickname extends BaseTimeEntity {

    @Column(name = "nickname", nullable = false, unique = true)
    private String value;

    Nickname (String value){
        this.value=value;
    }

    public Nickname changeNickname(String nickname){
        this.value = nickname;
        return new Nickname(value);
    }

}
