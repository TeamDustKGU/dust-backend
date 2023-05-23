package TeamDustKGU.dustbackend.user.domain.suspension;

import TeamDustKGU.dustbackend.common.RepositoryTest;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static TeamDustKGU.dustbackend.fixture.UserFixture.SEOKHO;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User [Repository Layer] -> SuspensionRepository 테스트")
public class SuspensionRepositoryTest extends RepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SuspensionRepository suspensionRepository;

    private User user;
    private Suspension suspension;

    @Test
    @DisplayName("suspensionRepository에 저장 성공")
    void saveUser() {
        //given
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(7);

        //when
        user = userRepository.save(SEOKHO.toUser());
        suspension = suspensionRepository.save(Suspension.getSuspension("정책 위반", start, end, user));

        //then
        assertThat(suspension.getId()).isEqualTo(user.getId());
    }
}
