package TeamDustKGU.dustbackend.user.domain;

import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Nickname {
    private static final String NICKNAME_PATTERN = "^[a-zA-Z0-9_가-힣]{2,15}$";

    @Column(name = "nickname", unique = true)
    private String value;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    private Nickname(String value) {
        this.value = value;
    }

    public static Nickname from(String value) {
        validateNickname(value);
        return new Nickname(value);
    }

    public void update(String updateValue) {
        validateNickname(value);
        this.value = updateValue;
    }

    private static void validateNickname(String value) {
        validateProhibitedWord(value);
        validateNicknamePattern(value);
    }

    private static void validateNicknamePattern(String value) {
        if (!Pattern.matches(NICKNAME_PATTERN, value)) {
            throw new DustException(UserErrorCode.INVALID_NICKNAME_PATTERN);
        }
    }

    private static void validateProhibitedWord(String value) {
        if (Arrays.stream(PasswordProhibited.WORDS).anyMatch(element -> value.contains(element))) {
            throw new DustException(UserErrorCode.INVALID_NICKNAME_PATTERN);
        }
    }

    public void changeModified(LocalDateTime newModifiedDate) {
        this.modifiedDate = newModifiedDate;
    }
}
