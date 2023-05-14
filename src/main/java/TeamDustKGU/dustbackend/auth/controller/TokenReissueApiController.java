package TeamDustKGU.dustbackend.auth.controller;

import TeamDustKGU.dustbackend.auth.service.TokenReissueService;
import TeamDustKGU.dustbackend.auth.service.dto.response.TokenResponse;
import TeamDustKGU.dustbackend.global.annotation.ExtractPayload;
import TeamDustKGU.dustbackend.global.annotation.ExtractToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/api/token/reissue")
public class TokenReissueApiController {
    private final TokenReissueService tokenReissueService;

    @PostMapping
    public ResponseEntity<TokenResponse> reissueTokens(@ExtractPayload Long memberId, @ExtractToken String refreshToken) {
        TokenResponse tokenResponse = tokenReissueService.reissueTokens(memberId, refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }
}
