package TeamDustKGU.dustbackend.board.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record BoardRequest(
        @NotBlank(message = "제목 작성은 필수입니다.")
        @Size(max = 20, message = "제목은 20자 이내로 작성해주세요.")
        String title,

        @NotBlank(message = "내용 작성은 필수입니다.")
        @Size(max = 2000, message = "내용은 2000자 이내로 작성해주세요.")
        String content
) {
}
