package TeamDustKGU.dustbackend.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Email VO 테스트")
public class EmailTest {
    @Test
    @DisplayName("동일한 이메일인지 확인한다.")
    void isSameEmail(){
        //given
        final Email email = new Email("user1@gmail.com");
        Email email1 = new Email("user1@gmail.com");
        Email email2 = new Email("user2@gmail.com");

        ///when
        boolean result1 = email.isSameEmail(email1);
        boolean result2 = email.isSameEmail(email2);

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }
}
