package TeamDustKGU.dustbackend.user.controller.dto.request;

import javax.validation.constraints.NotBlank;

public record NicknameUpdateRequest(
        @NotBlank(message = "수정할 닉네임은 필수입니다.")
        String value
) {
}
