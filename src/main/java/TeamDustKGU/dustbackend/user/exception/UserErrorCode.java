package TeamDustKGU.dustbackend.user.exception;

import TeamDustKGU.dustbackend.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "USER_001", "이메일 형식에 맞지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_002", "사용자 정보를 찾을 수 없습니다."),
    ALREADY_FOLLOW(HttpStatus.CONFLICT, "USER_003", "이미 관심유저로 등록한 회원입니다."),
    SELF_FOLLOW_NOT_ALLOWED(HttpStatus.CONFLICT, "USER_004", "본인을 관심유저로 설정할 수 없습니다."),
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_005", "관심유저로 등록되어 있지 않은 회원입니다.")
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
