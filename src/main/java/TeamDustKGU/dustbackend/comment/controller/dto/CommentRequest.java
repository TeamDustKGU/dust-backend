package TeamDustKGU.dustbackend.comment.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "내용 작성은 필수입니다.")
        @Size(max = 200, message = "내용은 200자 이내로 작성해주세요.")
        String content
) {
}
