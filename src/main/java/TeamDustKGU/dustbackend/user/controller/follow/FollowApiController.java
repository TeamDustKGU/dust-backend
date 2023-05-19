package TeamDustKGU.dustbackend.user.controller.follow;

import TeamDustKGU.dustbackend.global.annotation.ExtractPayload;
import TeamDustKGU.dustbackend.user.controller.dto.request.FollowRequest;
import TeamDustKGU.dustbackend.user.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/follow/{followerId}")
public class FollowApiController {
    private final FollowService followService;

    @PostMapping
    public ResponseEntity<Void> register(@ExtractPayload Long followingId,
                                         @PathVariable Long followerId,
                                         @RequestBody @Valid FollowRequest request) {
        followService.register(followingId, followerId, request.boardTitle(), request.boardCreatedDate());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> cancel(@ExtractPayload Long followingId,
                                       @PathVariable Long followerId) {
        followService.cancel(followingId, followerId);
        return ResponseEntity.ok().build();
    }
}
