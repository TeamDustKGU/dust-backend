package TeamDustKGU.dustbackend.board.exception;

import TeamDustKGU.dustbackend.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode {
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "Board_001", "게시글 정보를 찾을 수 없습니다."),
    UPDATE_BOARD_ONLY_WRITER(HttpStatus.CONFLICT, "Board_002", "게시글 작성자만 수정할 수 있습니다."),
    DELETE_BOARD_ONLY_WRITER(HttpStatus.CONFLICT, "Board_002", "게시글 작성자만 삭제할 수 있습니다.")
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
