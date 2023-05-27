package TeamDustKGU.dustbackend.auth.controller;

import TeamDustKGU.dustbackend.auth.controller.dto.request.AuthRequest;
import TeamDustKGU.dustbackend.auth.service.AuthService;
import TeamDustKGU.dustbackend.auth.service.EmailAuthService;
import TeamDustKGU.dustbackend.auth.service.dto.response.TokenResponse;
import TeamDustKGU.dustbackend.global.annotation.ExtractPayload;
import TeamDustKGU.dustbackend.user.domain.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class AuthApiController {
    private final AuthService authService;
    private final EmailAuthService emailAuthService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid AuthRequest request) {
        authService.signup(request.toEntity());

        String authToken = emailAuthService.create(request.email());
        emailAuthService.send(request.email(), authToken);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/signup/email-confirm")
    public ResponseEntity<Void> emailConfirm(@RequestParam("email") String email,
                                             @RequestParam("auth-token") String authToken) {
        emailAuthService.updateAuthStatus(Email.from(email), authToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid AuthRequest request) {
        TokenResponse tokenResponse = authService.login(Email.from(request.email()), request.password());
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@ExtractPayload Long userId) {
        authService.logout(userId);
        return ResponseEntity.noContent().build();
    }
}
