package TeamDustKGU.dustbackend.user.controller.dto.request;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record FollowRequest(
        @NotNull(message = "관심유저의 게시글은 필수입니다.")
        String boardTitle,

        @NotNull(message = "관심유저 게시글의 작성일시는 필수입니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime boardCreatedDate
) {
}
