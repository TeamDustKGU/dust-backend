package TeamDustKGU.dustbackend.user.domain;

import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static TeamDustKGU.dustbackend.global.utils.PasswordEncoderUtils.ENCODER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Password VO 테스트")
public class PasswordTest {
    @ParameterizedTest(name = "{index}: {0}")
    @ValueSource(strings = {"Abc@1234", "!@#helllo", "123456789101121314", "qwertyqwerty", "a|b|c|d|1|2"})
    @DisplayName("비밀번호 패턴에 맞지 않아 비밀번호 생성에 실패한다")
    void throwExceptionByInvalidNicknamePattern(String value) {
        assertThatThrownBy(() -> Password.encrypt(value, ENCODER))
                .isInstanceOf(DustException.class)
                .hasMessage(UserErrorCode.INVALID_PASSWORD_PATTERN.getMessage());
    }

    @ParameterizedTest(name = "{index}: {0}")
    @ValueSource(strings = {"12DefGh!@#", "P@ssWord!38", "Secure@2589", "NewPass-202!", "5!Passw0rd@"})
    @DisplayName("비밀번호 생성에 성공한다")
    void createPassword(String value) {
        Password password = Password.encrypt(value, ENCODER);

        assertThat(password.isSamePassword(value, ENCODER)).isTrue();
    }
}