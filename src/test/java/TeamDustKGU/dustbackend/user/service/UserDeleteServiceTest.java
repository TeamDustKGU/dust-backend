package TeamDustKGU.dustbackend.user.service;

import TeamDustKGU.dustbackend.common.ServiceTest;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static TeamDustKGU.dustbackend.fixture.UserFixture.SEOKHO;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("User [Service Layer] -> UserDeleteService 테스트")
public class UserDeleteServiceTest extends ServiceTest {
    @Autowired
    private UserDeleteService userDeleteService;

    @Autowired
    private UserFindService userFindService;

    private User user;

    @BeforeEach
    void setup() {
        user = userRepository.save(SEOKHO.toUser());
    }

    @Test
    @DisplayName("회원 탈퇴에 성공한다")
    void success() {
        //when
        userDeleteService.deleteUser(user.getId());
        flushAndClear();

        //then
        assertThatThrownBy(() -> userFindService.findById(user.getId()))
                .isInstanceOf(DustException.class)
                .hasMessage(UserErrorCode.USER_NOT_FOUND.getMessage());
    }
}
