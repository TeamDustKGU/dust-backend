package TeamDustKGU.dustbackend.user.controller.dto.request;

import javax.validation.constraints.NotNull;

public record SuspensionRequest(
        @NotNull(message = "비활성화 사유는 필수입니다.")
        String reason,

        @NotNull(message = "정지기간을 선택하세요.")
        int days
) {
}
