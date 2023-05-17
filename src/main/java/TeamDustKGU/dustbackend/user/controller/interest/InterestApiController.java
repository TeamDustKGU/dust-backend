package TeamDustKGU.dustbackend.user.controller.interest;

import TeamDustKGU.dustbackend.global.annotation.ExtractPayload;
import TeamDustKGU.dustbackend.user.controller.dto.request.InterestRequest;
import TeamDustKGU.dustbackend.user.service.interest.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/interest/{interestedId}")
public class InterestApiController {
    private final InterestService interestService;

    @PostMapping
    public ResponseEntity<Void> registerInterest(@ExtractPayload Long interestingId,
                                                 @PathVariable Long interestedId,
                                                 @RequestBody @Valid InterestRequest request) {
        interestService.register(interestingId, interestedId, request.boardTitle(), request.boardCreatedDate());
        return ResponseEntity.noContent().build();
    }
}
