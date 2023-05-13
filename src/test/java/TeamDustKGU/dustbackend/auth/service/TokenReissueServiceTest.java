package TeamDustKGU.dustbackend.auth.service;

import TeamDustKGU.dustbackend.auth.domain.Token;
import TeamDustKGU.dustbackend.auth.exception.AuthErrorCode;
import TeamDustKGU.dustbackend.auth.service.dto.response.TokenResponse;
import TeamDustKGU.dustbackend.auth.utils.JwtTokenProvider;
import TeamDustKGU.dustbackend.common.ServiceTest;
import TeamDustKGU.dustbackend.global.exception.DustException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Auth [Service Layer] -> TokenReissueService 테스트")
class TokenReissueServiceTest extends ServiceTest {
    @Autowired
    private TokenReissueService tokenReissueService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private final Long MEMBER_ID = 1L;
    private String REFRESHTOKEN;

    @BeforeEach
    void setup() {
        REFRESHTOKEN = jwtTokenProvider.createRefreshToken(MEMBER_ID);
    }

    @Nested
    @DisplayName("토큰 재발급")
    class reissueTokens {
        @Test
        @DisplayName("RefreshToken이 유효하지 않으면 예외가 발생한다")
        void throwExceptionByAuthInvalidToken() {
            // when - then
            assertThatThrownBy(() -> tokenReissueService.reissueTokens(MEMBER_ID, REFRESHTOKEN))
                    .isInstanceOf(DustException.class)
                    .hasMessage(AuthErrorCode.AUTH_INVALID_TOKEN.getMessage());
        }

        @Test
        @DisplayName("RefreshToken을 통해서 AccessToken과 RefreshToken을 재발급받는데 성공한다")
        void success() {
            // given
            tokenRepository.save(Token.issueRefreshToken(MEMBER_ID, REFRESHTOKEN));

            // when
            TokenResponse response = tokenReissueService.reissueTokens(MEMBER_ID, REFRESHTOKEN);

            // then
            assertAll(
                    () -> assertThat(response).isNotNull(),
                    () -> assertThat(response).usingRecursiveComparison().isNotNull()
            );
        }
    }
}