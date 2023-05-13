package TeamDustKGU.dustbackend.global.exception;

import lombok.Getter;

@Getter
public class DustException extends RuntimeException {
    private final ErrorCode code;

    public DustException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public static DustException type(ErrorCode code) {
        return new DustException(code);
    }
}
