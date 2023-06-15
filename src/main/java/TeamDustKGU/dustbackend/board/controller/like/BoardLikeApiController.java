package TeamDustKGU.dustbackend.board.controller.like;

import TeamDustKGU.dustbackend.board.service.like.BoardLikeService;
import TeamDustKGU.dustbackend.global.annotation.ExtractPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/{boardId}/likes")
public class BoardLikeApiController {
    private final BoardLikeService boardLikeService;

    @PostMapping
    public ResponseEntity<Void> register(@ExtractPayload Long userId, @PathVariable Long boardId) {
        boardLikeService.register(userId, boardId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> cancel(@ExtractPayload Long userId, @PathVariable Long boardId) {
        boardLikeService.cancel(userId, boardId);
        return ResponseEntity.ok().build();
    }
}

