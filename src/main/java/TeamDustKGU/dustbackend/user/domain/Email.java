package TeamDustKGU.dustbackend.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Email {
    private static final String EMAIL_PATTERN = "^[_a-zA-Z0-9-\\\\.]+@[\\\\.a-zA-Z0-9-]+\\\\.[a-zA-Z]+$";
    private static final Pattern EMAIL_MATCHER = Pattern.compile(EMAIL_PATTERN);

    @Column(name = "email", nullable = false, unique = true, updatable = false)
    private String value;


    Email(String value){
        this.value=value;
    }

    public boolean isSameEmail(Email email){
        return this.value.equals(email.getValue());
    }
}
