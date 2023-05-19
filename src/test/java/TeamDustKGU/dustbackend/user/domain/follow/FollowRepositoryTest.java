package TeamDustKGU.dustbackend.user.domain.follow;

import TeamDustKGU.dustbackend.common.RepositoryTest;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.domain.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static TeamDustKGU.dustbackend.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User [Repository Layer] -> InterestRepository 테스트")
public class FollowRepositoryTest extends RepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setup() {
        user1 = userRepository.save(SUNKYOUNG.toUser());
        user2 = userRepository.save(CHAERIN.toUser());
        user3 = userRepository.save(NAHYUN.toUser());
    }

    @Test
    @DisplayName("관심유저를 등록한 회원 ID와 관심유저로 등록된 회원 ID를 이용하여 관심유저 정보가 존재하는지 확인한다")
    void existsByFollowingIdAndAndFollowerId() {
        // given
        followRepository.save(Follow.registerFollow(user1, user2, "학생들 보세요.", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)));

        // when
        boolean actual1 = followRepository.existsByFollowingIdAndFollowerId(user1.getId(), user2.getId());
        boolean actual2 = followRepository.existsByFollowingIdAndFollowerId(user2.getId(), user1.getId());
        boolean actual3 = followRepository.existsByFollowingIdAndFollowerId(user1.getId(), user3.getId());

        // then
        Assertions.assertAll(
                () -> assertThat(actual1).isTrue(),
                () -> assertThat(actual2).isFalse(),
                () -> assertThat(actual3).isFalse()
        );
    }

    @Test
    @DisplayName("관심유저를 등록한 회원 ID와 관심유저로 등록된 회원 ID를 이용하여 관심유저 정보를 삭제한다")
    void deleteByFollowingIdAndFollowerId() {
        // given
        followRepository.save(Follow.registerFollow(user1, user2, "학생들 보세요.", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)));

        // when
        followRepository.deleteByFollowingIdAndFollowerId(user1.getId(), user2.getId());

        // then
        assertThat(followRepository.existsByFollowingIdAndFollowerId(user1.getId(), user2.getId())).isFalse();
    }
}
