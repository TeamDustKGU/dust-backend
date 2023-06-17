package TeamDustKGU.dustbackend.user.controller;

import TeamDustKGU.dustbackend.global.annotation.ExtractPayload;
import TeamDustKGU.dustbackend.user.controller.dto.request.NicknameUpdateRequest;
import TeamDustKGU.dustbackend.user.controller.dto.request.PasswordUpdateRequest;
import TeamDustKGU.dustbackend.user.service.UserUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/update")
public class UserUpdateApiController {
    private final UserUpdateService userUpdateService;

    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateNickname(@ExtractPayload Long userId,
                                               @RequestBody @Valid NicknameUpdateRequest request) {
        userUpdateService.updateNickname(userId, request.value());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@ExtractPayload Long userId,
                                               @RequestBody @Valid PasswordUpdateRequest request) {
        userUpdateService.updatePassword(userId, request.password());
        return ResponseEntity.ok().build();
    }
}
