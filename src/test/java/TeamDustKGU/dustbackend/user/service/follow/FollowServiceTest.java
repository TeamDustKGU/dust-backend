package TeamDustKGU.dustbackend.user.service.follow;

import TeamDustKGU.dustbackend.common.ServiceTest;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.domain.follow.Follow;
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

@DisplayName("User [Service Layer] -> FollowService 테스트")
public class FollowServiceTest extends ServiceTest {
    @Autowired
    private FollowService followService;

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
            assertThatThrownBy(() -> followService.register(userA.getId(), userA.getId(), "익명 게시글", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)))
                    .isInstanceOf(DustException.class)
                    .hasMessage(UserErrorCode.SELF_FOLLOW_NOT_ALLOWED.getMessage());
        }

        @Test
        @DisplayName("한 사용자에게 두 번이상 관심유저로 등록할 수 없다")
        void throwExceptionByAlreadyInterest() {
            // given
            followService.register(userA.getId(), userB.getId(), "익명 게시글1", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));

            // when - then
            assertThatThrownBy(() -> followService.register(userA.getId(), userB.getId(), "익명 게시글2", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)))
                    .isInstanceOf(DustException.class)
                    .hasMessage(UserErrorCode.ALREADY_FOLLOW.getMessage());
        }

        @Test
        @DisplayName("관심유저 등록에 성공한다")
        void success() {
            // when
            Long interestId = followService.register(userA.getId(), userB.getId(), "익명 게시글", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));

            // then
            Follow findFollow = followRepository.findById(interestId).orElseThrow();
            assertAll(
                    () -> assertThat(findFollow.getFollowing().getId()).isEqualTo(userA.getId()),
                    () -> assertThat(findFollow.getFollower().getId()).isEqualTo(userB.getId()),
                    () -> assertThat(findFollow.getBoardTitle()).isEqualTo("익명 게시글"),
                    () -> assertThat(findFollow.getBoardCreatedDate()).isEqualTo(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT))
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
            assertThatThrownBy(() -> followService.cancel(userA.getId(), userB.getId()))
                    .isInstanceOf(DustException.class)
                    .hasMessage(UserErrorCode.FOLLOW_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("관심유저 취소에 성공한다")
        void success() {
            // given
            followService.register(userA.getId(), userB.getId(), "익명 게시글", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));

            // when
            followService.cancel(userA.getId(), userB.getId());

            // then
            assertThat(followRepository.existsByFollowingIdAndFollowerId(userA.getId(), userB.getId())).isFalse();
        }
    }
}
