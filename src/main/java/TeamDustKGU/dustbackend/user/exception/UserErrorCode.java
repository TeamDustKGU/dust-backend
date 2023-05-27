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
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_005", "관심유저로 등록되어 있지 않은 회원입니다."),
    UPDATE_NICKNAME_AFTER_30_DAYS(HttpStatus.CONFLICT, "USER_006", "마지막 수정시간으로부터 30일 후에 닉네임을 변경할 수 있습니다."),
    USER_IS_NOT_ADMIN(HttpStatus.FORBIDDEN, "USER_007", "해당 회원은 관리자가 아닙니다."),
    ALREADY_ADMIN(HttpStatus.BAD_REQUEST, "USER_008", "이미 관리자로 지정된 회원입니다."),
    ALREADY_SUSPENDED(HttpStatus.CONFLICT, "USER_009", "이미 비활성화 상태인 유저입니다."),
    INVALID_PASSWORD_PATTERN(HttpStatus.BAD_REQUEST, "USER_010", "비밀번호 형식에 맞지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "USER_011", "이미 존재하는 이메일입니다."),
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}