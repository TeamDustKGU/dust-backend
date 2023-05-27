package TeamDustKGU.dustbackend.auth.domain;

import TeamDustKGU.dustbackend.common.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Auth [Repository Layer] -> EmailAuthQueryRepository 테스트")
class EmailAuthQueryRepositoryTest extends RepositoryTest {
    @Autowired
    private EmailAuthRepository emailAuthRepository;

    private final EmailAuth[] emailAuths = new EmailAuth[4];

    @BeforeEach
    void setup() {
        emailAuths[0] = emailAuthRepository.save(createEmailAuth("email0@gmail.com", true, LocalDateTime.of(2022, 5, 1, 10, 30, 0)));
        emailAuths[1] = emailAuthRepository.save(createEmailAuth("email0@gmail.com", false, LocalDateTime.now().plusMinutes(3L)));
        emailAuths[2] = emailAuthRepository.save(createEmailAuth("email1@gmail.com", true, LocalDateTime.of(2023, 5, 22, 15, 30, 0)));
        emailAuths[3] = emailAuthRepository.save(createEmailAuth("email2@gmail.com", true, LocalDateTime.now()));
    }

    @Test
    void findValidAuthByEmail() {
        Optional<EmailAuth> findEmailAuth0 = emailAuthRepository.findValidAuthByEmail("email0@gmail.com", emailAuths[1].getAuthToken(), LocalDateTime.now());
        Optional<EmailAuth> findEmailAuth1 = emailAuthRepository.findValidAuthByEmail("email1@gmail.com", emailAuths[2].getAuthToken(), LocalDateTime.now());
        Optional<EmailAuth> findEmailAuth2 = emailAuthRepository.findValidAuthByEmail("email2@gmail.com", emailAuths[3].getAuthToken(), LocalDateTime.now());

        assertAll(
                () -> assertThat(findEmailAuth0).isNotEmpty(),
                () -> assertThat(findEmailAuth0.get().getEmail()).isEqualTo("email0@gmail.com"),
                () -> assertThat(findEmailAuth0.get().getAuthToken()).isEqualTo(emailAuths[1].getAuthToken()),
                () -> assertThat(findEmailAuth0.get().getExpired()).isFalse(),
                () -> assertThat(findEmailAuth0.get().getExpireDate()).isAfter(LocalDateTime.now()),
                () -> assertThat(findEmailAuth1).isEmpty(),
                () -> assertThat(findEmailAuth2).isEmpty()
        );
    }

    @Test
    void existValidAuthByEmail() {
        boolean actual0 = emailAuthRepository.existValidAuthByEmail("email0@gmail.com", LocalDateTime.now());
        boolean actual1 = emailAuthRepository.existValidAuthByEmail("email1@gmail.com", LocalDateTime.now());
        boolean actual2 = emailAuthRepository.existValidAuthByEmail("email2@gmail.com", LocalDateTime.now());

        assertAll(
                () -> assertThat(actual0).isTrue(),
                () -> assertThat(actual1).isFalse(),
                () -> assertThat(actual2).isFalse()
        );
    }

    private EmailAuth createEmailAuth(String email, Boolean expired, LocalDateTime expireDate) {
        return EmailAuth.builder()
                .email(email)
                .authToken(UUID.randomUUID().toString())
                .expired(expired)
                .expireDate(expireDate)
                .build();
    }
}