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
    SUNKYOUNG("abc@naver.com", "Abcdefg123!"),
    CHAERIN("def@gmail.com", "P@ssw0rd"),
    SEOKHO("ghi123@kakao.com", "1qaz@WSX"),
    NAHYUN("jklmnop@kyonggi.ac.kr", "!Password123")
    ;

    private final String email;
    private final String password;

    public User toUser() {
        return User.createUser(Email.from(email), Password.encrypt(password, ENCODER));
    }
}
