package TeamDustKGU.dustbackend.user.domain;


import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Password {
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*+=-])(?=.*[0-9]).{8,15}$";
    private static final Pattern PASSWORD_MATCHER = Pattern.compile(PASSWORD_PATTERN);

    private static final int REPEATING_LIMIT = 3;
    private static final int CONSECUTIVE_LIMIT = 3;

    @Column(name = "password", nullable = false, length = 200)
    private String value;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    private Password(String value) {
        this.value = value;
    }

    public static Password encrypt(String value, PasswordEncoder encoder) {
        validatePassword(value);
        return new Password(encoder.encode(value));
    }

    public void update(String value, PasswordEncoder encoder) {
        validatePassword(value);
        this.value = encoder.encode(value);
    }

    private static void validatePassword(String value) {
        validatePasswordPattern(value);
        validateRepeatingCharacters(value);
        validateConsecutiveCharacters(value);
    }

    private static void validatePasswordPattern(String value) {
        if (isNotValidPattern(value)) {
            throw DustException.type(UserErrorCode.INVALID_PASSWORD_PATTERN);
        }
    }

    private static boolean isNotValidPattern(String password) {
        return !PASSWORD_MATCHER.matcher(password).matches();
    }

    private static void validateRepeatingCharacters(String value) {
        for (int i = 0; i < value.length() - REPEATING_LIMIT; i++) {
            boolean repeating = true;
            for (int j = i; j < i + REPEATING_LIMIT; j++) {
                if (value.charAt(j) != value.charAt(j + 1)) {
                    repeating = false;
                    break;
                } else {
                    repeating = true;
                }
            }
            if (repeating) {
                throw DustException.type(UserErrorCode.INVALID_PASSWORD_PATTERN);
            }
        }
    }

    private static void validateConsecutiveCharacters(String value) {
        for (int i = 0; i < value.length() - CONSECUTIVE_LIMIT + 1; i++) {
            boolean consecutive = false;
            for (int j = i; j < i + CONSECUTIVE_LIMIT - 1; j++) {
                if (isAlphabetOrDigit(value.charAt(j)) && isAlphabetOrDigit(value.charAt(j + 1))) {
                    int charDifference = Math.abs(value.charAt(j) - value.charAt(j + 1));
                    if (charDifference == 1) {
                        consecutive = true;
                    } else {
                        consecutive = false;
                        break;
                    }
                }
            }
            if (consecutive) {
                throw DustException.type(UserErrorCode.INVALID_PASSWORD_PATTERN);
            }
        }
    }

    private static boolean isAlphabetOrDigit(char c) {
        return Character.isLetterOrDigit(c);
    }

    public boolean isSamePassword(String comparePassword, PasswordEncoder encoder) {
        return encoder.matches(comparePassword, this.value);
    }
}
