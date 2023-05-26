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
@RequestMapping("/api")
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping("/comment/{boardId}")
    public ResponseEntity<Void> create(@ExtractPayload Long writerId, @PathVariable Long boardId,
                                       @RequestBody @Valid CommentRequest request) {
        commentService.create(writerId, boardId, request.content());
        return ResponseEntity.created(URI.create("/board/detail/"+boardId)).build();
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> delete(@ExtractPayload Long writerId, @PathVariable Long commentId) {
        commentService.delete(writerId, commentId);
        return ResponseEntity.ok().build();
    }
}
