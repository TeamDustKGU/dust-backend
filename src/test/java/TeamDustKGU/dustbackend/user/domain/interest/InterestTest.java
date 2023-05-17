package TeamDustKGU.dustbackend.user.domain.interest;

import TeamDustKGU.dustbackend.fixture.UserFixture;
import TeamDustKGU.dustbackend.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Interest 도메인 테스트")
public class InterestTest {
    private static final User SUNKYOUNG = UserFixture.SUNKYOUNG.toUser();
    private static final User CHAERIN = UserFixture.CHAERIN.toUser();
    
    @Test
    @DisplayName("Interest를 생성한다")
    void registerInterest() {
        Interest interest = Interest.registerInterest(SUNKYOUNG, CHAERIN, "익명의 게시글", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));

        Assertions.assertAll(
                () -> assertThat(interest.getInteresting()).isEqualTo(SUNKYOUNG),
                () -> assertThat(interest.getInterested()).isEqualTo(CHAERIN),
                () -> assertThat(interest.getBoardTitle()).isEqualTo("익명의 게시글"),
                () -> assertThat(interest.getBoardCreatedDate()).isEqualTo(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT))
        );
    }
}
