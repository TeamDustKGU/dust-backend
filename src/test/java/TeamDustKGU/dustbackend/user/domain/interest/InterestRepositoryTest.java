package TeamDustKGU.dustbackend.user.domain.interest;

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
public class InterestRepositoryTest extends RepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InterestRepository interestRepository;

    private User user1;
    private User user2;
    private User user3;
    private Interest interest;

    @BeforeEach
    void setup() {
        user1 = userRepository.save(SUNKYOUNG.toUser());
        user2 = userRepository.save(CHAERIN.toUser());
        user3 = userRepository.save(NAHYUN.toUser());
        interest = interestRepository.save(Interest.registerInterest(user1, user2, "학생들 보세요.", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)));
    }

    @Test
    @DisplayName("관심유저를 등록한 회원과 관심유저로 등록된 회원을 이용하여 관심유저 정보가 존재하는지 확인한다")
    void existsByInterestingIdAndAndInterestedId() {
        // when
        boolean actual1 = interestRepository.existsByInterestingIdAndAndInterestedId(user1.getId(), user2.getId());
        boolean actual2 = interestRepository.existsByInterestingIdAndAndInterestedId(user2.getId(), user1.getId());
        boolean actual3 = interestRepository.existsByInterestingIdAndAndInterestedId(user1.getId(), user3.getId());

        // then
        Assertions.assertAll(
                () -> assertThat(actual1).isTrue(),
                () -> assertThat(actual2).isFalse(),
                () ->  assertThat(actual3).isFalse()
        );
    }
}
