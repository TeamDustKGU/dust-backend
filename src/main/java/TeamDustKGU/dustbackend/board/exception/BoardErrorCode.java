package TeamDustKGU.dustbackend.board.exception;

import TeamDustKGU.dustbackend.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode {
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD_001", "게시글 정보를 찾을 수 없습니다."),
    USER_NOT_BOARD_WRITER(HttpStatus.CONFLICT, "BOARD_002", "게시글 작성자가 아닙니다."),
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
