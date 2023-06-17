package TeamDustKGU.dustbackend.user.controller;

import TeamDustKGU.dustbackend.global.annotation.ExtractPayload;
import TeamDustKGU.dustbackend.user.service.UserDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserDeleteApiController {
    private final UserDeleteService userDeleteService;

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@ExtractPayload Long userId)
    {
        userDeleteService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}