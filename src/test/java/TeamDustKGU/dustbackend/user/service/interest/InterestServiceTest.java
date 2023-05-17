package TeamDustKGU.dustbackend.user.service.interest;

import TeamDustKGU.dustbackend.common.ServiceTest;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.domain.interest.Interest;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static TeamDustKGU.dustbackend.fixture.UserFixture.CHAERIN;
import static TeamDustKGU.dustbackend.fixture.UserFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("User [Service Layer] -> InterestService 테스트")
public class InterestServiceTest extends ServiceTest {
    @Autowired
    private InterestService interestService;

    private User userA;
    private User userB;

    @BeforeEach
    void setup() {
        userA = userRepository.save(SUNKYOUNG.toUser());
        userB = userRepository.save(CHAERIN.toUser());
    }

    @Nested
    @DisplayName("관심유저 등록")
    class register {
        @Test
        @DisplayName("본인을 관심유저로 등록할 수 없다")
        void throwExceptionBySelfInterestNotAllowed() {
            assertThatThrownBy(() -> interestService.register(userA.getId(), userA.getId(), "익명 게시글", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)))
                    .isInstanceOf(DustException.class)
                    .hasMessage(UserErrorCode.SELF_INTEREST_NOT_ALLOWED.getMessage());
        }

        @Test
        @DisplayName("한 사용자에게 두 번이상 관심유저로 등록할 수 없다")
        void throwExceptionByAlreadyInterest() {
            // given
            interestService.register(userA.getId(), userB.getId(), "익명 게시글1", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));

            // when - then
            assertThatThrownBy(() -> interestService.register(userA.getId(), userB.getId(), "익명 게시글2", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)))
                    .isInstanceOf(DustException.class)
                    .hasMessage(UserErrorCode.ALREADY_INTEREST.getMessage());
        }

        @Test
        @DisplayName("관심유저 등록에 성공한다")
        void success() {
            // when
            Long interestId = interestService.register(userA.getId(), userB.getId(), "익명 게시글", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));

            // then
            Interest findInterest = interestRepository.findById(interestId).orElseThrow();
            assertAll(
                    () -> assertThat(findInterest.getInteresting().getId()).isEqualTo(userA.getId()),
                    () -> assertThat(findInterest.getInterested().getId()).isEqualTo(userB.getId()),
                    () -> assertThat(findInterest.getBoardTitle()).isEqualTo("익명 게시글"),
                    () -> assertThat(findInterest.getBoardCreatedDate()).isEqualTo(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT))
            );
        }
    }

    @Nested
    @DisplayName("관심유저 취소")
    class cancel {
        @Test
        @DisplayName("관심유저로 등록되지 않은 회원을 취소할 수 없다")
        void throwExceptionByInterestNotFound() {
            // when - then
            assertThatThrownBy(() -> interestService.cancel(userA.getId(), userB.getId()))
                    .isInstanceOf(DustException.class)
                    .hasMessage(UserErrorCode.INTEREST_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("관심유저 취소에 성공한다")
        void success() {
            // given
            interestService.register(userA.getId(), userB.getId(), "익명 게시글", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));

            // when
            interestService.cancel(userA.getId(), userB.getId());

            // then
            assertThat(interestRepository.existsByInterestingIdAndAndInterestedId(userA.getId(), userB.getId())).isFalse();
        }
    }
}
