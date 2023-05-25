package TeamDustKGU.dustbackend.user.service.suspension;

import TeamDustKGU.dustbackend.common.ServiceTest;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.domain.suspension.Suspension;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static TeamDustKGU.dustbackend.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("User [Service Layer] -> SuspensionService 테스트")
public class SuspensionServiceTest extends ServiceTest {
    @Autowired
    private SuspensionService suspensionService;

    private User admin;
    private User userA;
    private User userB;
    private LocalDateTime startDate, endDate;
    private String reason;

    @BeforeEach
    void setup() {
        admin = userRepository.save(SEOKHO.toAdmin());
        userA = userRepository.save(CHAERIN.toUser());
        userB = userRepository.save(SUNKYOUNG.toUser());
        startDate = LocalDateTime.now();
        endDate = startDate.plusDays(7);
        reason = "정책 위반";
    }

    @Nested
    @DisplayName("유저 비활성화")
    class suspend {
        @Test
        @DisplayName("관리자가 아니면 유저 비활성화에 실패한다")
        void throwExceptionByUserIsNotAdmin() {
            assertThatThrownBy(() -> suspensionService.suspend(userA.getId(), userB.getId(), startDate, endDate, reason))
                    .isInstanceOf(DustException.class)
                    .hasMessage(UserErrorCode.USER_IS_NOT_ADMIN.getMessage());
        }

        @Test
        @DisplayName("이미 비활성화된 유저를 비활성화할 수 없다")
        void throwExceptionByAlreadySuspended() {
            //given
            suspensionService.suspend(admin.getId(), userA.getId(), startDate, endDate, reason);
            //when - then
            assertThatThrownBy(() -> suspensionService.suspend(admin.getId(), userA.getId(), startDate, endDate, reason))
                    .isInstanceOf(DustException.class)
                    .hasMessage(UserErrorCode.ALREADY_SUSPENDED.getMessage());
        }

        @Test
        @DisplayName("유저 비활성화 성공")
        void success() {
            //when
            Long suspendedId = suspensionService.suspend(admin.getId(), userA.getId(), startDate, endDate, reason);

            //then
            Suspension findSuspension = suspensionRepository.findById(suspendedId).orElseThrow();
            assertAll(
                    () -> assertThat(findSuspension.getSuspended().getId()).isEqualTo(userA.getId()),
                    () -> assertThat(findSuspension.getStartDate()).isEqualTo(startDate),
                    () -> assertThat(findSuspension.getEndDate()).isEqualTo(endDate),
                    () -> assertThat(findSuspension.getReason()).isEqualTo(reason)
            );
        }
    }
}
