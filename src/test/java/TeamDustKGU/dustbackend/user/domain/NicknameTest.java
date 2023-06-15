package TeamDustKGU.dustbackend.user.domain;

import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Nickname VO 테스트")
public class NicknameTest {
    @ParameterizedTest(name = "{index}: {0}")
    @ValueSource(strings = {"ssibal", "!@#hello", "user@", "쉬벌22", "일"})
    @DisplayName("닉네임 패턴에 맞지 않아 닉네임 생성에 실패한다")
    void throwExceptionByInvalidNicknamePattern(String value) {
        assertThatThrownBy(() -> Nickname.from(value))
                .isInstanceOf(DustException.class)
                .hasMessage(UserErrorCode.INVALID_NICKNAME_PATTERN.getMessage());
    }

    @ParameterizedTest(name = "{index}: {0}")
    @ValueSource(strings = {"user123", "이름_1", "nickname123", "한글닉네임"})
    @DisplayName("닉네임 생성에 성공한다")
    void validateNickname(String value) {
        Nickname nickname = Nickname.from(value);

        assertThat(nickname.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("닉네임을 변경한다")
    void changeNickname(){
        // given
        Nickname nickname = Nickname.from("dust");

        // when
        String change = "dustChange";
        nickname.update(change);

        // then
        assertThat(nickname.getValue()).isEqualTo(change);
    }
}
