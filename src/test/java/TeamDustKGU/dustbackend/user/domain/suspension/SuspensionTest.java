package TeamDustKGU.dustbackend.user.domain.suspension;

import TeamDustKGU.dustbackend.fixture.UserFixture;
import TeamDustKGU.dustbackend.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Suspension 도메인 테스트")
public class SuspensionTest {
    private static final User SEOKHO = UserFixture.SEOKHO.toUser();

    @Test
    @DisplayName("Suspension을 생성한다.")
    void getSuspension() {
        Suspension suspension = Suspension.createSuspension("정책 위반", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT), LocalDateTime.of(2023, 6, 27, 10, 00, 00, 0000), SEOKHO);

        Assertions.assertAll(
                () -> assertThat(suspension.getReason()).isEqualTo("정책 위반"),
                () -> assertThat(suspension.getStartDate()).isEqualTo(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)),
                () -> assertThat(suspension.getEndDate()).isEqualTo(LocalDateTime.of(2023, 6, 27, 10, 00, 00, 0000)),
                () -> assertThat(suspension.getSuspended()).isEqualTo(SEOKHO)
        );
    }
}