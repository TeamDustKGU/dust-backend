package TeamDustKGU.dustbackend.auth.service;

import TeamDustKGU.dustbackend.auth.exception.AuthErrorCode;
import TeamDustKGU.dustbackend.auth.service.dto.response.TokenResponse;
import TeamDustKGU.dustbackend.auth.utils.JwtTokenProvider;
import TeamDustKGU.dustbackend.global.exception.DustException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenReissueService {
    private final TokenManager tokenManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenResponse reissueTokens(Long memberId, String refreshToken) {
        if (!tokenManager.isRefreshTokenExists(memberId, refreshToken)) {
            throw DustException.type(AuthErrorCode.AUTH_INVALID_TOKEN);
        }
        String newAccessToken = jwtTokenProvider.createAccessToken(memberId);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(memberId);

        tokenManager.reissueRefreshTokenByRtrPolicy(memberId, newRefreshToken);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
