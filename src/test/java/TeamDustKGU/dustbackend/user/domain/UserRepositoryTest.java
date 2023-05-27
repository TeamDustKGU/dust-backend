package TeamDustKGU.dustbackend.user.domain;

import TeamDustKGU.dustbackend.common.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static TeamDustKGU.dustbackend.fixture.UserFixture.*;
import static TeamDustKGU.dustbackend.global.utils.PasswordEncoderUtils.ENCODER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("User [Repository Layer] -> UserRepository 테스트")
public class UserRepositoryTest extends RepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.save(CHAERIN.toUser());
        userRepository.save(SUNKYOUNG.toUser());
        userRepository.save(SEOKHO.toUser());
    }

    @Test
    @DisplayName("이메일을 이용해서 사용자를 조회한다")
    void findByEmail() {
        // when
        User findUserA = userRepository.findByEmail(Email.from(CHAERIN.getEmail())).orElseThrow();
        User findUserB = userRepository.findByEmail(Email.from(SUNKYOUNG.getEmail())).orElseThrow();
        User findUserC = userRepository.findByEmail(Email.from(SEOKHO.getEmail())).orElseThrow();

        // then
        assertAll(
                () -> assertThat(findUserA.getEmailValue()).isEqualTo(CHAERIN.getEmail()),
                () -> assertThat(findUserA.getPassword().isSamePassword(CHAERIN.getPassword(), ENCODER)).isTrue(),
                () -> assertThat(findUserB.getEmailValue()).isEqualTo(SUNKYOUNG.getEmail()),
                () -> assertThat(findUserB.getPassword().isSamePassword(SUNKYOUNG.getPassword(), ENCODER)).isTrue(),
                () -> assertThat(findUserC.getEmailValue()).isEqualTo(SEOKHO.getEmail()),
                () -> assertThat(findUserC.getPassword().isSamePassword(SEOKHO.getPassword(), ENCODER)).isTrue()
        );
    }

    @Test
    @DisplayName("이메일을 이용해서 사용자가 존재하는지 확인한다")
    void existsByEmail() {
        boolean actual1 = userRepository.existsByEmail(Email.from(CHAERIN.getEmail()));
        boolean actual2 = userRepository.existsByEmail(Email.from(SUNKYOUNG.getEmail()));
        boolean actual3 = userRepository.existsByEmail(Email.from(SEOKHO.getEmail()));
        boolean actual4 = userRepository.existsByEmail(Email.from(NAHYUN.getEmail()));

        assertAll(
                () -> assertThat(actual1).isTrue(),
                () -> assertThat(actual2).isTrue(),
                () -> assertThat(actual3).isTrue(),
                () -> assertThat(actual4).isFalse()
        );
    }
}
