package TeamDustKGU.dustbackend.user.service;

import TeamDustKGU.dustbackend.common.ServiceTest;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static TeamDustKGU.dustbackend.fixture.UserFixture.CHAERIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("User [Service Layer] -> UserUpdateService 테스트")
class UserUpdateServiceTest extends ServiceTest {
    @Autowired
    private UserUpdateService userUpdateService;

    @Autowired
    private UserFindService userFindService;

    private User user;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @BeforeEach
    void setup() {
        user = userRepository.save(CHAERIN.toUser());
    }

    @Nested
    @DisplayName("닉네임 수정")
    class updateNickname {
        @Test
        @DisplayName("마지막 수정시간으로부터 30일 후에 닉네임을 변경할 수 있다")
        void throwExceptionByUpdateNicknameAfter30Days() {
            // given
            userUpdateService.updateNickname(user.getId(), "닉네임1");
            flushAndClear();

            // when
            User findUser = userFindService.findById(user.getId());

            // then
            assertThatThrownBy(() -> userUpdateService.updateNickname(findUser.getId(), "닉네임2"))
                    .isInstanceOf(DustException.class)
                    .hasMessage(UserErrorCode.UPDATE_NICKNAME_AFTER_30_DAYS.getMessage());
        }

        @Test
        @DisplayName("닉네임 변경에 성공한다")
        void success() {
            // given
            userUpdateService.updateNickname(user.getId(), "닉네임1");
            flushAndClear();

            // when
            User findUser = userFindService.findById(user.getId());

            // then
            assertThat(findUser.getNicknameValue()).isEqualTo("닉네임1");
            assertThat(findUser.getNickname().getModifiedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter));
        }
    }

    @Test
    @DisplayName("지금이 특정 시간보다 30일 후인지 검증한다")
    void validateIfDateAfter30Days() {
        // given
        LocalDateTime target1 = LocalDateTime.now().plusDays(31);
        LocalDateTime target2 = LocalDateTime.now().minusDays(31);

        // when - then
        assertThatThrownBy(() -> userUpdateService.validateIfDateAfter30Days(target1))
                .isInstanceOf(DustException.class)
                .hasMessage(UserErrorCode.UPDATE_NICKNAME_AFTER_30_DAYS.getMessage());
        assertDoesNotThrow(() -> userUpdateService.validateIfDateAfter30Days(target2));
    }
}
