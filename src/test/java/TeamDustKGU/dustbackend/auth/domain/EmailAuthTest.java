package TeamDustKGU.dustbackend.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("EmailAuth 도메인 테스트")
class EmailAuthTest {
    @Test
    @DisplayName("EmailAuth의 authToken을 사용 완료로 변경한다")
    void useToken() {
        // given
        String email = "example@google.com";
        String authToken = UUID.randomUUID().toString();
        EmailAuth emailAuth = EmailAuth.createEmailAuth(email, authToken);

        // then
        emailAuth.useToken();

        // when
        assertAll(
                () -> assertThat(emailAuth.getEmail()).isEqualTo(email),
                () -> assertThat(emailAuth.getAuthToken()).isEqualTo(authToken),
                () -> assertThat(emailAuth.getExpired()).isEqualTo(true),
                () -> assertThat(emailAuth.getExpireDate()).isAfter(LocalDateTime.now())
        );
    }
}