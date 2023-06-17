package TeamDustKGU.dustbackend.user.controller.dto.request;

import javax.validation.constraints.NotBlank;

public record PasswordUpdateRequest(
        @NotBlank(message = "수정할 비밀번호는 필수입니다.")
        String password
) {
}