package TeamDustKGU.dustbackend.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static TeamDustKGU.dustbackend.fixture.UserFixture.CHAERIN;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("비밀번호 변경 Test")
public class PasswordTest {

    @Test
    @DisplayName("비밀번호 변경")
    void updatePassword() {
        User user1 = CHAERIN.toUser();
        user1.updatePassword("update" + user1.getPassword());

        assertThat(user1.getPasswordValue()).isEqualTo("update123456789");
    }
}