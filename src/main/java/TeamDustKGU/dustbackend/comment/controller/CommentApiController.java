package TeamDustKGU.dustbackend.comment.controller;

import TeamDustKGU.dustbackend.comment.controller.dto.CommentRequest;
import TeamDustKGU.dustbackend.comment.service.CommentService;
import TeamDustKGU.dustbackend.global.annotation.ExtractPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping("/{boardId}")
    public ResponseEntity<Void> create(@ExtractPayload Long writerId, @PathVariable Long boardId,
                                       @RequestBody @Valid CommentRequest request) {
        commentService.create(writerId, boardId, request.content());
        return ResponseEntity.created(URI.create("/boards/detail/" + boardId)).build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@ExtractPayload Long writerId, @PathVariable Long commentId) {
        commentService.delete(writerId, commentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{boardId}/{parentId}/child-comments")
    public ResponseEntity<Void> createChild(@ExtractPayload Long writerId, @PathVariable Long boardId,
                                            @PathVariable Long parentId, @RequestBody @Valid CommentRequest request) {
        commentService.createChild(writerId, boardId, parentId, request.content());
        return ResponseEntity.created(URI.create("/board/detail/" + boardId)).build();
    }
}
