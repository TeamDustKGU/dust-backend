package TeamDustKGU.dustbackend.auth.service;

import TeamDustKGU.dustbackend.auth.exception.AuthErrorCode;
import TeamDustKGU.dustbackend.auth.service.dto.response.TokenResponse;
import TeamDustKGU.dustbackend.auth.utils.JwtTokenProvider;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.Email;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.domain.UserRepository;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import TeamDustKGU.dustbackend.user.service.UserFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static TeamDustKGU.dustbackend.global.utils.PasswordEncoderUtils.ENCODER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserFindService userFindService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenManager tokenManager;

    @Transactional
    public Long signup(User user) {
        validDuplicateUserByEmail(user.getEmail());
        return userRepository.save(user).getId();
    }

    private void validDuplicateUserByEmail(Email email) {
        if (userRepository.existsByEmail(email)) {
            throw DustException.type(UserErrorCode.DUPLICATE_EMAIL);
        }
    }

    @Transactional
    public TokenResponse login(Email email, String password) {
        User user = userFindService.findByEmail(email);

        validatePassword(user.getPassword().getValue(), password);
        validateEmailAuthDone(user);

        return issueTokens(user.getId());
    }

    private void validatePassword(String encodedPassword, String rawPassword) {
        if (!ENCODER.matches(rawPassword, encodedPassword)) {
            throw DustException.type(AuthErrorCode.WRONG_PASSWORD);
        }
    }

    private void validateEmailAuthDone(User user) {
        if (user.getAuth() == 0) {
            throw DustException.type(AuthErrorCode.EMAIL_AUTH_NOT_DONE);
        }
    }

    private TokenResponse issueTokens(Long memberId) {
        String accessToken = jwtTokenProvider.createAccessToken(memberId);
        String refreshToken = jwtTokenProvider.createRefreshToken(memberId);
        tokenManager.synchronizeRefreshToken(memberId, refreshToken);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(Long userId) {
        tokenManager.deleteRefreshTokenByMemberId(userId);
    }
}
