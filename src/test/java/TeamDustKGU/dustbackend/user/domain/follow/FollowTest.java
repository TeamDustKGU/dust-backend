package TeamDustKGU.dustbackend.user.domain.follow;

import TeamDustKGU.dustbackend.fixture.UserFixture;
import TeamDustKGU.dustbackend.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Follow 도메인 테스트")
public class FollowTest {
    private static final User SUNKYOUNG = UserFixture.SUNKYOUNG.toUser();
    private static final User CHAERIN = UserFixture.CHAERIN.toUser();
    
    @Test
    @DisplayName("Follow를 생성한다")
    void registerFollow() {
        Follow follow = Follow.registerFollow(SUNKYOUNG, CHAERIN, "익명의 게시글", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));

        Assertions.assertAll(
                () -> assertThat(follow.getFollowing()).isEqualTo(SUNKYOUNG),
                () -> assertThat(follow.getFollower()).isEqualTo(CHAERIN),
                () -> assertThat(follow.getBoardTitle()).isEqualTo("익명의 게시글"),
                () -> assertThat(follow.getBoardCreatedDate()).isEqualTo(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT))
        );
    }
}
