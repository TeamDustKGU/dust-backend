package TeamDustKGU.dustbackend.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Email {
    @Column(name = "email", nullable = false, unique = true, updatable = false)
    private String value;

    Email(String value){
        this.value=value;
    }

    public boolean isSameEmail(Email email){
        return this.value.equals(email.getValue());
    }
}
