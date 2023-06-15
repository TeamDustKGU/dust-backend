package TeamDustKGU.dustbackend.fixture;

import TeamDustKGU.dustbackend.user.domain.Email;
import TeamDustKGU.dustbackend.user.domain.Password;
import TeamDustKGU.dustbackend.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static TeamDustKGU.dustbackend.global.utils.PasswordEncoderUtils.ENCODER;

@Getter
@RequiredArgsConstructor
public enum UserFixture {
    SUNKYOUNG("abc@naver.com", "NewPass-2023"),
    CHAERIN("def@gmail.com", "P@ssW0rd9"),
    SEOKHO("ghi123@kakao.com", "5!Password"),
    NAHYUN("jklmnop@kyonggi.ac.kr", "secure0147!")
    ;

    private final String email;
    private final String password;

    public User toUser() {
        return User.createUser(Email.from(email), Password.encrypt(password, ENCODER));
    }

    public User toAdmin() {
        return User.createAdmin(Email.from(email), Password.encrypt(password, ENCODER));
    }
}
