package TeamDustKGU.dustbackend.board.controller;

import TeamDustKGU.dustbackend.board.controller.dto.BoardRequest;
import TeamDustKGU.dustbackend.board.service.BoardService;
import TeamDustKGU.dustbackend.global.annotation.ExtractPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardApiController {
    private final BoardService boardService;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@ExtractPayload Long writerId,
                                       @RequestBody @Valid BoardRequest request) {
        boardService.create(writerId, request.title(), request.content());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update/{boardId}")
    public ResponseEntity<Void> update(@ExtractPayload Long writerId, @PathVariable Long boardId,
                                         @RequestBody @Valid BoardRequest request) {
        boardService.update(writerId, boardId, request.title(), request.content());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<Void> delete(@ExtractPayload Long writerId, @PathVariable Long boardId) {
        boardService.delete(writerId, boardId);
        return ResponseEntity.ok().build();
    }
}