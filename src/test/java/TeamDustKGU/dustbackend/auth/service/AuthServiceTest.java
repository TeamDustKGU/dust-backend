package TeamDustKGU.dustbackend.auth.service;

import TeamDustKGU.dustbackend.auth.domain.Token;
import TeamDustKGU.dustbackend.auth.exception.AuthErrorCode;
import TeamDustKGU.dustbackend.auth.service.dto.response.TokenResponse;
import TeamDustKGU.dustbackend.auth.utils.JwtTokenProvider;
import TeamDustKGU.dustbackend.common.ServiceTest;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.global.utils.PasswordEncoderUtils;
import TeamDustKGU.dustbackend.user.domain.Email;
import TeamDustKGU.dustbackend.user.domain.Password;
import TeamDustKGU.dustbackend.user.domain.Role;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import TeamDustKGU.dustbackend.user.service.UserFindService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static TeamDustKGU.dustbackend.fixture.UserFixture.CHAERIN;
import static TeamDustKGU.dustbackend.fixture.UserFixture.SUNKYOUNG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Auth [Service Layer] -> AuthService 테스트")
class AuthServiceTest extends ServiceTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserFindService userFindService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Nested
    @DisplayName("회원가입")
    class signup {
        @Test
        @DisplayName("중복된 이메일이면 회원가입에 실패한다")
        void throwExceptionByDuplicateEmail() {
            // given
            authService.signup(SUNKYOUNG.toUser());

            // when - then
            User newUser = User.createUser(Email.from(SUNKYOUNG.getEmail()), Password.encrypt("!Password123", PasswordEncoderUtils.ENCODER));
            assertThatThrownBy(() -> authService.signup(newUser))
                    .isInstanceOf(DustException.class)
                    .hasMessage(UserErrorCode.DUPLICATE_EMAIL.getMessage());
        }

        @Test
        @DisplayName("회원가입에 성공한다")
        void success() {
            // when
            Long savedUserId = authService.signup(CHAERIN.toUser());

            // then
            User findUser = userFindService.findById(savedUserId);
            Assertions.assertAll(
                    () -> assertThat(findUser.getId()).isEqualTo(savedUserId),
                    () -> assertThat(findUser.getRole()).isEqualTo(Role.USER),
                    () -> assertThat(findUser.getEmailValue()).isEqualTo(CHAERIN.getEmail()),
                    () -> assertThat(findUser.getAuth()).isEqualTo(0),
                    () -> assertThat(findUser.getPassword().isSamePassword(CHAERIN.getPassword(), PasswordEncoderUtils.ENCODER)).isTrue(),
                    () -> assertThat(findUser.getNicknameValue()).isEqualTo("A-"+savedUserId),
                    () -> assertThat(findUser.getStatus()).isEqualTo(1)
            );
        }
    }

    @Nested
    @DisplayName("로그인")
    class login {
        private Long userId;

        @BeforeEach
        void setup() {
            userId = authService.signup(CHAERIN.toUser());
        }

        @Test
        @DisplayName("가입되지 않은 이메일이면 로그인에 실패한다")
        void throwExceptionByUserNotFound() {
            // when - then
            assertThatThrownBy(() -> authService.login(Email.from("diff" + CHAERIN.getEmail()), CHAERIN.getPassword()))
                    .isInstanceOf(DustException.class)
                    .hasMessage(UserErrorCode.USER_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않으면 로그인에 실패한다")
        void throwExceptionByWrongPassword() {
            // when - then
            assertThatThrownBy(() -> authService.login(Email.from(CHAERIN.getEmail()),  "wrong" + CHAERIN.getPassword()))
                    .isInstanceOf(DustException.class)
                    .hasMessage(AuthErrorCode.WRONG_PASSWORD.getMessage());
        }

        @Test
        @DisplayName("이메일 인증이 완료되지 않으면 로그인에 실패한다")
        void throwExceptionByEmailAuthNotDone() {
            // when - then
            assertThatThrownBy(() -> authService.login(Email.from(CHAERIN.getEmail()),  CHAERIN.getPassword()))
                    .isInstanceOf(DustException.class)
                    .hasMessage(AuthErrorCode.EMAIL_AUTH_NOT_DONE.getMessage());
        }


        @Test
        @DisplayName("로그인에 성공한다")
        void success() {
            // given
            User findUser = userFindService.findById(userId);
            findUser.authenticate();

            // when
            TokenResponse tokenResponse = authService.login(Email.from(CHAERIN.getEmail()), CHAERIN.getPassword());

            // then
            assertThat(findUser.getAuth()).isEqualTo(1);
            Assertions.assertAll(
                    () -> assertThat(tokenResponse).isNotNull(),
                    () -> assertThat(jwtTokenProvider.getId(tokenResponse.accessToken())).isEqualTo(userId),
                    () -> assertThat(jwtTokenProvider.getId(tokenResponse.refreshToken())).isEqualTo(userId),
                    () -> {
                        Token findToken = tokenRepository.findByMemberId(userId).orElseThrow();
                        assertThat(findToken.getRefreshToken()).isEqualTo(tokenResponse.refreshToken());
                    }
            );
        }
    }

    @Test
    @DisplayName("로그아웃에 성공한다")
    void successLogout() {
        // given
        Long userId = authService.signup(CHAERIN.toUser());
        User user = userFindService.findById(userId);
        user.authenticate();
        authService.login(Email.from(CHAERIN.getEmail()), CHAERIN.getPassword());

        // when
        authService.logout(user.getId());

        // then
        Optional<Token> findToken = tokenRepository.findByMemberId(user.getId());
        assertThat(findToken).isEmpty();
    }
}