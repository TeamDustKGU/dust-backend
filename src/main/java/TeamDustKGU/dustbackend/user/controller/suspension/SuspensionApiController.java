package TeamDustKGU.dustbackend.user.controller.suspension;

import TeamDustKGU.dustbackend.global.annotation.ExtractPayload;
import TeamDustKGU.dustbackend.user.controller.dto.request.SuspensionRequest;
import TeamDustKGU.dustbackend.user.service.suspension.SuspensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/suspension/{suspendedId}")
public class SuspensionApiController {
    private final SuspensionService suspensionService;

    @PostMapping
    public ResponseEntity<Void> suspend(@ExtractPayload Long adminId,
                                        @PathVariable Long suspendedId,
                                        @RequestBody @Valid SuspensionRequest request) {
        suspensionService.suspend(adminId, suspendedId, LocalDateTime.now(), LocalDateTime.now().plusDays(request.days()), request.reason());
        return ResponseEntity.ok().build();
    }
}