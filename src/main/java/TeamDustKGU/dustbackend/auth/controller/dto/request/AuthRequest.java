package TeamDustKGU.dustbackend.auth.controller.dto.request;

import TeamDustKGU.dustbackend.user.domain.Password;
import TeamDustKGU.dustbackend.user.domain.User;
import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static TeamDustKGU.dustbackend.global.utils.PasswordEncoderUtils.ENCODER;

public record AuthRequest(
        @Email
        @NotBlank(message = "이메일 작성은 필수입니다.")
        String email,

        @NotBlank(message = "비밀번호 작성은 필수입니다.")
        String password
) {
        @Builder
        public AuthRequest {}

        public User toEntity() {
                return User.builder()
                        .email(TeamDustKGU.dustbackend.user.domain.Email.from(email))
                        .password(Password.encrypt(password, ENCODER))
                        .build();
        }
}
