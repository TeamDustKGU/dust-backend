package TeamDustKGU.dustbackend.auth.service;

import TeamDustKGU.dustbackend.auth.domain.EmailAuth;
import TeamDustKGU.dustbackend.auth.exception.AuthErrorCode;
import TeamDustKGU.dustbackend.common.ServiceTest;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.UUID;

import static TeamDustKGU.dustbackend.fixture.UserFixture.CHAERIN;
import static TeamDustKGU.dustbackend.fixture.UserFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Auth [Service Layer] -> EmailAuthService 테스트")
class EmailAuthServiceTest extends ServiceTest {
    @Autowired
    private EmailAuthService emailAuthService;

    @Autowired
    private AuthService authService;

    @Nested
    @DisplayName("EmailAuth 생성")
    class create {
        @Test
        @DisplayName("이미 이메일 인증을 요청한 이메일입니다")
        void throwExceptionByAlreadyEmailAuth() {
            // given
            emailAuthRepository.save(EmailAuth.createEmailAuth(CHAERIN.getEmail(), UUID.randomUUID().toString()));

            // when - then
            assertThatThrownBy(() -> emailAuthService.create(CHAERIN.getEmail()))
                    .isInstanceOf(DustException.class)
                    .hasMessage(AuthErrorCode.ALREADY_EMAIL_AUTH.getMessage());
        }

        @Test
        @DisplayName("이메일 인증 요청에 성공한다")
        void success() {
            // given
            String authToken = emailAuthService.create(CHAERIN.getEmail());

            // when
            EmailAuth findEmailAuth = emailAuthRepository.findValidAuthByEmail(CHAERIN.getEmail(), authToken, LocalDateTime.now()).orElseThrow();

            // then
            assertAll(
                    () -> assertThat(findEmailAuth.getEmail()).isEqualTo(CHAERIN.getEmail()),
                    () -> assertThat(findEmailAuth.getAuthToken()).isEqualTo(authToken),
                    () -> assertThat(findEmailAuth.getExpired()).isEqualTo(false),
                    () -> assertThat(findEmailAuth.getExpireDate()).isAfter(LocalDateTime.now())
            );
        }
    }

    @Nested
    @DisplayName("이메일 인증 완료")
    class updateAuthStatus {
        private String authToken;

        @BeforeEach
        void setup() {
            authService.signup(CHAERIN.toUser());
            authToken = emailAuthService.create(CHAERIN.getEmail());

            emailAuthService.create(SUNKYOUNG.getEmail());
        }

        @Test
        @DisplayName("해당 이메일에 대해 유효한 이메일 인증 정보가 없으면 이메일 인증 완료에 실패한다")
        void throwExceptionByEmailAuthNotFound() {
            // given
            emailAuthService.updateAuthStatus(Email.from(CHAERIN.getEmail()), authToken);

            // when - then
            assertThatThrownBy(() -> emailAuthService.updateAuthStatus(Email.from(CHAERIN.getEmail()), authToken))
                    .isInstanceOf(DustException.class)
                    .hasMessage(AuthErrorCode.EMAIL_AUTH_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("회원이 조회되지 않으면 이메일 인증 완료에 실패한다")
        void throwExceptionByUserNotFound() {
            // when - then
            assertThatThrownBy(() -> emailAuthService.updateAuthStatus(Email.from(SUNKYOUNG.getEmail()), authToken))
                    .isInstanceOf(DustException.class)
                    .hasMessage(AuthErrorCode.EMAIL_AUTH_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("이메일 인증을 완료한다")
        void success() {
            // when
            emailAuthService.updateAuthStatus(Email.from(CHAERIN.getEmail()), authToken);

            // then
            assertThat(emailAuthRepository.existValidAuthByEmail(CHAERIN.getEmail(), LocalDateTime.now())).isFalse();
        }
    }
}