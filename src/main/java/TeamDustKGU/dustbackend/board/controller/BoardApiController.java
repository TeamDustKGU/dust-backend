package TeamDustKGU.dustbackend.board.controller;

import TeamDustKGU.dustbackend.board.controller.dto.request.BoardRequest;
import TeamDustKGU.dustbackend.board.service.BoardService;
import TeamDustKGU.dustbackend.global.annotation.ExtractPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardApiController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Void> create(@ExtractPayload Long writerId,
                                       @RequestBody @Valid BoardRequest request) {
        Long boardId = boardService.create(writerId, request.title(), request.content());
        return ResponseEntity.created(URI.create("/detail/"+boardId)).build();
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<Void> update(@ExtractPayload Long writerId, @PathVariable Long boardId,
                                         @RequestBody @Valid BoardRequest request) {
        boardService.update(writerId, boardId, request.title(), request.content());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> delete(@ExtractPayload Long writerId, @PathVariable Long boardId) {
        boardService.delete(writerId, boardId);
        return ResponseEntity.ok().build();
    }
}
