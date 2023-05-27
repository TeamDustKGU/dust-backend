package TeamDustKGU.dustbackend.user.service;

import TeamDustKGU.dustbackend.common.ServiceTest;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.Email;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static TeamDustKGU.dustbackend.fixture.UserFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("User [Service Layer] -> UserFindService 테스트")
public class UserFindServiceTest extends ServiceTest {
    @Autowired
    private UserFindService userFindService;

    private User user;

    @BeforeEach
    void setup() {
        user = userRepository.save(SUNKYOUNG.toUser());
    }

    @Test
    @DisplayName("ID(PK)로 회원을 조회한다")
    void findById() {
        // when
        User findUser = userFindService.findById(user.getId());

        // then
        assertThat(findUser).isEqualTo(user);
        assertThatThrownBy(() -> userFindService.findById(user.getId() + 100L))
                .isInstanceOf(DustException.class)
                .hasMessage(UserErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("이메일로 회원을 조회한다")
    void findByEmail() {
        // when
        User findUser = userFindService.findByEmail(Email.from(SUNKYOUNG.getEmail()));

        // then
        assertThat(findUser).isEqualTo(user);
        assertThatThrownBy(() -> userFindService.findByEmail(Email.from("diff" + SUNKYOUNG.getEmail())))
                .isInstanceOf(DustException.class)
                .hasMessage(UserErrorCode.USER_NOT_FOUND.getMessage());
    }
}
